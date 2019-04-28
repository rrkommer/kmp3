package com.artefaktur.kmp3.player

import android.os.Build
import android.text.Html
import android.text.Spanned
import java.util.*
import java.util.concurrent.TimeUnit

object MusicUtils {
    fun buildSpanned(res: String): Spanned {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            Html.fromHtml(res, Html.FROM_HTML_MODE_LEGACY)
        else
            Html.fromHtml(res)
    }

    fun formatSongDuration(duration: Long): String {
        return String.format(
            Locale.getDefault(), "%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(duration),
            TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(
                TimeUnit.MILLISECONDS.toMinutes(
                    duration
                )
            )
        )
    }

    fun formatSongTrack(trackNumber: Int): Int {
        var formatted = trackNumber
        if (trackNumber >= 1000) {
            formatted = trackNumber % 1000
        }
        return formatted
    }

}
