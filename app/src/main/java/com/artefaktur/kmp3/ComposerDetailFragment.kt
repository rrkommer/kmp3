package com.artefaktur.kmp3

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.artefaktur.kmp3.database.Composer
import com.artefaktur.kmp3.database.Mp3Db
import kotlinx.android.synthetic.main.fragment_composer_detail.view.*

class ComposerDetailFragment : Fragment() {
    lateinit var composer: Composer


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_composer_detail, container, false)
        view.composer_detail_name.text = createDetailView()
        getMainActivity().supportFragmentManager.transaction {
            val titleFragement = TitleListFragment.newInstance(Mp3Db.getDb().getTitelFromKomposer(composer))
            replace(R.id.composer_detail_titlereplacement, titleFragement)
        }
        return view
    }

    fun createDetailView(): Spannable {
//        val sb = SpannableStringBuilder()
        val title = spannable {
            bold(size(2.0f, "${composer.name}\n")) +
                    bold("(${composer.bornYear} - ${composer.diedYear}) ${composer.country}\n")
        }
        return title
//        return sb
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
