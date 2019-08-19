package com.example.musicappdemo4.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.musicappdemo4.R
import com.example.musicappdemo4.data.model.Song

class SongAdapter(private val listSong: List<Song>,private val listener: SongClick) : RecyclerView.Adapter<SongAdapter.ViewHolder>() {

    val songClickListener: SongClick = listener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.song_row,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listSong.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val song = listSong[position]
        holder.bindData(song,listener,position)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val txtTitle = itemView.findViewById<TextView>(R.id.txtTitleSongRow)
        val txtArtist = itemView.findViewById<TextView>(R.id.txtArtistSongRow)
        val imgCover = itemView.findViewById<ImageView>(R.id.imgCoverSongRow)

        fun bindData(song:Song, listener: SongClick, position: Int){
            txtArtist.text = song.artist
            txtTitle.text = song.title
            imgCover.setImageResource(R.drawable.disc1)
            listener.onSongClick(position)
        }
    }

    interface SongClick{
        fun onSongClick(index:Int)
    }
}
