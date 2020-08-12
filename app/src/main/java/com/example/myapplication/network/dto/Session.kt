package com.example.myapplication.network.dto

import com.google.gson.annotations.SerializedName

data class UserSession(
    @SerializedName("usu_codigo")
    var codigo: Int,

    @SerializedName("usu_user_name")
    var username: String
)

data class SessionPost(
    @SerializedName("username")
    var username: String,

    @SerializedName("password")
    var password: String
)

data class SessionResponse(
    @SerializedName("user")
    var user: UserSession,

    @SerializedName("token")
    var token: String
)