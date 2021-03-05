package com.lijesh.wikimusic.View.adpter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.lijesh.wikimusic.R
import com.lijesh.wikimusic.model.newListView.track
import kotlinx.android.synthetic.main.track_single_layout_fragment.view.*

class trackAdapter(private val context: Context,private var tracks:List<track>): RecyclerView.Adapter<trackAdapter.MyViewHolder>() {
    inner class MyViewHolder(view: View):RecyclerView.ViewHolder(view) {
        var name=view.name
        var artist=view.artist_name_track
        var image=view.image
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView= LayoutInflater.from(context).inflate(
            R.layout.track_single_layout_fragment,
            parent,
            false
        )
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val response=tracks[position]
        holder.name.text=response.name
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