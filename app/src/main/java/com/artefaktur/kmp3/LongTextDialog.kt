package com.artefaktur.kmp3


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_long_text_dialog.view.*

/**
 * A simple [Fragment] subclass.
 */
class LongTextDialog : Fragment() {
  lateinit var text: String

  companion object {
    fun openDialog(parentView: View, text: String) {
      val dlg = LongTextDialog()
      dlg.text = text
      goMainLink(dlg)
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
  ): View? {

    val view = inflater.inflate(R.layout.fragment_long_text_dialog, container, false)
    view.long_text_text.text = text
    view.long_text_close_button.setOnClickListener {
      getMainActivity().onBackPressed()
    }
    return view
  }


}
