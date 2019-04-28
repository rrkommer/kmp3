package com.artefaktur.kmp3.player

import android.os.Build
import android.text.Html
import android.text.Spanned

object MusicUtils {
    fun buildSpanned(res: String): Spanned {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            Html.fromHtml(res, Html.FROM_HTML_MODE_LEGACY)
        else
            Html.fromHtml(res)
    }
}
