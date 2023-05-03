package com.example.justmeet.API

import com.example.justmeet.Models.Location
import com.example.justmeet.Models.Setting
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
    suspend fun getOneUserByName(@Path("name") name: String): Response<User>

    @POST("user")
    suspend fun insertUsuari(@Body user: User): Response<User>

    @PUT("user/{id}")
    suspend fun updateUser(@Path("id") id: Int, @Body user: User): Response<User>

    @DELETE("user/{id}")
    suspend fun deleteUser(@Path("id") id: Int): Response<User>


    // UserAnswers requests
    @POST("listUserAnswer")
    suspend fun insertUserAnswerList(@Body usuari: List<UserAnswer>): Response<List<UserAnswer>>

    // UserMatches

    @GET("userGameList/{idUser}")
    suspend fun getUsersMatch(@Path("idUser") idUser : Int) : List<User>

    //Settings Request

    @GET("setting/{idSetting}")
    suspend fun getSettingById(@Path("idSetting") idSetting : Int) : Setting

    @PUT("setting/{id}")
    suspend fun updateSetting(@Path("id") id: Int, @Body user: Setting): Response<Setting>

    //Location Request

    @PUT("location/{id}")
    suspend fun putLocationOnUser(@Path("id") idUser : Int,@Body location: Location) : Response<Location>

    @GET("locations")
    suspend fun getAllLocations() :ArrayList<Location>

    @GET("locationByUser/{id}")
    suspend fun getLocationByUser(@Path("id") id : Int) : Location

    @POST("location")
    suspend fun insertLocation(@Body location: Location) : Response<Location>


}