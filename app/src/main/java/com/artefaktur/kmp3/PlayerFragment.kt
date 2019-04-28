package com.artefaktur.kmp3

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.artefaktur.kmp3.database.Mp3Db
import com.artefaktur.kmp3.player.PlayerService


class PlayerFragment : BaseFragment() {
    var baseDir = "/storage/3633-6130/ourMP3"
    private var listener: OnFragmentInteractionListener? = null
    private var sBound: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_player, container, false)
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    fun onStateChanged() {
    }

    fun onPlaybackCompleted() {
    }

    fun onPositionChanged(currentPosition: Int) {

    }

    private val mConnection = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {
            val mPlayerService = (iBinder as PlayerService.LocalBinder).instance
            val mMediaPlayerHolder = mPlayerService.mediaPlayerHolder!!
            mMediaPlayerHolder.playerFragment = this@PlayerFragment
            val mMusicNotificationManager = mPlayerService.musicNotificationManager
//            val mMusicNotificationManager.accent = UIUtils.getColor(mActivity, mAccent, R.color.blue)
            loadMusic()
        }

        override fun onServiceDisconnected(componentName: ComponentName) {
        }
    }
    fun loadMusic()
    {
        val db = Mp3Db.get(baseDir + "/ourMP3gwiki/acccexp", baseDir + "/mp3root/classic")
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

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
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
