package com.lijesh.wikimusic.View.Activities

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.lijesh.wikimusic.R
import com.lijesh.wikimusic.View.fragment.AlbumFragment
import com.lijesh.wikimusic.View.fragment.ArtistFragment
import com.lijesh.wikimusic.View.fragment.TrackFragment
import com.lijesh.wikimusic.viewModel.MusicViewModel
import kotlinx.android.synthetic.main.genre_details_activity.*

class GenreDetailActivity : AppCompatActivity() {
    private lateinit var backBtn: ImageButton
    private lateinit var tagName: TextView
    private lateinit var tagSummary: TextView

    private lateinit var tagViewModel: MusicViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.genre_details_activity)

        setter()

        //getting data from Intent :
        val tag = intent.getStringExtra("tag")
        tagName.text=tag.toString()

        //accessing ViewModel :
        tagViewModel= ViewModelProvider(this).get(MusicViewModel::class.java)
        tagViewModel.getTagInfoVM(tag.toString())
        tagViewModel.tagInfoResponse.observe(
            this, Observer { response ->
                if (response.isSuccessful) {
                    val summary=response.body()!!.tag.wiki.summary
                    tagSummary.text = removeTags(summary)

                } else {
                    Toast.makeText(this, "Make Sure Internet is Connected!", Toast.LENGTH_SHORT).show()
                }
            }
        )

        //SetUp ViewPager
        setupViewPager(viewpager)
        tabs!!.setupWithViewPager(viewpager)

    }
    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(AlbumFragment(), "Albums")
        adapter.addFragment(ArtistFragment(), "Artists")
        adapter.addFragment(TrackFragment(), "Tracks")
        viewPager.adapter = adapter
    }
    internal inner class ViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {

        private val mFragmentList = ArrayList<Fragment>()
        private val mFragmentTitleList = ArrayList<String>()

        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun addFragment(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence {
            return mFragmentTitleList[position]
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
    private fun setter() {

        backBtn=back_btn
        tagName=tag_name
        tagSummary=tag_summary

        backBtn.setOnClickListener {
            super.onBackPressed()
        }
    }
}