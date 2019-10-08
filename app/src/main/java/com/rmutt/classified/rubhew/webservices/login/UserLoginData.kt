package com.rmutt.classified.rubhew.webservices.login

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserLoginData {
    @SerializedName("username")
    @Expose
    var username: String? = null
    @SerializedName("email")
    @Expose
    var email: String? = null
    @SerializedName("password")
    @Expose
    var password: String? = null
}