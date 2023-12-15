package com.example.flo.ui.main.home

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.flo.data.remote.PodcastResult
import com.example.flo.databinding.ItemAlbumBinding


class PodcastRVAdapter(val context: Context, val result: PodcastResult): RecyclerView.Adapter<PodcastRVAdapter.ViewHolder>() {

    interface MyItemClickListener {
        fun onItemClick(position: Int)
    }

    // 전달된 객체를 저장할 변수 정의
    private lateinit var mItemClickListener: MyItemClickListener

    // MyItemClickListener 인터페이스 객체를 전달하는 메소드 선언
    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemAlbumBinding = ItemAlbumBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(result.albums[position].coverImgUrl == "" || result.albums[position].coverImgUrl == null) {

        } else { // 데이터 중에 커버 이미지가 비어있거나 null이 아니라면
            Log.d("image", result.albums[position].coverImgUrl)

            // 받아온 커버 이미지 아이템에서 바인딩한 커버 이미지에 적용
            Glide.with(context).load(result.albums[position].coverImgUrl).into(holder.coverImg)
        }
        holder.title.text = result.albums[position].title
        holder.singer.text = result.albums[position].singer

        holder.itemView.setOnClickListener {
            mItemClickListener.onItemClick(position)
        }

    }

    override fun getItemCount(): Int = result.albums.size

    inner class ViewHolder(val binding: ItemAlbumBinding): RecyclerView.ViewHolder(binding.root) {
        val coverImg : ImageView = binding.itemAlbumCoverImgIv
        val title : TextView = binding.itemAlbumTitleTv
        val singer: TextView = binding.itemAlbumSigerTv
    }
}