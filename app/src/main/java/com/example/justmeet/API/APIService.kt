package com.example.justmeet.API

import com.example.justmeet.Models.User
import com.example.justmeet.Models.UserAnswer
import retrofit2.Response
import retrofit2.http.*

interface APIService {

    @GET("users")
    suspend fun getUsuaris(): Response<ArrayList<User>>

    @GET("user/{id}")
    suspend fun getOneUser(@Path("id") id: Int): User

    @GET("userByName/{name}")
    suspend fun getOneUserByName(@Path("name") name: String): User
    @POST("user")
    suspend fun insertUsuari(@Body usuari: User): Response<User>

    @PUT("/usuaris/")
    suspend fun updateUser(@Body usuari: User): Response<User>

    @DELETE("/usuaris/")
    suspend fun deleteUser(@Query("codi") codi: Int): Response<User>


    // UserAnswers requests
    @POST("listUserAnswer")
    suspend fun insertUserAnswerList(@Body usuari: List<UserAnswer>): Response<List<UserAnswer>>

}