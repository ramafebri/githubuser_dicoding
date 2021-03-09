package com.example.githubuser.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubuser.model.ListGituser
import com.example.githubuser.model.SearchGituser
import com.example.githubuser.network.NetworkConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel: ViewModel() {
    val listGituser = MutableLiveData<List<ListGituser>>()

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

    fun getGituser(): LiveData<List<ListGituser>> {
        return listGituser
    }
}