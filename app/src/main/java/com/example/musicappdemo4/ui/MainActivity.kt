package com.example.musicappdemo4.ui

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicappdemo4.R
import com.example.musicappdemo4.data.model.MyMedia
import com.example.musicappdemo4.data.model.Song
import com.example.musicappdemo4.data.remote.MainContract
import com.example.musicappdemo4.data.remote.MainPresenter
import com.example.musicappdemo4.service.MusicService
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), SongAdapter.SongClick, MainContract.MainView, View.OnClickListener {

    private val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123
    private var intentService:Intent? = null
    private var musicService: MusicService? = null
    private var connection: ServiceConnection? = null
    private var presenter: MainContract.Presenter? = null
    private var isBound = false
    private val myMedia = MyMedia(this)
    private var isPlay = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkPermission()
        presenter = MainPresenter(this)
        presenter?.registerReceiver(this)
        connectService()
        btnPlayMain.setOnClickListener(this)
        imgCoverMain.setOnClickListener(this)
        txtTitleMain.setOnClickListener(this)
        txtArtistMain.setOnClickListener(this)

    }

    fun connectService(){
        intentService = Intent(this,MusicService::class.java)
        if(connection==null) {
            connection = object : ServiceConnection {
                override fun onServiceDisconnected(p0: ComponentName?) {
                    isBound = false
                }

                override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
                    isBound = true
                    val binder:MusicService.MusicServiceBinder = p1 as MusicService.MusicServiceBinder
                    musicService = binder.getService()
                    presenter?.setService(musicService!!)
                    presenter?.onServiceConnected()
                }
            }
        }
        bindService(intentService,connection!!, Context.BIND_AUTO_CREATE)
        startService(intentService)
    }

    fun closeService(){
        presenter?.stop()
        unbindService(connection!!)
        isBound = false
        stopService(intentService)
        //Log.d(App.TAG,"unbinded and stopped Service")
    }

    fun initRecycleView(){
        val listSong = presenter?.getSongs(this)
        recycleViewMain.layoutManager = LinearLayoutManager(this)
        listSong?.let { recycleViewMain.adapter = SongAdapter(it,this) }
    }

    fun initCurrentSongView(song: Song){
        imgCoverMain.setImageBitmap(myMedia.getCoverBitMap(song))
        txtArtistMain.text = song.artist
        txtTitleMain.text = song.title
        btnPlayMain.setImageResource(R.drawable.ic_pause_main)
        if(!isPlay)
            isPlay = true
    }

    override fun onSongClick(index:Int) {
        presenter?.onPickSongToPlay(index)
        //Log.d(App.TAG,"song: $index")
    }

    override fun updateInfoSongNow(song: Song) {
        initCurrentSongView(song)
    }

    override fun updateStatusPlay(icon:Int) {
        btnPlayMain.setImageResource(icon)
    }

    override fun onClick(p0: View?) {
        when(p0){
            btnPlayMain -> presenter?.onPauseSong()
            else -> {
                if(isPlay)
                    startActivity(Intent(this,PlayActivity::class.java))
            }
        }
    }

    override fun exitMain() {
        closeService()
        finish()
        //Log.d(App.TAG,"exitMain")
    }

    override fun onDestroy() {
        super.onDestroy()
        if(isBound)
            connection?.let { unbindService(it) }
        presenter?.unregisterReceiver(this)
        //Log.d(App.TAG,"onDestroyMain")

    }

    fun checkPermission(){
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE)

        } else {
            initRecycleView()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    initRecycleView()
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    finish()
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }
}
