package com.rmutt.classified.rubhew.webservices.notificationmessage

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class NotificationDataModel {

    @SerializedName("sender_id")
    @Expose
    var senderId: String? = null
    @SerializedName("sender_name")
    @Expose
    var senderName: String? = null
    @SerializedName("owner_id")
    @Expose
    var ownerId: String? = null
    @SerializedName("owner_name")
    @Expose
    var ownerName: String? = null
    @SerializedName("product_id")
    @Expose
    var productId: String? = null
    @SerializedName("product_title")
    @Expose
    var productTitle: String? = null
    @SerializedName("type")
    @Expose
    var type: String? = null
    @SerializedName("message")
    @Expose
    var message: String? = null

}