package com.example.justmeet.API

import android.util.Log
import com.example.justmeet.Models.*
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.net.ssl.X509TrustManager
import kotlin.coroutines.CoroutineContext

class CrudApi() : CoroutineScope {

    val urlapi = "https://172.16.24.24:45455/api/" // IP PC HDD
    //val urlapi = "https://172.16.24.123:45455/api/" // IP SERVER PC ADRI
//    val urlapi = "http://192.168.1.135/"
//    val urlapi = "http://172.19.254.119/"

    private var job: Job = Job()

    suspend fun getAllUsersFromAPI(): ArrayList<User> {

        val response = getRetrofit().create(APIService::class.java).getUsuaris().body()
        return response!!
    }

    private fun getRetrofit(): Retrofit {
        val gson = GsonBuilder()
            .setLenient()
            .create()

        return Retrofit.Builder().baseUrl(urlapi).client(getClient())
            .addConverterFactory(GsonConverterFactory.create(gson)).build()
    }

    private fun getClient(): OkHttpClient =
        OkHttpClient.Builder()
            .sslSocketFactory(sslContext.socketFactory, tmf.trustManagers[0] as X509TrustManager)
            .addInterceptor(HeaderInterceptor())
            .addInterceptor(logging)
            .build()

    suspend fun addUserToAPI(usuari: User): Boolean {
        try {
            if(isDebug){
                return false
            }
            val call = getRetrofit().create(APIService::class.java).insertUsuari(usuari)
            return call.isSuccessful
        } catch (e : Exception) {
            Log.d("Expection", "Web Service Tancat")
            return false
        }
    }

    suspend fun modifyUserFromApi(user: User): Boolean {
        try {
            if(isDebug){
                return false
            }
            val call = getRetrofit().create(APIService::class.java).updateUser(user.idUser!!, user)
            return call.isSuccessful
        } catch (e : Exception) {
            Log.d("Expection", "Web Service Tancat")
            return false
        }
    }

    suspend fun deleteUserFromAPI(codi: Int): Boolean {
        val call = getRetrofit().create(APIService::class.java).deleteUser(codi)
        return call.isSuccessful
    }

    suspend fun getOneUser(codi: Int): User? {
        try {
            val call = getRetrofit().create(APIService::class.java).getOneUser(codi)
            return call!!
        } catch (e : Exception) {
            Log.d("Expection", "Web Service Tancat")
            return null
        }
    }

    suspend fun getOneUserByName(name: String): User? {
        try {
            if(isDebug){
                return null
            }
            val call = getRetrofit().create(APIService::class.java).getOneUserByName(name)
            return if (call?.isSuccessful == true) call.body() else null
        } catch (e : Exception) {
            Log.d("Expection", "Web Service Tancat")
            return null
        }
    }

    //Settings Request

    suspend fun getSettingById(idSetting: Int): Setting? {
        try {
            if(isDebug){
                return null
            }
            val call = getRetrofit().create(APIService::class.java).getSettingById(idSetting)
            return call
        } catch (e : Exception) {
            Log.d("Expection", "Web Service Tancat")
            return null
        }
    }

    suspend fun modifySettingFromApi(set: Setting): Boolean {
        try {
            if(isDebug){
                return false
            }
            val call = getRetrofit().create(APIService::class.java).updateSetting(set.idSetting!!, set)
            return call.isSuccessful
        } catch (e : Exception) {
            Log.d("Expection", "Web Service Tancat")
            return false
        }
    }

    // UserAnswersRequets
    suspend fun addUserAnswerToAPI(listUserAnswer: List<UserAnswer>): Boolean {
        try {
            if(isDebug){
                return false
            }
            val call = getRetrofit().create(APIService::class.java).insertUserAnswerList(listUserAnswer)
            println(call.message())
            return call.isSuccessful
        } catch (e : Exception) {
            Log.d("Expection", "Web Service Tancat")
            return false
        }
    }

    //UserMatchesRequets
    suspend fun getUsersMatches(idUser: Int): List<User>? {
        try {
            if(isDebug){
                return null
            }
            val call = getRetrofit().create(APIService::class.java).getUsersMatch(idUser)
            return call
        } catch (e : Exception) {
            Log.d("Expection", "Web Service Tancat")
            return null
        }
    }

    //Location Requests

    suspend fun modifyLocationUser(location: Location): Boolean {
        try {
            if(isDebug){
                return false
            }
            val call = getRetrofit().create(APIService::class.java)
                .putLocationOnUser(location.idLocation!!, location)
            return call.isSuccessful
        } catch (e : Exception) {
            Log.d("Expection", "Web Service Tancat")
            return false
        }
    }

    suspend fun getAllLocationsFromAPI(): ArrayList<Location>? {
        try {
            if(isDebug){
                return null
            }
            val response = getRetrofit().create(APIService::class.java).getAllLocations()
            return response!!
        } catch (e : Exception) {
            Log.d("Expection", "Web Service Tancat")
            return null
        }
    }

    suspend fun getOneLocationByUserFromAPI(id: Int): Location? {

        try {
            if(isDebug){
                return null
            }
            val response = getRetrofit().create(APIService::class.java).getLocationByUser(id)
            return response!!
        } catch (e : Exception) {
            Log.d("Expection", "Web Service Tancat")
            return null
        }
    }

    suspend fun postLocation(location: Location): Boolean {
        try {
            if(isDebug){
                return false
            }
            val response = getRetrofit().create(APIService::class.java).insertLocation(location)
            return response.isSuccessful
        } catch (e : Exception) {
            Log.d("Expection", "Web Service Tancat")
            return false
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
}

