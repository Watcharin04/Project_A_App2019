package com.rmutt.classified.rubhew.webservices.settings

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CityListModel {

    @SerializedName("id")
    @Expose
    var id: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("latitude")
    @Expose
    var lat: String? = null
    @SerializedName("longitude")
    @Expose
    var long: String? = null

}