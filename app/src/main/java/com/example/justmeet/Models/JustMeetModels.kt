package com.example.justmeet.Models


import java.sql.Date
import java.time.LocalDate
import java.util.*
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
    var photo: Int?,
    var description: String?,
    var premium: Boolean?,
    var idSetting: Int?,
    var idSettingNavigation: Setting?,
    var location: Location?
)

data class Location(
    var idLocation : Int?,
    var longitud : Double,
    var latitud : Double
)

data class UserGame(
    var idGame : Game,
    var idUser: Int
)

data class Game(
    var idGame: Int?,
    var registrationDate : String?,
    var match : Boolean?,
    var percentage : Float?
//    var usuaris : List<User>,
//    var questions : List<Question>
)

data class QuestionGame(
    var idGame: Int,
    var idQuestion : Int
)

data class Setting(
    var idSetting: Int?,
    var maxDistance : Int,
    var minAge : Int,
    var maxAge : Int,
    var genre : String,
    var idGameTypeNavigation : GameType?
)

data class GameType(
    var idGameType: Int?,
    var type : String
)

data class UserAnswer(
    var idGame: Int,
    var idUser: Int,
    var idQuestion: Int,
    var idAnswer: Int?
)

data class Answer(
    var idAnswer: Int,
    var answer1 : String,
    var selected : Int
)



data class Question(
    var idQuestion: Int,
    var question1 : String,
    var idGameType: Int,
    var gameType : GameType,
    var answers : ArrayList<Answer>
)

lateinit var llistaUsers: ArrayList<User>

lateinit var tmf: TrustManagerFactory
lateinit var sslContext: SSLContext
lateinit var algorithm: String
 var userLog = User(null,null,null,null,null,null,null,null,null,null,null,null)
var listQuestionAux: ArrayList<Question> = arrayListOf()
lateinit var gameFromSocket : Game
 var gameFinishFromSocket = Game(null,null,null,null)
//var userMatch = User(null,null,null,null,null,null,null,null,null,null,null,null)
var listUserMatches : ArrayList<User> = arrayListOf()


