package com.rmutt.classified.rubhew.webservices.productlist

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ProductsData {
    @SerializedName("id")
    @Expose
    var id: String? = null
    @SerializedName("product_name")
    @Expose
    var productName: String? = null
    @SerializedName("cat_id")
    @Expose
    var catId: String? = null
    @SerializedName("sub_cat_id")
    @Expose
    var subCatId: String? = null
    @SerializedName("featured")
    @Expose
    var featured: String? = null
    @SerializedName("urgent")
    @Expose
    var urgent: String? = null
    @SerializedName("highlight")
    @Expose
    var highlight: String? = null
    @SerializedName("highlight_bgClr")
    @Expose
    var highlightBgClr: String? = null
    @SerializedName("location")
    @Expose
    var location: String? = null
    @SerializedName("city")
    @Expose
    var city: String? = null
    @SerializedName("status")
    @Expose
    var status: String? = null
    @SerializedName("hide")
    @Expose
    var hide: String? = null
    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null
    @SerializedName("expire_date")
    @Expose
    var expireDate: String? = null
    @SerializedName("category")
    @Expose
    var category: String? = null
    @SerializedName("sub_category")
    @Expose
    var subCategory: String? = null
    @SerializedName("favorite")
    @Expose
    var favorite: Boolean? = null
    @SerializedName("showtag")
    @Expose
    var showtag: String? = null
    @SerializedName("tag")
    @Expose
    var tag: String? = null
    @SerializedName("pic_count")
    @Expose
    var picCount: Int? = null
    @SerializedName("picture")
    @Expose
    var picture: String? = null
    @SerializedName("price")
    @Expose
    var price: String? = null
    @SerializedName("currency")
    @Expose
    var currency: String? = null
    @SerializedName("currency_in_left")
    @Expose
    var currencyInLeft: String? = null
    @SerializedName("username")
    @Expose
    var username: String? = null
    @SerializedName("subcription_title")
    @Expose
    var subcriptionTitle: String? = null
    @SerializedName("subcription_image")
    @Expose
    var subcriptionImage: String? = null
    var itemType = 1
}
