package com.example.musicappdemo4.data.remote

import com.example.musicappdemo4.data.model.Song

interface MediaPlayerListener {

    fun nextSong()

    fun prevSong()

    fun pauseSong()

    fun playSongAtIndex(index: Int)

    fun isPlaying():Boolean

    fun getSongCurrent():Song

    fun seekTo(position: Int)

    fun getCurrentTime():Int

}
