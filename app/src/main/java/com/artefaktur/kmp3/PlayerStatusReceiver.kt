package com.artefaktur.kmp3

import com.artefaktur.kmp3.database.Track

interface PlayerStatusReceiver {
  fun onStartPlayTrack(track: Track)
  fun onStopPlayTrack(track: Track)
}