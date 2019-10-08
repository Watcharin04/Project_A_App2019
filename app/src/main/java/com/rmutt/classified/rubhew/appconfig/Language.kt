package com.rmutt.classified.rubhew.appconfig

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Language {

    @SerializedName("id")
    @Expose
    var id: String? = null
    @SerializedName("code")
    @Expose
    var code: String? = null
    @SerializedName("direction")
    @Expose
    var direction: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("file_name")
    @Expose
    var fileName: String? = null
    @SerializedName("active")
    @Expose
    var active: String? = null
    @SerializedName("default")
    @Expose
    var default: String? = null

}