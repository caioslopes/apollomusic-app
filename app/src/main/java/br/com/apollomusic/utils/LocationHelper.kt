package br.com.apollomusic.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Singleton
class LocationHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(): Pair<String, String> = suspendCoroutine { cont ->
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        try {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    cont.resume(Pair(location.latitude.toString(), location.longitude.toString()))
                } else {
                    cont.resume(Pair("0.0", "0.0"))
                }
            }.addOnFailureListener {
                cont.resume(Pair("0.0", "0.0"))
            }
        } catch (e: Exception) {
            cont.resume(Pair("0.0", "0.0"))
        }
    }
}
