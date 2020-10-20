package com.example.githubuser.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubuser.model.DetailGituser
import com.example.githubuser.model.ListGituser
import com.example.githubuser.model.SearchGituser
import com.example.githubuser.network.NetworkConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GituserViewModel : ViewModel() {
    val listGituser = MutableLiveData<List<ListGituser>>()
    val followersGituser = MutableLiveData<List<ListGituser>>()
    val followingGituser = MutableLiveData<List<ListGituser>>()
    val detailGituser = MutableLiveData<DetailGituser>()
    val usernameGituser = MutableLiveData<String>()
    val loadingDetail = MutableLiveData<Boolean>()

    fun setGituser() {
        viewModelScope.launch {
            NetworkConfig().getService()
                .getGituser()
                .enqueue(object : Callback<List<ListGituser>> {
                    override fun onFailure(call: Call<List<ListGituser>>, t: Throwable) {
                        val error : String = t.localizedMessage ?: ""
                        Log.d("Exception", error)
                    }
                    override fun onResponse(
                        call: Call<List<ListGituser>>,
                        response: Response<List<ListGituser>>
                    ) {
                        listGituser.postValue(response.body())
                    }
                })
        }
    }

    fun searchGituser(username: String?) {
        viewModelScope.launch {
            NetworkConfig().getService()
                .searchGituser(username)
                .enqueue(object : Callback<SearchGituser> {
                    override fun onFailure(call: Call<SearchGituser>, t: Throwable) {
                        val error : String = t.localizedMessage ?: ""
                        Log.d("Exception", error)
                    }

                    override fun onResponse(
                        call: Call<SearchGituser>,
                        response: Response<SearchGituser>
                    ) {
                        val listItems = response.body()?.items
                       listGituser.postValue(listItems)
                    }
                })
        }
    }

    fun setDetailGituser(username: String) {
        viewModelScope.launch {
            NetworkConfig().getService().getGituserDetail(username)
                .enqueue(object : Callback<DetailGituser> {
                    override fun onFailure(call: Call<DetailGituser>, t: Throwable) {
                        val error : String = t.localizedMessage ?: ""
                        Log.d("Exception", error)
                    }

                    override fun onResponse(
                        call: Call<DetailGituser>,
                        response: Response<DetailGituser>
                    ) {
                        val detailItems = response.body()
                        detailGituser.postValue(detailItems)
                    }
                })
        }
    }

    fun setFollowersGituser(username: String) {
        viewModelScope.launch {
            loadingDetail.value = true
            NetworkConfig().getService()
                .getGituserFollowers(username)
                .enqueue(object : Callback<List<ListGituser>> {
                    override fun onFailure(call: Call<List<ListGituser>>, t: Throwable) {
                        val error : String = t.localizedMessage ?: ""
                        Log.d("Exception", error)
                        loadingDetail.value = false
                    }
                    override fun onResponse(
                        call: Call<List<ListGituser>>,
                        response: Response<List<ListGituser>>
                    ) {
                        followersGituser.postValue(response.body())
                        loadingDetail.value = false
                    }
                })
        }
    }

    fun setFollowingGituser(username: String) {
        viewModelScope.launch {
            loadingDetail.value = true
            NetworkConfig().getService()
                .getGituserFollowing(username)
                .enqueue(object : Callback<List<ListGituser>> {
                    override fun onFailure(call: Call<List<ListGituser>>, t: Throwable) {
                        val error : String = t.localizedMessage ?: ""
                        Log.d("Exception", error)
                        loadingDetail.value = false
                    }
                    override fun onResponse(
                        call: Call<List<ListGituser>>,
                        response: Response<List<ListGituser>>
                    ) {
                        followingGituser.postValue(response.body())
                        loadingDetail.value = false
                    }
                })
        }
    }

    fun setUsernameGituser(newUsername: String) {
        usernameGituser.value = newUsername
    }

    fun getGituser(): LiveData<List<ListGituser>> {
        return listGituser
    }

    fun getFollowersGituser(): LiveData<List<ListGituser>> {
        return followersGituser
    }

    fun getFollowingGituser(): LiveData<List<ListGituser>> {
        return followingGituser
    }

    fun getDetailGituser(): LiveData<DetailGituser> {
        return detailGituser
    }

    fun getUsernameGituser(): LiveData<String> {
        return usernameGituser
    }

    fun getLoadingDetail(): LiveData<Boolean> {
        return loadingDetail
    }
}