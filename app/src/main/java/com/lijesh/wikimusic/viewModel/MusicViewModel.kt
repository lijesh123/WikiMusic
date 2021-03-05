package com.lijesh.wikimusic.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lijesh.wikimusic.model.album.albums
import com.lijesh.wikimusic.model.albumActivity.api.albumactivity
import com.lijesh.wikimusic.model.artistActivity.artistActivity
import com.lijesh.wikimusic.model.artistActivity.topalbums.topalbums
import com.lijesh.wikimusic.model.artistActivity.toptracks.toptracks
import com.lijesh.wikimusic.model.artists.artists
import com.lijesh.wikimusic.model.tagInfo.tagInfo
import com.lijesh.wikimusic.model.topGenre.Toptags
import com.lijesh.wikimusic.model.tracks.tracks
import com.lijesh.wikimusic.repo.Repository
import com.lijesh.wikimusic.utils.Constants.Companion.API_KEY
import kotlinx.coroutines.launch
import retrofit2.Response

class MusicViewModel(): ViewModel() {

    val tagRepo= Repository()
    var apiResponse: MutableLiveData<Response<Toptags>> = MutableLiveData()
    fun getDataAllVM(method:String){
        viewModelScope.launch {
            val response=tagRepo.getAllDataRepo(method,API_KEY)
            apiResponse.value=response
        }
    }

    var tagInfoResponse:MutableLiveData<Response<tagInfo>> = MutableLiveData()
    fun getTagInfoVM(tag:String){
        viewModelScope.launch {

            val response=tagRepo.getTagInfoRepo(tag, API_KEY)
            tagInfoResponse.value=response
        }
    }

    val albumsResponse:MutableLiveData<Response<albums>> = MutableLiveData()
    fun getAlbumsVM(album:String){
        viewModelScope.launch {
            val response=tagRepo.getAlbumsRepo(album, API_KEY)
            albumsResponse.value=response
        }

    }

    val artistsResponse:MutableLiveData<Response<artists>> = MutableLiveData()
    fun getArtistsVM(artist:String){
        viewModelScope.launch {
            val response=tagRepo.getArtistsRepo(artist, API_KEY)
            artistsResponse.value=response
        }

    }

    val trackResponse:MutableLiveData<Response<tracks>> = MutableLiveData()
    fun getTracksVM(track:String){
        viewModelScope.launch {
            val response=tagRepo.getTracksRepo(track, API_KEY)
            trackResponse.value=response
        }

    }

    //Album Activity:
    val albumResponse:MutableLiveData<Response<albumactivity>> = MutableLiveData()
    fun getAlbumVM(album:String,artist: String){
        viewModelScope.launch {
            val response=tagRepo.getAlbumRepo(album,artist, API_KEY)
            albumResponse.value=response
        }

    }

    val artistResponse:MutableLiveData<Response<artistActivity>> = MutableLiveData()
    fun getArtistVM(artist:String){
        viewModelScope.launch {
            val response=tagRepo.getArtistRepo(artist, API_KEY)
            artistResponse.value=response
        }

    }

    val topTracksResponse:MutableLiveData<Response<toptracks>> = MutableLiveData()
    fun getTopTracksVM(artist:String){
        viewModelScope.launch {
            val response=tagRepo.getTopTracksRepo(artist, API_KEY)
            topTracksResponse.value=response
        }

    }

    val topAlbumResponse:MutableLiveData<Response<topalbums>> = MutableLiveData()
    fun getTopAlbumVM(artist:String){
        viewModelScope.launch {
            val response=tagRepo.getTopAlbumRepo(artist, API_KEY)
            topAlbumResponse.value=response
        }

    }


}
