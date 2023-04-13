package com.example.justmeet.Models

import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory


data class User(
    var idUser : Int,
    var name : String,
    var password : String,
    var email : String,
    var birthday : String,
    var genre : String,
    var photo : Int,
    var description : String,
    var premium : Boolean,
    var settings : Setting,
    var location: Location
)

data class Location(
    var idLocation : Int,
    var longitud : Double,
    var latitud : Double
)

data class UserGame(
    var idGame : Game,
    var idUser: Int
)

data class Game(
    var idGame: Int,
    var registrationDate : String,
    var match : Boolean,
    var usuaris : List<User>,
    var questions : List<Question>
)

data class QuestionGame(
    var idGame: Int,
    var idQuestion : Int
)

data class Setting(
    var idSetting: Int,
    var maxDistance : Int,
    var minAge : Int,
    var maxAge : Int,
    var genre : String,
    var gameType : GameType
)

data class GameType(
    var idGameType: Int,
    var type : String
)

data class UserAnswer(
    var game: Game,
    var user: User,
    var question: Question,
    var answer: Answer
)

data class Answer(
    var idAnswer: Int,
    var answer1 : String
)



data class Question(
    var idQuestion: Int,
    var question1 : String,
    var idGameType: Int,
    var gameType : GameType,
    var answers : List<Answer>
)

lateinit var llistaUsers: ArrayList<User>

lateinit var tmf: TrustManagerFactory
lateinit var sslContext: SSLContext
lateinit var algorithm: String