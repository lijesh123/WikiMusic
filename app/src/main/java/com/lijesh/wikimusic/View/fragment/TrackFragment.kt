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
import com.lijesh.wikimusic.View.adpter.trackAdapter
import com.lijesh.wikimusic.model.newListView.track
import com.lijesh.wikimusic.viewModel.MusicViewModel
import kotlinx.android.synthetic.main.track_fragment.view.*


class TrackFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    lateinit var recyclerAdapter: trackAdapter
    private var responseList= ArrayList<track>()

    private lateinit var tagViewModel: MusicViewModel
    private lateinit var trackTag:String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.track_fragment, container, false)

        //progressBar :
        val progressBar =view.spin_kit as ProgressBar
        val doubleBounce: Sprite = ThreeBounce()
        progressBar.indeterminateDrawable = doubleBounce

        //recyclerView
        recyclerView=view.recycle_view_tracks
        recyclerAdapter= trackAdapter(
            requireActivity(),
            responseList
        )
        recyclerView.layoutManager= GridLayoutManager(requireActivity(), 2)
        recyclerView.adapter=recyclerAdapter

        tagViewModel= ViewModelProvider(requireActivity()).get(MusicViewModel::class.java)
        tagViewModel.tagInfoResponse.observe(
            requireActivity(), Observer {
                    response ->
                if(response.isSuccessful) {

                    trackTag = response.body()!!.tag.name
                    tagViewModel.getTracksVM(trackTag)
                    tagViewModel.trackResponse.observe(
                        requireActivity(), Observer {
                            if (it.isSuccessful) {
                                for (element in it.body()!!.results.trackmatches.track) {
                                    val item =
                                        track(
                                            element.name,
                                            element.artist,
                                            element.image[3].text
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