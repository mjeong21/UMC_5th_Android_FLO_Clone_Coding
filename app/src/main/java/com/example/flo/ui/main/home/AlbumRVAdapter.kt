package com.example.flo.ui.main.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.data.entities.Album
import com.example.flo.databinding.ItemAlbumBinding

class AlbumRVAdapter(): RecyclerView.Adapter<AlbumRVAdapter.ViewHolder>() {

    private val albums = ArrayList<Album>()

    interface MyItemClickListener {
        fun onItemClick(album: Album)
        fun onPlayItem(album: Album)
    }

    // RecyclerView에 모든 좋아요한 함수를 담음
    @SuppressLint("NotifyDataSetChanged")
    fun addAlbums(albums: ArrayList<Album>) {
        this.albums.clear()
        this.albums.addAll(albums)

        notifyDataSetChanged()
    }


    // 전달된 객체를 저장할 변수 정의
    private lateinit var mItemClickListener: MyItemClickListener

    // MyItemClickListener 인터페이스 객체를 전달하는 메소드 선언
    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

//    fun addItem(album: Album) {
//        albumList.add(album)
//
//        // RecyclerView Adapter는 데이터가 바뀐 것을 모름 -> 데이터가 바뀌었다는 것을 알려줘야함
//        notifyDataSetChanged()
//    }
//
//    fun removeItem(position: Int) {
//        albumList.removeAt(position)
//        notifyDataSetChanged()
//    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemAlbumBinding = ItemAlbumBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(albums[position])
        holder.itemView.setOnClickListener {
            mItemClickListener.onItemClick(albums[position])
        }
        holder.binding.itemAlbumPlayImgIv.setOnClickListener {
            mItemClickListener.onPlayItem(albums[position])
        }

        //아이템 삭제
//        holder.binding.itemAlbumTitleTv.setOnClickListener {mItemClickListener.onRemoveAlbum(position)}
    }

    override fun getItemCount(): Int = albums.size

    // ViewHolder 안에 있는 아이템클릭리스너에서 인터페이스 객체의 메소드를 호출
    // 아이템뷰, position 전달
    inner class ViewHolder(val binding: ItemAlbumBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(album: Album) {
            binding.itemAlbumTitleTv.text = album.title
            binding.itemAlbumSigerTv.text = album.singer
            binding.itemAlbumCoverImgIv.setImageResource(album.coverImg!!)
        }
    }

}