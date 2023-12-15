package com.example.flo.ui.main.locker

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.data.entities.Song
import com.example.flo.databinding.ItemSavedalbumBinding

class SavedRVAdapter(): RecyclerView.Adapter<SavedRVAdapter.ViewHolder>() {

    private val songs = ArrayList<Song>()

    // 밖에서도 접근하기 위해서 interface 생성
    interface ItemClickListener {
        fun onRemoveSong(songId: Int)
    }

    // interface를 받아옴
    private lateinit var mMoreClickListener: ItemClickListener

    // fragment에서 클릭리스너를 구체화할 수 있는 set함수
    fun setItemClickListener(moreClickListener: ItemClickListener) {
        mMoreClickListener = moreClickListener
    }

    // RecyclerView에 모든 좋아요한 함수를 담음
    @SuppressLint("NotifyDataSetChanged")
    fun addSongs(songs: ArrayList<Song>) {
        this.songs.clear()
        this.songs.addAll(songs)

        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun removeSong(position: Int) {
        songs.removeAt(position)
        notifyDataSetChanged()
    }

    // ViewHolder를 생성해줘야 할 때 호출되는 함수
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemSavedalbumBinding = ItemSavedalbumBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    // ViewHolder에 데이터를 바인딩 해줘야 할 때마다 호출되는 함수
    // position: RecyclerView의 인덱스 id
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(songs[position])

        holder.binding.itemSavedAlbumMoreIv.setOnClickListener{
            mMoreClickListener.onRemoveSong(songs[position].id)
            removeSong(position)
        }

        // 재생, 일시정지
        holder.binding.itemSavedPlayOnIv.setOnClickListener {
            songs[position].isPlaying = true
            notifyDataSetChanged()
        }
        holder.binding.itemSavedPlayOffIv.setOnClickListener {
            songs[position].isPlaying = false
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int = songs.size

    // 아이템 뷰 객체를 갖고 있음
    inner class ViewHolder(val binding: ItemSavedalbumBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(song: Song) {
            binding.itemSavedAlbumTitleTv.text = song.title
            binding.itemSavedAlbumSigerTv.text = song.singer
            binding.itemSavedAlbumImgIv.setImageResource(song.coverImg!!)

            if(song.isPlaying) {
                binding.itemSavedPlayOnIv.visibility = View.GONE
                binding.itemSavedPlayOffIv.visibility = View.VISIBLE
            }
            else {
                binding.itemSavedPlayOnIv.visibility = View.VISIBLE
                binding.itemSavedPlayOffIv.visibility = View.GONE
            }
        }
    }
}