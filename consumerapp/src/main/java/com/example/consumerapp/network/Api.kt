package com.example.consumerapp.network

import com.example.consumerapp.model.DetailGituser
import com.example.consumerapp.model.ListGituser
import com.example.consumerapp.model.SearchGituser
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {
    @GET("search/users")
    fun searchGituser(@Query("q") username: String?): Call<SearchGituser>

    @GET("users")
    fun getGituser(): Call<List<ListGituser>>

    @GET("users/{username}")
    fun getGituserDetail(@Path("username") path: String): Call<DetailGituser>

    @GET("users/{username}/followers")
    fun getGituserFollowers(@Path("username") path: String): Call<List<ListGituser>>

    @GET("users/{username}/following")
    fun getGituserFollowing(@Path("username") path: String): Call<List<ListGituser>>
}
