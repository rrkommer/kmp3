package com.artefaktur.kmp3


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.text.TextUtils.replace
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*


class HomeFragment : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            //            main = it.get(ARG_PARAM1) as MainActivity
        }
        home_button_titellist?.setOnClickListener {
            val f = TitleFragment.newInstance(1)
            manager().transaction {
                replace(R.id.main_replacement, f)
            }
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        view.home_button_titellist.setOnClickListener {
            val f = TitleFragment.newInstance(1)
            pushClient(f, R.id.main_replacement)
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
