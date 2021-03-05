package com.lijesh.wikimusic.View.Activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.ThreeBounce
import com.lijesh.wikimusic.R
import com.lijesh.wikimusic.View.adpter.genresAdapter
import com.lijesh.wikimusic.model.newListView.genres
import com.lijesh.wikimusic.viewModel.MusicViewModel
import kotlinx.android.synthetic.main.album_activity.*

class AlbumActivity : AppCompatActivity() {
    private lateinit var albumViewModel: MusicViewModel

    private lateinit var backBtn: ImageButton
    private lateinit var albumName: TextView
    private lateinit var albumArtist: TextView
    private lateinit var wiki_summary: TextView
    private lateinit var imageBg: ImageView

    //recyclerView:
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdapter: genresAdapter
    private var responseList= ArrayList<genres>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.album_activity)

        //progressbar :
        val progressBar = findViewById<View>(R.id.spin_kit) as ProgressBar
        val doubleBounce: Sprite = ThreeBounce()
        progressBar.indeterminateDrawable = doubleBounce

        setter()

        //getting data from intent:
        val album = intent.getStringExtra("album")
        val artist = intent.getStringExtra("artist")
        val image = intent.getStringExtra("image")
        imageBg.load(image) {
            crossfade(true)
            placeholder(R.drawable.imagebg)
        }
        Log.d("TAG", "onCreate: $album,$artist")
        albumName.text=album.toString()
        albumArtist.text=artist.toString()

        //accessing ViewModel :
        albumViewModel= ViewModelProvider(this).get(MusicViewModel::class.java)
        albumViewModel.getAlbumVM(album.toString(),artist.toString())
        albumViewModel.albumResponse.observe(this, Observer { response ->
            if (response.isSuccessful){
                val result=response.body()!!.album

                for (element in result.tags.tag) {
                    val item =
                        genres(element.name.toUpperCase())
                    responseList.add(item)
                }
                recyclerAdapter.notifyDataSetChanged()
               /* Glide.with(this.applicationContext)
                    .load(result.image[4].text)
                    .error(R.drawable.imagebg)
                    .thumbnail(
                        Glide.with(applicationContext)
                            .load(R.drawable.imagebg)
                    )
                    .into(imageBg)
*/
                if (result.wiki!=null){
                    val desc = result.wiki.summary
                    val descWithoutTags = removeTags(desc)
                    wiki_summary.text=descWithoutTags
                }else{
                    wiki_summary.text="No Description Found!!!"
                }

                progressBar.visibility=View.GONE
            }
        })

    }

    private fun setter() {
        backBtn=back_btn_album
        albumName=album_name
        albumArtist=artist_name
        recyclerView=recycle_view_album_details
        imageBg=album_activity_bg
        wiki_summary=album_summary

        //recyclerView :
        recyclerView=recycle_view_album_details
        recyclerAdapter=
            genresAdapter(this, responseList)
        recyclerView.layoutManager= LinearLayoutManager(applicationContext,LinearLayoutManager.HORIZONTAL,false)
        recyclerView.adapter=recyclerAdapter

        backBtn.setOnClickListener {
            super.onBackPressed()
        }
    }

    private fun removeTags(s: String) : String{

        var target = 0
        for (i in s.indices) {
            if (s[i] == '<') {
                target = i
                break
            }
        }
        return s.substring(0, target)
    }


}