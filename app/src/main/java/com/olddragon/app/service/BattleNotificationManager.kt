package com.olddragon.app.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.olddragon.app.MainActivity
import com.olddragon.app.R

/**
 * Gerenciador de notificações de batalha
 */
class BattleNotificationManager(private val context: Context) {

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    companion object {
        const val CHANNEL_ID = "battle_channel"
        const val CHANNEL_NAME = "Batalhas"
        const val NOTIFICATION_ID = 1
        const val DEATH_NOTIFICATION_ID = 2
    }

    init {
        createNotificationChannel()
    }

    /**
     * Cria canal de notificação (necessário para Android 8+)
     */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notificações de batalha do Old Dragon"
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * Cria notificação para batalha em andamento
     */
    fun createBattleNotification(
        playerName: String,
        enemyName: String,
        round: Int,
        playerHp: Byte,
        enemyHp: Byte
    ): android.app.Notification {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Batalha em Andamento!")
            .setContentText("$playerName (HP: $playerHp) vs $enemyName (HP: $enemyHp)")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
    }

    /**
     * Envia notificação de morte do personagem
     */
    fun sendDeathNotification(playerName: String) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Seu personagem morreu!")
            .setContentText("$playerName foi derrotado em batalha.")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(0, 500, 250, 500))
            .build()

        notificationManager.notify(DEATH_NOTIFICATION_ID, notification)
    }

    /**
     * Envia notificação de vitória
     */
    fun sendVictoryNotification(playerName: String, enemyName: String, xpGained: Int) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Vitória!")
            .setContentText("$playerName derrotou $enemyName! XP ganho: $xpGained")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(DEATH_NOTIFICATION_ID, notification)
    }

    /**
     * Atualiza notificação de batalha em andamento
     */
    fun updateBattleNotification(
        playerName: String,
        enemyName: String,
        round: Int,
        playerHp: Byte,
        enemyHp: Byte
    ) {
        val notification = createBattleNotification(playerName, enemyName, round, playerHp, enemyHp)
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    /**
     * Cancela todas as notificações
     */
    fun cancelAll() {
        notificationManager.cancelAll()
    }
}
