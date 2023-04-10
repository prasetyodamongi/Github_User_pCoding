package com.pcoding.githubuser.api

import com.pcoding.githubuser.data.DetailResponse
import com.pcoding.githubuser.data.User
import com.pcoding.githubuser.data.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    @Headers("Authorization: token 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")
    fun getSearchUser(
        @Query("q") username: String
    ): Call<UserResponse>

    @GET("users/{username}")
    @Headers("Authorization: token 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")
    fun getDetailUser(
        @Path("username") username: String
    ): Call<DetailResponse>

    @GET("users/{username}/followers")
    @Headers("Authorization: token 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")
    fun getFollowers(
        @Path("username") username: String
    ): Call<ArrayList<User>>

    @GET("users/{username}/following")
    @Headers("Authorization: token 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")
    fun getFollowing(
        @Path("username") username: String
    ): Call<ArrayList<User>>
}