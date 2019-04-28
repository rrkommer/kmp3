package com.artefaktur.ourmp3.player

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.VectorDrawable
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat


import com.artefaktur.kmp3.MainActivity

import com.artefaktur.kmp3.R
import com.artefaktur.kmp3.player.MusicUtils
import com.artefaktur.kmp3.player.PlayerService


// Notification params
private const val CHANNEL_ID = "com.iven.musicplayergo.CHANNEL_ID"
private const val REQUEST_CODE = 100
const val NOTIFICATION_ID = 101

// Notification actions
const val PLAY_PAUSE_ACTION = "com.iven.musicplayergo.PLAYPAUSE"
const val NEXT_ACTION = "com.iven.musicplayergo.NEXT"
const val PREV_ACTION = "com.iven.musicplayergo.PREV"

class MusicNotificationManager(private val playerService: PlayerService) {

  //notification manager/builder
  val notificationManager: NotificationManager =
    playerService.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
  var notificationBuilder: Notification.Builder? = null

  //accent
  var accent: Int = R.color.blue

  //https://gist.github.com/Gnzlt/6ddc846ef68c587d559f1e1fcd0900d3
  private fun getLargeIcon(): Bitmap {

    val vectorDrawable = playerService.getDrawable(R.drawable.music_notification) as VectorDrawable

    val largeIconSize = playerService.resources.getDimensionPixelSize(R.dimen.notification_large_dim)
    val bitmap = Bitmap.createBitmap(largeIconSize, largeIconSize, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
    vectorDrawable.setTint(accent)
    vectorDrawable.alpha = 150
    vectorDrawable.draw(canvas)

    return bitmap
  }

  private fun playerAction(action: String): PendingIntent {

    val pauseIntent = Intent()
    pauseIntent.action = action

    return PendingIntent.getBroadcast(playerService, REQUEST_CODE, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT)
  }

  fun createNotification(): Notification {

    val song = playerService.mediaPlayerHolder!!.currentSong

    notificationBuilder = Notification.Builder(playerService, CHANNEL_ID)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      createNotificationChannel()
    }

    val openPlayerIntent = Intent(playerService, MainActivity::class.java)
    openPlayerIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
    val contentIntent = PendingIntent.getActivity(
      playerService, REQUEST_CODE,
      openPlayerIntent, 0
    )
    val title = song!!.title
    val composer = title.composer

    val artist = composer.name
    val songTitle = song.name

    val spanned = MusicUtils.buildSpanned(playerService.getString(R.string.playing_song, artist, songTitle))

    notificationBuilder!!
      .setShowWhen(false)
      .setSmallIcon(R.drawable.music_notification)
      .setLargeIcon(getLargeIcon())
      .setColor(accent)
      .setContentTitle(spanned)
      .setContentText(title.titleName)
      .setContentIntent(contentIntent)
      .addAction(notificationAction(PREV_ACTION))
      .addAction(notificationAction(PLAY_PAUSE_ACTION))
      .addAction(notificationAction(NEXT_ACTION))
      .setVisibility(Notification.VISIBILITY_PUBLIC)
    val style = Notification.MediaStyle().setShowActionsInCompactView(0, 1, 2)

    notificationBuilder!!.setStyle(style)
    return notificationBuilder!!.build()
  }

  private fun notificationAction(action: String): Notification.Action {

    var icon: Int =
      if (playerService.mediaPlayerHolder!!.state != PAUSED) R.drawable.ic_pause_notification else R.drawable.ic_play_notification

    when (action) {
      PREV_ACTION -> icon = R.drawable.ic_skip_previous_notification
      NEXT_ACTION -> icon = R.drawable.ic_skip_next_notification
    }

    return Notification.Action.Builder(icon, action, playerAction(action)).build()
  }

  @RequiresApi(26)
  private fun createNotificationChannel() {
    if (notificationManager.getNotificationChannel(CHANNEL_ID) == null) {
      val notificationChannel = NotificationChannel(
        CHANNEL_ID,
        playerService.getString(R.string.app_name),
        NotificationManager.IMPORTANCE_LOW
      )
      notificationChannel.description = playerService.getString(R.string.app_name)
      notificationChannel.enableLights(false)
      notificationChannel.enableVibration(false)
      notificationChannel.setShowBadge(false)
      notificationManager.createNotificationChannel(notificationChannel)
    }
  }
}
