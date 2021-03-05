package com.lijesh.wikimusic.View.adpter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.lijesh.wikimusic.View.Activities.ArtistActivity
import com.lijesh.wikimusic.R
import com.lijesh.wikimusic.model.newListView.artist
import kotlinx.android.synthetic.main.artist_single_layout_fragment.view.*

class artistAdapter(private val context: Context,private var artists: List<artist>): RecyclerView.Adapter<artistAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View):RecyclerView.ViewHolder(view) {

        var artist=view.name
        var image=view.image
        var artistCardView=view.artist_card_view
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView= LayoutInflater.from(context).inflate(
            R.layout.artist_single_layout_fragment,
            parent,
            false
        )
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val response=artists[position]
        holder.artist.text=response.name
        holder.image.load(response.image) {
            crossfade(true)
            placeholder(R.drawable.imagebg)
        }
        holder.artistCardView.setOnClickListener {
            val intent = Intent(context, ArtistActivity::class.java).apply {
                putExtra("artist", response.name)
                putExtra("image", response.image)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return artists.size
    }
}