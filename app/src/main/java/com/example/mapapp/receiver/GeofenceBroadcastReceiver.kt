package com.example.mapapp.receiver

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.mapapp.MapsActivity
import com.example.mapapp.R
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent

class GeofenceBroadcastReceiver : BroadcastReceiver() {

    private val TAG = "Broadcast =>"
    private val CHANNEL_ID = "Geofence Channel"

    override fun onReceive(context: Context?, intent: Intent?) {
        //Added Notification
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if (geofencingEvent.hasError()) {
            val errorMessage = GeofenceStatusCodes
                .getStatusCodeString(geofencingEvent.errorCode)
            Log.e(TAG, errorMessage)
            return
        }
        // Get the transition type.
        val geofenceTransition = geofencingEvent.geofenceTransition

        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {

            val triggeringGeofences = geofencingEvent.triggeringGeofences
            sendNotification(context!!)
            Log.i(TAG, triggeringGeofences.toString())
        } else {
            // Log the error.
            Log.e(TAG, "Error")
        }


    }

    private fun sendNotification(
        context: Context
    ) {

        val notificationManager =
            getSystemService(context, NotificationManager::class.java) as NotificationManager

        //This is to create a Channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val mChannel =
                NotificationChannel(CHANNEL_ID, "Channel1", notificationManager.importance)
            mChannel.enableLights(true)
            mChannel.lightColor = Color.GREEN
            mChannel.enableVibration(false)
            notificationManager.createNotificationChannel(mChannel)

        }
        /*
        * Intent to send in the Notification
        *
        * */
        val contentIntent = Intent(context, MapsActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )


        /*
        *
        * Notification Builder
        * */

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("GeoFence App")
            .setContentText("You have entered the geofence area")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setAutoCancel(true)
            .setDefaults(Notification.DEFAULT_ALL)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setLights(ContextCompat.getColor(context, R.color.purple_200), 50, 10)
            .setContentIntent(pendingIntent)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .build()

        notificationManager.notify(1234, notification)

    }
}