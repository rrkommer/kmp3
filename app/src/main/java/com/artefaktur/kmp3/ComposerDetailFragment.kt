package com.artefaktur.kmp3

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.artefaktur.kmp3.database.Composer
import com.artefaktur.kmp3.database.Mp3Db
import kotlinx.android.synthetic.main.fragment_composer_detail.view.*

class ComposerDetailFragment : Fragment() {
    lateinit var composer: Composer
//    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_composer_detail, container, false)
        view.composer_detail_name.text = composer.name
        getMainActivity().supportFragmentManager.transaction {
            val titleFragement = TitleListFragment.newInstance(Mp3Db.getDb().getTitelFromKomposer(composer))
            replace(R.id.composer_detail_titlereplacement, titleFragement)
        }
        return view
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
//        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
//        if (context is OnFragmentInteractionListener) {
//            listener = context
//        } else {
//            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
//        }
    }

    override fun onDetach() {
        super.onDetach()
//        listener = null
    }


//    interface OnFragmentInteractionListener {
//
//        fun onFragmentInteraction(composer: Composer)
//    }

    companion object {

        @JvmStatic
        fun newInstance(composer: Composer): ComposerDetailFragment {
            val ret = ComposerDetailFragment()
            ret.composer = composer
            return ret
        }
    }
}
