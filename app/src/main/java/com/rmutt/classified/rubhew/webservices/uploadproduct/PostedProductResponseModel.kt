package com.rmutt.classified.rubhew.webservices.uploadproduct

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PostedProductResponseModel {
    @SerializedName("status")
    @Expose
    var status: String? = null
    @SerializedName("id")
    @Expose
    var id: String? = null
}