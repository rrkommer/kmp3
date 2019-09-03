package com.artefaktur.kmp3

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.artefaktur.kmp3.database.Composer
import com.artefaktur.kmp3.database.Mp3Db
import com.artefaktur.kmp3.database.Title
import kotlinx.android.synthetic.main.fragment_composer_detail.view.*
import android.content.Intent



class ComposerDetailFragment : BaseFragment() {

  lateinit var composer: Composer
  var titleListFragement: TitleListFragment? = null
  var mediaListFragement: MediaListFragment? = null
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.fragment_composer_detail, container, false)
    view.composer_detail_name.text = createDetailView()
    view.composer_detail_name.setMovementMethod(LinkMovementMethod.getInstance())
    showTitle(view)

    view.composer_detail_button_showtitle.setOnClickListener {
      showTitle(view)
    }
    view.composer_detail_button_showmedien.setOnClickListener {
      showMedien(view)
    }
    return view
  }

  private fun showMedien(view: View?) {
    getMainActivity().supportFragmentManager.transaction {
      val elements = Mp3Db.getDb().getMediaByComposer(composer)
      titleListFragement = null
      mediaListFragement = MediaListFragment.newInstance(elements, withFilter = false)
      mediaListFragement?.let {
        replace(R.id.composer_detail_titlereplacement, it)
      }
    }
  }

  fun showTitle(view: View) {
    getMainActivity().supportFragmentManager.transaction {
      val elements = Mp3Db.getDb().getTitelFromKomposer(composer)
      mediaListFragement = null
      titleListFragement = TitleListFragment.newInstance(elements)
      titleListFragement?.let {
        replace(R.id.composer_detail_titlereplacement, it)
      }
    }
  }

  fun createDetailView(): Spannable {
    val title = spannable {
      bold(size(2.0f, "${composer.name}\n")) +
              bold("(${composer.bornYear} - ${composer.diedYear}) ${composer.country}\n") +
      clickSpan(normal("Im Web Suchen")) {
        var name = composer.firstLastName

        openURL("https://www.google.com/search?q=" + name)
      }}
    return title
//        return sb
  }

  override fun onSearch(text: String) {
    titleListFragement?.let { it.onSearch(text) }
    mediaListFragement?.let { it.onSearch(text) }
  }

  override fun onResetSearch() {
    titleListFragement?.let { it.onResetSearch() }
    mediaListFragement?.let { it.onResetSearch() }
  }

  companion object {
    @JvmStatic
    fun newInstance(composer: Composer): ComposerDetailFragment {
      val ret = ComposerDetailFragment()
      ret.composer = composer
      return ret
    }
  }
}
