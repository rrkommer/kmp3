package com.artefaktur.kmp3


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.text.TextUtils.replace
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.artefaktur.kmp3.database.Mp3Db
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*


class HomeFragment : BaseFragment() {

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    // Inflate the layout for this fragment
    val view = inflater.inflate(R.layout.fragment_home, container, false)
    view.home_button_composerlist.setOnClickListener {
      val f = ComposerListFragment.newInstance(Mp3Db.getDb().composers)
      getMainActivity().pushClient(f, R.id.main_replacement)
    }
    view.home_button_medialist.setOnClickListener {
      var sortedMedia = ArrayList(Mp3Db.getDb().media)
      val sortedl = sortedMedia.sortedByDescending { it.createdDate }

      val f = MediaListFragment.newInstance(sortedl)
      getMainActivity().pushClient(f, R.id.main_replacement)
    }
    view.home_button_orchester.setOnClickListener {
      val sortedl = Mp3Db.getDb().orchester.sortedBy { it.name }
      val orchesterItemContainer = OrchesterItemContainer(sortedl)
      val frag = GenericItemFragment.newInstance(orchesterItemContainer)
      getMainActivity().pushClient(frag, R.id.main_replacement)
    }
    view.home_button_interpret.setOnClickListener {
      val sortedl = Mp3Db.getDb().interpreten.sortedBy { it.name }
      val itemContainer = InterpretItemContainer(sortedl)
      val frag = GenericItemFragment.newInstance(itemContainer)
      getMainActivity().pushClient(frag, R.id.main_replacement)
    }
    view.home_button_dirigent.setOnClickListener {
      val sortedl = Mp3Db.getDb().dirigenten.sortedBy { it.name }
      val itemContainer = DirigentItemContainer(sortedl)
      val frag = GenericItemFragment.newInstance(itemContainer)
      getMainActivity().pushClient(frag, R.id.main_replacement)
    }
    return view
  }


  companion object {
    @JvmStatic
    fun newInstance(main: MainActivity): HomeFragment {
      val ret = HomeFragment().apply {
        arguments = Bundle().apply {
        }
      }
      ret.main = main
      return ret
    }
  }
}
