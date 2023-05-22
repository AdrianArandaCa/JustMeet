package com.example.justmeet.Models

import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import kotlin.collections.ArrayList

var listQuestion: ArrayList<Question> = arrayListOf()
var listAnswer: ArrayList<Answer> = arrayListOf()

data class User(
    var idUser: Int?,
    var name: String?,
    var password: String?,
    var email: String?,
    var birthday: Int?,
    var genre: String?,
    var photo: String?,
    var description: String?,
    var premium: Boolean?,
    var idSetting: Int?,
    var idSettingNavigation: Setting?,
    var location: Location?,
    var isConnected : Boolean?
) : java.io.Serializable

data class Location(
    var idLocation: Int?,
    var longitud: Double?,
    var latitud: Double?,
    var idUser: Int?
)

data class UserGame(
    var idGame: Game,
    var idUser: Int
)

data class Game(
    var idGame: Int?,
    var registrationDate: String?,
    var match: Boolean?,
    var percentage: Float?
//    var usuaris : List<User>,
//    var questions : List<Question>
)

data class QuestionGame(
    var idGame: Int,
    var idQuestion: Int
)

data class Setting(
    var idSetting: Int?,
    var maxDistance: Int,
    var minAge: Int,
    var maxAge: Int,
    var genre: String,
    var idGametype: Int?,
    var idGameTypeNavigation: GameType?
)

data class GameType(
    var idGameType: Int?,
    var type: String
)

data class UserAnswer(
    var idGame: Int,
    var idUser: Int,
    var idQuestion: Int,
    var idAnswer: Int?
)

data class Answer(
    var idAnswer: Int,
    var answer1: String,
    var selected: Int
)


data class Question(
    var idQuestion: Int,
    var question1: String,
    var idGameType: Int,
    var gameType: GameType,
    var answers: ArrayList<Answer>
)

data class Avatar(
    var idFoto: Int,
    var resourcePhoto: String,
    var nomFoto: String,
    var selected: Int
)

data class Chat(
    var message: String,
    var isUserLogged: Int
)

data class Advertisement(
    var idAdvertisement: Int,
    var nameCompany: String,
    var url: String,
    var website: String,
    var logo: Int
)

lateinit var llistaUsers: ArrayList<User>
lateinit var tmf: TrustManagerFactory
lateinit var sslContext: SSLContext
lateinit var algorithm: String
var userLog: User? = User(null, null, null, null, null, null, null, null, null, null, null, null,false)
var listQuestionAux: ArrayList<Question> = arrayListOf()
lateinit var gameFromSocket: Game
var gameFinishFromSocket = Game(null, null, null, null)

//var userMatch = User(null,null,null,null,null,null,null,null,null,null,null,null)
var listUserMatches: ArrayList<User> = arrayListOf()
var listChatUsers: ArrayList<Chat> = arrayListOf()
var userGameLeave: Boolean = false
var isSucces: Boolean = false
var isConnectedChat : Boolean = false
var timeForGame : Long = 11000
var isDebug : Boolean = false


