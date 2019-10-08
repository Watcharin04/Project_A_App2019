package com.rmutt.classified.rubhew.webservices.settings

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CountryListModel {

    @SerializedName("id")
    @Expose
    var id: String? = null
    @SerializedName("code")
    @Expose
    var code: String? = null
    @SerializedName("lowercase_code")
    @Expose
    var lowercaseCode: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("lang")
    @Expose
    var lang: String? = null

}