package com.lijesh.wikimusic.View.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.ThreeBounce
import com.lijesh.wikimusic.R
import com.lijesh.wikimusic.View.adpter.albumAdapter
import com.lijesh.wikimusic.model.newListView.album
import com.lijesh.wikimusic.viewModel.MusicViewModel
import kotlinx.android.synthetic.main.album_fragment.view.*

class AlbumFragment : Fragment() {

    private lateinit var recyclerView:RecyclerView
    lateinit var recyclerAdapter: albumAdapter
    private lateinit var tagViewModel: MusicViewModel
    private var responseList= ArrayList<album>()

    private lateinit var albumTag:String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.album_fragment, container, false)

        //progressBar :
        val progressBar =view.spin_kit as ProgressBar
        val doubleBounce: Sprite = ThreeBounce()
        progressBar.indeterminateDrawable = doubleBounce

        //recyclerView
        recyclerView=view.recycle_view_albums
        recyclerAdapter= albumAdapter(
            requireActivity(),
            responseList
        )
        recyclerView.layoutManager= GridLayoutManager(requireActivity(), 2)
        recyclerView.adapter=recyclerAdapter

        //getting tag name by MVVM
        tagViewModel= ViewModelProvider(requireActivity()).get(MusicViewModel::class.java)
        tagViewModel.tagInfoResponse.observe(
            requireActivity(), Observer { response ->
                if (response.isSuccessful) {
                    albumTag = response.body()!!.tag.name
                    Log.d("TAG", "onCreateView: $albumTag")
                    tagViewModel.getAlbumsVM(albumTag)
                    tagViewModel.albumsResponse.observe(
                        requireActivity(), Observer {
                            if (it.isSuccessful) {
                                for (element in it.body()!!.results.albummatches.album) {
                                    val item =
                                        album(
                                            element.name,
                                            element.artist,
                                            element.image[3].text
                                        )
                                    Log.d("album", "onCreateView: ${element.name},${element.artist}")
                                    responseList.add(item)
                                }
                                recyclerAdapter.notifyDataSetChanged()
                                progressBar.visibility=View.GONE

                            }else {
                                Toast.makeText(getActivity(), "Make Sure Internet is Connected!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )

                }else {
                    Toast.makeText(getActivity(), "Make Sure Internet is Connected!", Toast.LENGTH_SHORT).show()
                }
            }
        )

        return view
    }

}