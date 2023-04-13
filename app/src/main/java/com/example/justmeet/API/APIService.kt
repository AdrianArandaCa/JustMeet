package com.example.justmeet.API

import com.example.justmeet.Models.User
import retrofit2.Response
import retrofit2.http.*

interface APIService {

    @GET("users")
    suspend fun getUsuaris(): Response<ArrayList<User>>

    @GET("user/{id}")
    suspend fun getOneUser(@Path("id") id: Int): User

    @POST("user")
    suspend fun insertUsuari(@Body usuari: User): Response<User>

    @PUT("/usuaris/")
    suspend fun updateUser(@Body usuari: User): Response<User>

    @DELETE("/usuaris/")
    suspend fun deleteUser(@Query("codi") codi: Int): Response<User>
}