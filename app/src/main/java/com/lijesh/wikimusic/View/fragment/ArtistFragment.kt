package com.lijesh.wikimusic.View.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.ThreeBounce
import com.lijesh.wikimusic.R
import com.lijesh.wikimusic.View.adpter.artistAdapter
import com.lijesh.wikimusic.model.newListView.artist
import com.lijesh.wikimusic.viewModel.MusicViewModel
import kotlinx.android.synthetic.main.artist_fragment.view.*

class ArtistFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    lateinit var recyclerAdapter: artistAdapter
    private lateinit var tagViewModel: MusicViewModel
    private var responseList= ArrayList<artist>()

    private lateinit var artistTag:String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.artist_fragment, container, false)

        val progressBar = view.findViewById<View>(R.id.spin_kit) as ProgressBar
        val doubleBounce: Sprite = ThreeBounce()
        progressBar.indeterminateDrawable = doubleBounce
        //recyclerView
        recyclerView=view.recycle_view_artists
        recyclerAdapter= artistAdapter(
            requireActivity(),
            responseList
        )
        recyclerView.layoutManager= GridLayoutManager(requireActivity(), 2)
        recyclerView.adapter=recyclerAdapter

        //getting tag name by MVVM
        tagViewModel= ViewModelProvider(requireActivity()).get(MusicViewModel::class.java)
        tagViewModel.tagInfoResponse.observe(
            requireActivity(), Observer {
                    response ->
                if(response.isSuccessful) {

                    artistTag = response.body()!!.tag.name
                    tagViewModel.getArtistsVM(artistTag)
                    tagViewModel.artistsResponse.observe(
                        requireActivity(), Observer {
                            if (it.isSuccessful) {
                                for (element in it.body()!!.results.artistmatches.artist) {
                                    val item =
                                        artist(
                                            element.name,
                                            element.image[4].text
                                        )
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