package com.example.githubapiapp.data.remote.retrofit

import com.example.githubapiapp.data.remote.response.GithubResponse
import com.example.githubapiapp.data.remote.response.GithubResponseDetail
import com.example.githubapiapp.data.remote.response.ItemsItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    suspend fun getUser(
        @Query("q") username: String,
    ): GithubResponse

    @GET("users/{username}")
    suspend fun getDetailUser(
        @Path("username") username: String,
    ): GithubResponseDetail

    @GET("users/{username}/followers")
    suspend fun getFollowers(
        @Path("username") username: String,
    ): List<ItemsItem>

    @GET("users/{username}/following")
    suspend fun getFollowing(
        @Path("username") username: String,
    ): List<ItemsItem>

}