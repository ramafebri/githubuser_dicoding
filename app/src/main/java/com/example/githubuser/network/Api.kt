package com.example.githubuser.network

import com.example.githubuser.BuildConfig
import com.example.githubuser.model.DetailGituser
import com.example.githubuser.model.ListGituser
import com.example.githubuser.model.SearchGituser
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

const val apiKey = BuildConfig.API_KEY
interface Api {
    @GET("search/users")
    @Headers("Authorization: token $apiKey")
    fun searchGituser(@Query("q") username: String?): Call<SearchGituser>

    @GET("users")
    @Headers("Authorization: token $apiKey")
    fun getGituser(): Call<List<ListGituser>>

    @GET("users/{username}")
    @Headers("Authorization: token $apiKey")
    fun getGituserDetail(@Path("username") path: String): Call<DetailGituser>

    @GET("users/{username}/followers")
    @Headers("Authorization: token $apiKey")
    fun getGituserFollowers(@Path("username") path: String): Call<List<ListGituser>>

    @GET("users/{username}/following")
    @Headers("Authorization: token $apiKey")
    fun getGituserFollowing(@Path("username") path: String): Call<List<ListGituser>>
}
