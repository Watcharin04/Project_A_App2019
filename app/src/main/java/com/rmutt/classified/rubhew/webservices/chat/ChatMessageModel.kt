package com.rmutt.classified.rubhew.webservices.chat

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ChatMessageModel {

    @SerializedName("sender_username")
    @Expose
    var senderUsername: String? = null
    @SerializedName("sender_id")
    @Expose
    var senderId: String? = null
    @SerializedName("client_username")
    @Expose
    var clientUsername: String? = null
    @SerializedName("sender_pic")
    @Expose
    var senderPic: String? = null
    @SerializedName("client_pic")
    @Expose
    var clientPic: String? = null
    @SerializedName("total_pages")
    @Expose
    var totalPages: Int? = null
    @SerializedName("page")
    @Expose
    var page: Any? = null
    @SerializedName("mtype")
    @Expose
    var mtype: String? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("time")
    @Expose
    var time: String? = null
    @SerializedName("seen")
    @Expose
    var seen: String? = null

}