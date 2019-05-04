package com.artefaktur.kmp3


import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.artefaktur.kmp3.intdb.toIsoString
import kotlinx.android.synthetic.main.fragment_settings_dialog.view.*
import java.io.*


class SettingsDialog : Fragment() {
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.fragment_settings_dialog, container, false)
    val settings = getMainActivity().getSettings()
    view.settings_cancel.setOnClickListener {
      closeDialog()
    }
    view.settings_save.setOnClickListener {
      settings.storeMp3Root(view.settings_path.text.toString())
      closeDialog()
    }
    view.settings_path.setText(settings.getMp3Root())

    view.settings_export.setOnClickListener {
      createExternalStoragePrivateFile()
    }
    return view
  }


  fun closeDialog() {
    getMainActivity().onBackPressed()
  }

  fun createExternalStoragePrivateFile() {
    val alluseages = getMainActivity().intDb.mediaDao().getAll()
    val file = File(getMainActivity().getExternalFilesDir(null), "km3MediaUsage.txt")
    try {
      val os = OutputStreamWriter(BufferedOutputStream(FileOutputStream(file))).use {
        it.write("MediaPK|Usage|lastHeared|Rating\n")
        for (line in alluseages) {
          val line = "${line.mediaPk}|${line.usage}|${line.lastHeared?.toIsoString() ?: ""}|${line.rating}\n"
          it.write(line)
        }
      }
    } catch (e: IOException) {
      // Unable to create file, likely because external storage is
      // not currently mounted.
      Log.w("ExternalStorage", "Error writing $file", e)
    }

  }


}
