package br.com.apollomusic.services

import android.app.*
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.*
import br.com.apollomusic.R
import br.com.apollomusic.data.api.LocationApiService
import br.com.apollomusic.data.api.OwnerApiService
import br.com.apollomusic.domain.location.dto.VerifyLocationRequest
import br.com.apollomusic.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
@AndroidEntryPoint
class LocationTrackingService: Service()  {
    @Inject lateinit var locationApiService: LocationApiService
    @Inject lateinit var tokenManager: TokenManager

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private val CHANNEL_ID = "LocationChannelApollo"
    private val NOTIFICATION_ID = 1

    override fun onCreate() {
        super.onCreate()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        setupLocationCallback()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(NOTIFICATION_ID, createNotification())

        startLocationUpdates()

        return START_STICKY
    }

    private fun setupLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->

                    CoroutineScope(Dispatchers.IO).launch {
                        sendLocationToBackend(location)
                    }

                }
            }

        }
    }

    private fun startLocationUpdates() {
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            5000L
        ).setMinUpdateIntervalMillis(3000L)
            .build()

        try {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                mainLooper
            )
        } catch (e: SecurityException) {
            Log.e("LOC_TRACKER", "Permissão de localização negada.", e)
        }
    }

    private fun fetchCurrentLocation(onLocationFetched: (String, String) -> Unit) {
        try {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val lat = location.latitude.toString()
                    val lon = location.longitude.toString()
                    onLocationFetched(lat, lon)
                } else {
                    Log.w("LOC_TRACKER", "Localização atual retornou nula.")
                }
            }.addOnFailureListener { e ->
                Log.e("LOC_TRACKER", "Falha ao buscar localização atual.", e)
            }
        } catch (e: SecurityException) {
            Log.e("LOC_TRACKER", "Permissão necessária não concedida.", e)
        }
    }

    private suspend fun sendLocationToBackend(location: Location) {

        Log.i("LOC_TRACKER", "Enviando localização: Lat=${location.latitude}, Lon=${location.longitude}")

        val fcmToken = tokenManager.fcmToken.first()

        if (fcmToken.isNotEmpty()) {
            val request = VerifyLocationRequest(
                deviceToken = fcmToken,
                latitude = location.latitude.toString(),
                longitude = location.longitude.toString()
            )

            CoroutineScope(Dispatchers.IO).launch {

                try {
                    val response = locationApiService.verifyLocationEstablishment(request)

                    when {
                        response.isSuccessful -> {
                            Log.i("LOC_TRACKER", "Localização enviada com sucesso (HTTP ${response.code()})")
                        }

                        response.code() == 400 -> {
                            Log.e("LOC_TRACKER", "Erro 400: Requisição inválida -> ${response.errorBody()?.string()}")
                        }

                        response.code() == 500 -> {
                            Log.e("LOC_TRACKER", "Erro 500: Erro interno do servidor")
                        }

                        else -> {
                            Log.e("LOC_TRACKER", "Erro HTTP ${response.code()}: ${response.errorBody()?.string()}")
                        }
                    }

                } catch (e: Exception) {
                    Log.e("LOC_TRACKER", "Falha de conexão: ${e.message}", e)
                }

            }
        } else {
            Log.e("API_CALL", "Token FCM ausente. Não é possível enviar localização.")
        }

    }

    private fun createNotification(): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Rastreamento de Localização",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Apollo Music em Ação")
            .setContentText("Buscando música personalizada por ambiente.")
            .setSmallIcon(R.drawable.adaptive_icon)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationClient.removeLocationUpdates(locationCallback)
        Log.d("LOC_TRACKER", "Serviço de rastreamento encerrado.")
    }
}
