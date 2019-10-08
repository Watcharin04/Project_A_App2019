package com.rmutt.classified.rubhew.webservices.registration

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserRegistrationData {
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("username")
    @Expose
    var username: String? = null
    @SerializedName("email")
    @Expose
    var email: String? = null
    @SerializedName("password")
    @Expose
    var password: String? = null

    var fbLogin: String? = null
}