package com.rmutt.classified.rubhew.dashboard

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CustomData {

    @SerializedName("type")
    @Expose
    var type: String? = null
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("value")
    @Expose
    var value: String? = null

}