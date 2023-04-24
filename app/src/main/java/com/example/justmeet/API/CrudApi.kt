package com.example.justmeet.API

import com.example.justmeet.Models.User
import com.example.justmeet.Models.UserAnswer
import com.example.justmeet.Models.sslContext
import com.example.justmeet.Models.tmf
import com.example.justmeet.R
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.InputStream
import java.security.cert.Certificate
import java.security.cert.CertificateFactory
import javax.net.ssl.X509TrustManager
import kotlin.coroutines.CoroutineContext

class CrudApi(): CoroutineScope {

    val urlapi = "https://172.16.24.123:45455/api/"
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
        val call = getRetrofit().create(APIService::class.java).insertUsuari(usuari)
        return call.isSuccessful
    }

    suspend fun modifyUserFromApi(user: User): Boolean {
        val call = getRetrofit().create(APIService::class.java).updateUser(user.idUser ?: 0, user)
        return call.isSuccessful
    }

    suspend fun deleteUserFromAPI(codi: Int): Boolean {
        val call = getRetrofit().create(APIService::class.java).deleteUser(codi)
        return call.isSuccessful
    }
    suspend fun getOneUser(codi: Int): User {
        val call = getRetrofit().create(APIService::class.java).getOneUser(codi)
        return call!!
    }
    suspend fun getOneUserByName(name: String): User {
        val call = getRetrofit().create(APIService::class.java).getOneUserByName(name)
        return call!!
    }

    // UserAnswersRequets
    suspend fun addUserAnswerToAPI(listUserAnswer: List<UserAnswer>): Boolean {
        val call = getRetrofit().create(APIService::class.java).insertUserAnswerList(listUserAnswer)
        println(call.message())
        return call.isSuccessful
    }

    //UserMatchesRequets
    suspend fun getUsersMatches(idUser: Int): List<User> {

        return getRetrofit().create(APIService::class.java).getUsersMatch(idUser)
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job


}

