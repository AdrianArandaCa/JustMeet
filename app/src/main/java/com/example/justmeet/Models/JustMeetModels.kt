package com.example.justmeet.Models


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
    var idSetting : Int
)

data class Location(
    var idLocation : Int,
    var longitud : Double,
    var latitud : Double,
    var idUser : Int
)

data class UserGame(
    var idGame : Int,
    var idUser: Int
)

data class Game(
    var idGame: Int,
    var registrationDate : String,
    var match : Boolean
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
    var idGameType : Int
)

data class GameType(
    var idGameType: Int,
    var type : String
)

data class UserAnswer(
    var idGame: Int,
    var idUser: Int,
    var idQuestion: Int,
    var idAnswer: Int
)

data class Answer(
    var idAnswer: Int,
    var answer : String
)

data class QuestionAnswer(
    var idQuestion: Int,
    var idAnswer: Int
)

data class Question(
    var idQuestion: Int,
    var question : String,
    var idGameType: Int
)
