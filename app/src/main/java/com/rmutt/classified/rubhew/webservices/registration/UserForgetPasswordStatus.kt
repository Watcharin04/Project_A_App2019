package com.rmutt.classified.rubhew.webservices.registration

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserForgetPasswordStatus {
    @SerializedName("status")
    @Expose
    var status: String? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
}