package com.artefaktur.kmp3


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_generic_detail.view.*

class GenericDetailFragment<T : Any> : BaseFragment() {
  lateinit var item: T
  lateinit var itemContainer: GenericItemContainer<T>
  var titleListFragement: TitleListFragment? = null
  var mediaListFragement: MediaListFragment? = null
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.fragment_generic_detail, container, false)

    view.generic_detail_text.text = itemContainer.getDetailText(item)
    showTitle(view)

    view.generic_detail_button_showtitle.setOnClickListener {
      showTitle(view)
    }
    view.generic_detail_button_showmedien.setOnClickListener {
      showMedien(view)
    }
    return view
  }

  private fun showMedien(view: View?) {
    getMainActivity().supportFragmentManager.transaction {
      val elements = itemContainer.getMedienForItem(item)
      titleListFragement = null
      mediaListFragement = MediaListFragment.newInstance(elements)
      mediaListFragement?.let {
        replace(R.id.generic_detail_titlereplacement, it)
      }
    }
  }

  fun showTitle(view: View) {
    getMainActivity().supportFragmentManager.transaction {
      val elements = itemContainer.getTitleForItem(item)
      mediaListFragement = null
      titleListFragement = TitleListFragment.newInstance(elements)
      titleListFragement?.let {
        replace(R.id.generic_detail_titlereplacement, it)
      }
    }
  }

  companion object {
    @JvmStatic
    fun <T : Any> newInstance(item: T, itemContainer: GenericItemContainer<T>): GenericDetailFragment<T> {
      val ret = GenericDetailFragment<T>()
      ret.item = item
      ret.itemContainer = itemContainer
      return ret
    }
  }
}
