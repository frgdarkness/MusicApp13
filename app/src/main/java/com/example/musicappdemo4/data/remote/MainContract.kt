package com.example.musicappdemo4.data.remote

import android.content.Context
import com.example.musicappdemo4.data.model.Song
import com.example.musicappdemo4.service.MusicService

interface MainContract {
    interface MainView{
        fun updateInfoSongNow(song: Song)

        fun updateStatusPlay(icon:Int)

        fun exitMain()
    }

    interface PlayView{
        fun updateInfoSongNowActiPlay(song: Song)

        fun updateStatusPLayActiPlay(icon: Int)

        fun updateSeekbar(progress: Int)

        fun exitActivityPlay()
    }

    interface Presenter{

        fun setService(musicService: MusicService)

        fun onServiceConnected()

        fun onServiceDisconnected()

        fun registerReceiver(context: Context)

        fun unregisterReceiver(context: Context)

        fun getSongs(context: Context):List<Song>

        fun onPauseSong()

        fun onNextSong()

        fun onPrevSong()

        fun onSeekTo(progress:Int)

        fun onPickSongToPlay(index:Int)

        fun setTimeSong()

        fun stop()

        fun exitActiPlay()
    }
}
