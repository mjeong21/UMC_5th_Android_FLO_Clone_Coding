package com.example.flo.ui.main.look

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.flo.data.remote.FloChartResult
import com.example.flo.databinding.ItemSongBinding

class SongRVAdapter(val context: Context, val result: FloChartResult): RecyclerView.Adapter<SongRVAdapter.ViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemSongBinding = ItemSongBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // holder.bind(result.songs[position]

        if(result.songs[position].coverImgUrl == "" || result.songs[position].coverImgUrl == null) {

        } else { // 데이터 중에 커버 이미지가 비어있거나 null이 아니라면
            Log.d("image", result.songs[position].coverImgUrl)

            // 받아온 커버 이미지 아이템에서 바인딩한 커버 이미지에 적용
            Glide.with(context).load(result.songs[position].coverImgUrl).into(holder.coverImg)
        }
        holder.title.text = result.songs[position].title
        holder.singer.text = result.songs[position].singer

    }

    override fun getItemCount(): Int = result.songs.size

    inner class ViewHolder(val binding: ItemSongBinding): RecyclerView.ViewHolder(binding.root) {
        val coverImg : ImageView = binding.itemSongImgIv
        val title : TextView = binding.itemSongTitleTv
        val singer: TextView = binding.itemSongSingerTv
    }

}