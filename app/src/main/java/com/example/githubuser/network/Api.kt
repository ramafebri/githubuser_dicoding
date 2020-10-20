package com.example.githubuser.network

import com.example.githubuser.model.DetailGituser
import com.example.githubuser.model.ListGituser
import com.example.githubuser.model.SearchGituser
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {
    @GET("search/users")
    @Headers("Authorization: token f4b4147f402253e043ff0cd1050bda4bdf1e029b")
    fun searchGituser(@Query("q") username: String?): Call<SearchGituser>

    @GET("users")
    @Headers("Authorization: token f4b4147f402253e043ff0cd1050bda4bdf1e029b")
    fun getGituser(): Call<List<ListGituser>>

    @GET("users/{username}")
    @Headers("Authorization: token f4b4147f402253e043ff0cd1050bda4bdf1e029b")
    fun getGituserDetail(@Path("username") path: String): Call<DetailGituser>

    @GET("users/{username}/followers")
    @Headers("Authorization: token f4b4147f402253e043ff0cd1050bda4bdf1e029b")
    fun getGituserFollowers(@Path("username") path: String): Call<List<ListGituser>>

    @GET("users/{username}/following")
    @Headers("Authorization: token f4b4147f402253e043ff0cd1050bda4bdf1e029b")
    fun getGituserFollowing(@Path("username") path: String): Call<List<ListGituser>>
}
