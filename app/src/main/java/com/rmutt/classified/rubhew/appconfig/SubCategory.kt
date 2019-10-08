package com.rmutt.classified.rubhew.appconfig

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SubCategory {

    @SerializedName("id")
    @Expose
    var id: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("picture")
    @Expose
    var picture: String? = null

}