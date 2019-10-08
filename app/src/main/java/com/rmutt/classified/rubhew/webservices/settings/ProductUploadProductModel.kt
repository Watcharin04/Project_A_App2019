package com.rmutt.classified.rubhew.webservices.settings

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ProductUploadProductModel {
    @SerializedName("status")
    @Expose
    var status: String? = null
    @SerializedName("url")
    @Expose
    var url: String? = null
}