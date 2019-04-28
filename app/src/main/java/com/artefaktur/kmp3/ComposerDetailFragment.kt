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
import com.artefaktur.kmp3.database.Title
import kotlinx.android.synthetic.main.fragment_composer_detail.view.*

class ComposerDetailFragment : BaseFragment() {

    lateinit var composer: Composer
    lateinit var titleFragement: TitleListFragment
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_composer_detail, container, false)
        view.composer_detail_name.text = createDetailView()
        getMainActivity().supportFragmentManager.transaction {
            val elements = Mp3Db.getDb().getTitelFromKomposer(composer)
            titleFragement = TitleListFragment.newInstance(elements)
            replace(R.id.composer_detail_titlereplacement, titleFragement)
        }
        return view
    }

    fun createDetailView(): Spannable {
        val title = spannable {
            bold(size(2.0f, "${composer.name}\n")) +
                    bold("(${composer.bornYear} - ${composer.diedYear}) ${composer.country}\n")
        }
        return title
//        return sb
    }

    override fun onSearch(text: String) {
        titleFragement.onSearch(text)
    }

    override fun onResetSearch() {
        titleFragement.onResetSearch()
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
