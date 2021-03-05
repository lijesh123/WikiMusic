package com.lijesh.wikimusic.View.adpter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.lijesh.wikimusic.R
import com.lijesh.wikimusic.model.newListView.track
import kotlinx.android.synthetic.main.album_single_layout_fragment.view.album_backGD
import kotlinx.android.synthetic.main.album_single_layout_fragment.view.album_name
import kotlinx.android.synthetic.main.album_single_layout_fragment.view.artist_name

class topTracksAdapter(private val context: Context, private var tracks: List<track>): RecyclerView.Adapter<topTracksAdapter.MyViewHolder>() {
    inner class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var albumName=view.album_name
        var artist=view.artist_name
        var image=view.album_backGD
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView= LayoutInflater.from(context).inflate(
            R.layout.top_single_item,
            parent,
            false
        )
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val response=tracks[position]
        holder.albumName.text=response.name
        holder.artist.text=response.artist

        holder.image.load(response.image) {
            crossfade(true)
            placeholder(R.drawable.imagebg)
        }


    }

    override fun getItemCount(): Int {
        return tracks.size
    }
}