package br.com.apollomusic.services

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.util.Log
import androidx.core.app.NotificationCompat
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import br.com.apollomusic.R

class MeuFCMService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("FCM_APOLLO", "Mensagem recebida de: ${remoteMessage.from}")

        remoteMessage.data.isNotEmpty().let {
            val nomeEstabelecimento = remoteMessage.data["nome_estabelecimento"] ?: "Um local próximo"
            val ambienteSugerido = remoteMessage.data["ambiente_sugerido"] ?: "Música personalizada"

            val titulo = "Bem-vindo ao $nomeEstabelecimento!"
            val corpo = "Toque para iniciar a playlist de $ambienteSugerido."

            showNotification(titulo, corpo)
        }
    }

    private fun showNotification(title: String, message: String) {
        val channelId = "PROXIMITY_CHANNEL"
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Notificações de Proximidade",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Alertas quando um estabelecimento parceiro está por perto."
            }
            notificationManager.createNotificationChannel(channel)
        }

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.adaptive_icon) // Trocar ícone
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        notificationManager.notify(0, notificationBuilder.build())
    }

    override fun onNewToken(token: String) {
        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String) {
        Log.d("FCM_APOLLO", "Token do dispositivo: $token")
    }
}