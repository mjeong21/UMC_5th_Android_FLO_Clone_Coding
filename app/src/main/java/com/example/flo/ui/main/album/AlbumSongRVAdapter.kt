package com.example.flo.ui.main.album

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.data.remote.AlbumResponse
import com.example.flo.data.remote.AlbumResult
import com.example.flo.databinding.ItemAlbumSongBinding

class AlbumSongRVAdapter(val context: Context, val result: ArrayList<AlbumResult>, private val albumResponse: AlbumResponse): RecyclerView.Adapter<AlbumSongRVAdapter.ViewHolder>() {



    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemAlbumSongBinding = ItemAlbumSongBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = result[position].title
        holder.singer.text = result[position].singer
        holder.num.text = result[position].songIdx.toString()

        if (result[position].isTitleSong == "F") {
            holder.titleIcon.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = albumResponse.result.size

    inner class ViewHolder(val binding: ItemAlbumSongBinding): RecyclerView.ViewHolder(binding.root) {
        val title : TextView = binding.songMusicTitleTv
        val singer: TextView = binding.songMusicSingerTv
        val num : TextView = binding.songNumberTv
        val titleIcon : TextView = binding.songTitleIconTv
    }
}