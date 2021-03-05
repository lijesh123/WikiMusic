package com.lijesh.wikimusic.View.Activities

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
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
import com.lijesh.wikimusic.View.adpter.topAlbumAdapter

import com.lijesh.wikimusic.View.adpter.topTracksAdapter
import com.lijesh.wikimusic.model.newListView.album
import com.lijesh.wikimusic.model.newListView.genres
import com.lijesh.wikimusic.model.newListView.track
import com.lijesh.wikimusic.viewModel.MusicViewModel
import kotlinx.android.synthetic.main.artist_activity.*
import kotlinx.android.synthetic.main.artist_activity.spin_kit

class ArtistActivity : AppCompatActivity() {

    private lateinit var artistViewModel: MusicViewModel
    private lateinit var artistName: TextView
    private lateinit var playcount: TextView
    private lateinit var followers: TextView
    private lateinit var summary: TextView
    private lateinit var imageButton: ImageButton
    private lateinit var backImagee : ImageView

    //recyclerView tag:
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdapter: genresAdapter
    private var responseList= ArrayList<genres>()

    //topTracks
    private lateinit var recyclerViewTopTracks: RecyclerView
    private lateinit var recyclerAdapterTopTracks: topTracksAdapter
    private var responseListTopTracks= ArrayList<track>()

    //topAlbums
    private lateinit var recyclerViewTopAlbums: RecyclerView
    private lateinit var recyclerAdapterTopAlbums: topAlbumAdapter
    private var responseListTopAlbums= ArrayList<album>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.artist_activity)
        // progressBar :
        val progressBar = spin_kit as ProgressBar
        val doubleBounce: Sprite = ThreeBounce()
        progressBar.indeterminateDrawable = doubleBounce

        setter()

        //getting Data from Intent :
        val artist = intent.getStringExtra("artist")
        val image = intent.getStringExtra("image")
        artistName.text=artist.toString()
        backImagee.load(image) {
            crossfade(true)
            placeholder(R.drawable.imagebg)
        }

        //accessing ViewModel :
        artistViewModel= ViewModelProvider(this).get(MusicViewModel::class.java)
        artistViewModel.getArtistVM(artist.toString())
        artistViewModel.artistResponse.observe(this, Observer { response ->
            if (response.isSuccessful) {
                //tags
                val result = response.body()!!.artist
                for (element in result.tags.tag) {
                    val item =
                        genres(element.name.toUpperCase())
                    responseList.add(item)
                }
                recyclerAdapter.notifyDataSetChanged()

                //setting Playcount & followers
                var playCount = result.stats.playcount.toInt()
                var folloWer = result.stats.listeners.toInt()
                when {
                    playCount > 1000000 -> {
                        playCount /= 1000000;
                        playcount.text = "$playCount" + "M"
                    }
                    playCount > 1000 -> {
                        playCount /= 1000;
                        playcount.text = "$playCount" + "K"
                    }
                    else -> {
                        playcount.text = "$playCount"
                    }
                }
                when {
                    folloWer > 1000000 -> {
                        folloWer /= 1000000;
                        followers.text = "$folloWer" + "M"
                    }
                    folloWer > 1000 -> {
                        folloWer /= 1000;
                        followers.text = "$folloWer" + "K"
                    }
                    else -> {
                        followers.text = "$folloWer"
                    }
                }
                //summary
                if (result.bio != null) {
                    summary.text = removeTags(result.bio.summary)
                } else {
                    summary.text = "No Bio :("
                }

                //top Tracks:
                artistViewModel.getTopTracksVM(artist.toString())
                artistViewModel.topTracksResponse.observe(this, Observer {
                    if (it.isSuccessful) {
                        for (element in it.body()!!.toptracks.track) {
                            val item =
                                track(
                                    element.name,
                                    element.artist.name,
                                    element.image[3].text
                                )
                            responseListTopTracks.add(item)
                        }
                        recyclerAdapterTopTracks.notifyDataSetChanged()
                    }

                })

                //top Album:
                artistViewModel.getTopAlbumVM(artist.toString())
                artistViewModel.topAlbumResponse.observe(this, Observer {
                    if (it.isSuccessful) {
                        for (element in it.body()!!.topalbums.album) {
                            val item =
                                album(
                                    element.name,
                                    element.artist.name,
                                    element.image[3].text
                                )
                            responseListTopAlbums.add(item)
                        }
                        recyclerAdapterTopAlbums.notifyDataSetChanged()
                    }

                })

                progressBar.visibility= View.GONE
            }
        })

    }

    private fun setter() {
        artistName=artist_name_activity
        playcount=playcount_int
        followers=followers_int
        summary=artist_summary
        imageButton=backBtn
        backImagee=backImage

        //tags
        recyclerView=recyclerview_artist_tag
        recyclerAdapter=
            genresAdapter(this, responseList)
        recyclerView.layoutManager= LinearLayoutManager(applicationContext,
            LinearLayoutManager.HORIZONTAL,false)
        recyclerView.adapter=recyclerAdapter

        //topTracks recyclerView

        recyclerViewTopTracks=topTracksRecyclerView
        recyclerAdapterTopTracks=
            topTracksAdapter(
                this,
                responseListTopTracks
            )
        recyclerViewTopTracks.layoutManager=   LinearLayoutManager(applicationContext,
            LinearLayoutManager.HORIZONTAL,false)
        recyclerViewTopTracks.adapter=recyclerAdapterTopTracks

        //topAlbum recyclerView

        recyclerViewTopAlbums=topAlbumsRecyclerView
        recyclerAdapterTopAlbums=
            topAlbumAdapter(
                this,
                responseListTopAlbums
            )
        recyclerViewTopAlbums.layoutManager=   LinearLayoutManager(applicationContext,
            LinearLayoutManager.HORIZONTAL,false)
        recyclerViewTopAlbums.adapter=recyclerAdapterTopAlbums

        imageButton.setOnClickListener {
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