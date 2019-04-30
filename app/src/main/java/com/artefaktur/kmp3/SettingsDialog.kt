package com.artefaktur.kmp3


import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_settings_dialog.view.*


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
    return view
  }

  fun closeDialog() {
    getMainActivity().onBackPressed()
  }


}
