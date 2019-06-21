package com.artefaktur.kmp3

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.IBinder
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import com.artefaktur.kmp3.database.Mp3Db
import com.artefaktur.kmp3.player.MusicUtils
import com.artefaktur.kmp3.player.PlayerService
import com.artefaktur.ourmp3.player.EqualizerUtils
import com.artefaktur.ourmp3.player.MediaPlayerHolder
import com.artefaktur.ourmp3.player.NOTIFICATION_ID
import com.artefaktur.ourmp3.player.PAUSED
import kotlinx.android.synthetic.main.fragment_player.*
import kotlinx.android.synthetic.main.fragment_player.view.*
import kotlinx.android.synthetic.main.player_seek.*
import kotlinx.android.synthetic.main.player_seek.view.*
import java.lang.Exception


class PlayerFragment : BaseFragment() {

  private var listener: PlayerStatusReceiver? = null
  private var sBound: Boolean = false

  private val sThemeInverted: Boolean = true
  private var mAccent: Int = R.color.blue
  private var sUserIsSeeking = false



  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.fragment_player, container, false)
    view.player_info.setOnClickListener {
      currentTrackPlaying()?.let {
        val title = it.title
        goMainLink(TitleDetailFragment.newInstance(title))
      }
    }
    view.skip_prev_button.setOnClickListener { skipPrev() }

    view.play_pause_button.setOnClickListener { resumeOrPause() }
    view.skip_next_button.setOnClickListener { skipNext() }
    return view
  }

  fun initWithMediaPlayer(mph: MediaPlayerHolder) {
    initializeSeekBar(view!!, mph)
  }


  private fun initializeSeekBar(view: View, mhp: MediaPlayerHolder) {
    view.seekTo.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
      val defaultPositionColor = view.song_position.currentTextColor
      var userSelectedPosition = 0

      override fun onStartTrackingTouch(seekBar: SeekBar?) {
        sUserIsSeeking = true
      }

      override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        if (fromUser) {
          userSelectedPosition = progress
          view.song_position.setTextColor(UIUtils.getColor(getMainActivity(), mAccent, R.color.blue))
        }
        view.song_position.text = MusicUtils.formatSongDuration(progress.toLong())
      }

      override fun onStopTrackingTouch(seekBar: SeekBar?) {
        if (sUserIsSeeking) {
          view.song_position.setTextColor(defaultPositionColor)
        }
        sUserIsSeeking = false
        mhp.seekTo(userSelectedPosition)
      }
    })
  }

  fun onPositionChanged(position: Int) {
    if (sUserIsSeeking == false) {
      seekTo.progress = position
    }
  }

  fun onStateChanged() {
    val mph = getMediaPlayerHolder()
    updatePlayingStatus(mph)
    if (mph.state != com.artefaktur.ourmp3.player.RESUMED && mph.state != PAUSED) {
      updatePlayingInfo(false, true)
    }
  }

  fun onPlaybackCompleted() {
    updateResetStatus(getMediaPlayerHolder(), true)
  }

  private fun skipPrev() {
    if (checkIsPlayer()) {
      val mph = getMediaPlayerHolder()
      mph.instantReset()
      if (mph.isReset) {
        mph.reset()
        updateResetStatus(mph, false)
      }
    }
  }

  private fun resumeOrPause() {
    if (checkIsPlayer()) {
      val mph = getMediaPlayerHolder()
      mph.resumeOrPause()
    }
  }

  private fun skipNext() {
    if (checkIsPlayer()) {
      val mph = getMediaPlayerHolder()
      mph.skip(true)
    }
  }

  private fun checkIsPlayer(): Boolean {
    val mph = getMediaPlayerHolder()
    val isPlayer = mph.isMediaPlayer
    if (!isPlayer) {
      EqualizerUtils.notifyNoSessionId(getMainActivity())
    }
    return isPlayer
  }


  private fun updateResetStatus(mMediaPlayerHolder: MediaPlayerHolder, onPlaybackCompletion: Boolean) {
    val themeColor = if (sThemeInverted) R.color.white else R.color.black
    val color = if (onPlaybackCompletion) themeColor else if (mMediaPlayerHolder.isReset) mAccent else themeColor
    skip_prev_button.setColorFilter(
      UIUtils.getColor(
        getMainActivity(), color,
        if (onPlaybackCompletion) themeColor else R.color.blue
      ), PorterDuff.Mode.SRC_IN
    )
  }

  private fun updatePlayingInfo(restore: Boolean, startPlay: Boolean) {
    val mMediaPlayerHolder = getMediaPlayerHolder()
    val mPlayerService = mMediaPlayerHolder.playerService
    val mMusicNotificationManager = mPlayerService.musicNotificationManager
    if (startPlay) {
      mMediaPlayerHolder.mediaPlayer!!.start()
      mPlayerService.startForeground(
        NOTIFICATION_ID,
        mMusicNotificationManager.createNotification()
      )
    }

    val track = mMediaPlayerHolder.currentSong
    if (track == null) {
      Log.e("", "No song selected")
      return
    }
    val durationLong = track.timeFromMp3
    seekTo.max = durationLong.toInt()
    duration.text = MusicUtils.formatSongDuration(durationLong)
    val title = track.title
    val composer = title.composer
    playing_song.text =
      MusicUtils.buildSpanned(getString(R.string.playing_song, composer.name, track.title.titleName))
    playing_album.text = title.titleName
    playing_track.text = track.name
    listener?.onStartPlayTrack(track)
    if (restore) {
      song_position.text = MusicUtils.formatSongDuration(mMediaPlayerHolder.playerPosition.toLong())
      seekTo.progress = mMediaPlayerHolder.playerPosition

      updatePlayingStatus(mMediaPlayerHolder)
      updateResetStatus(mMediaPlayerHolder, false)

      //stop foreground if coming from pause state
      if (mPlayerService.isRestoredFromPause) {
        mPlayerService.stopForeground(false)
        mPlayerService.musicNotificationManager.notificationManager.notify(
          NOTIFICATION_ID,
          mPlayerService.musicNotificationManager.notificationBuilder!!.build()
        )
        mPlayerService.isRestoredFromPause = false
      }
    }
  }

  private fun updatePlayingStatus(mMediaPlayerHolder: MediaPlayerHolder) {
    val drawable = if (mMediaPlayerHolder.state != PAUSED) R.drawable.ic_pause else R.drawable.ic_play
    play_pause_button.setImageResource(drawable)
  }

  override fun onAttach(context: Context) {
    super.onAttach(context)
    if (context is PlayerStatusReceiver) {
      listener = context
    } else {
      throw RuntimeException(context.toString() + " must implement PlayerStatusReceiver")
    }
  }

  override fun onDetach() {
    super.onDetach()
    listener = null
  }


  private val mConnection = object : ServiceConnection {
    override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {
      val mPlayerService = (iBinder as PlayerService.LocalBinder).instance
      val mMediaPlayerHolder = mPlayerService.mediaPlayerHolder!!
      mMediaPlayerHolder.playerFragment = this@PlayerFragment

      val mMusicNotificationManager = mPlayerService.musicNotificationManager
//            val mMusicNotificationManager.accent = UIUtils.getColor(mActivity, mAccent, R.color.blue)
      loadMusic()
      getMainActivity().onMediaPlayerCreated(mMediaPlayerHolder)
    }

    override fun onServiceDisconnected(componentName: ComponentName) {
    }
  }

  fun loadMusic() {
    val baseDir = getMainActivity().getSettings().getMp3Root()
    try {
      val db = Mp3Db.get(baseDir + "gwikiweb/acccexp", baseDir + "/mp3root/classic")
    } catch (ex: Exception) {
      toast("Cannot open Music db in dir: " + baseDir + "; " + ex.message)
      Mp3Db.createEmpty()
    }
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    doBindService()
  }

  private fun doBindService() {
    val mActivity = activity as AppCompatActivity
    // Establish a connection with the service.  We use an explicit
    // class name because we want a specific service implementation that
    // we know will be running in our own process (and thus won't be
    // supporting component replacement by other applications).
    val startNotStickyIntent = Intent(mActivity, PlayerService::class.java)
    mActivity.bindService(startNotStickyIntent, mConnection, Context.BIND_AUTO_CREATE)
    sBound = true
    mActivity.startService(startNotStickyIntent)
  }

  override fun onDestroy() {
    super.onDestroy()
    doUnbindService()
  }

  private fun doUnbindService() {
    if (sBound) {
      val mActivity = activity as AppCompatActivity
      // Detach our existing connection.
      mActivity.unbindService(mConnection)
      sBound = false
    }
  }


  companion object {

    @JvmStatic
    fun newInstance(main: MainActivity): PlayerFragment {
      val ret = PlayerFragment().apply {
        arguments = Bundle().apply {
        }
      }
      ret.main = main
      return ret
    }
  }
}
