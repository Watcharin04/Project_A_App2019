package com.rmutt.classified.rubhew.webservices.chat

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GroupChatModel {

    @SerializedName("session_user_id")
    @Expose
    var sessionUserId: String? = null
    @SerializedName("session_username")
    @Expose
    var sessionUsername: String? = null
    @SerializedName("session_user_image")
    @Expose
    var sessionUserImage: String? = null
    @SerializedName("from_user_id")
    @Expose
    var fromUserId: String? = null
    @SerializedName("from_username")
    @Expose
    var fromUsername: String? = null
    @SerializedName("from_user_image")
    @Expose
    var fromUserImage: String? = null
    @SerializedName("from_fullname")
    @Expose
    var fromFullname: String? = null
    @SerializedName("unseen")
    @Expose
    var unseen: Int? = null
    @SerializedName("status")
    @Expose
    var status: String? = null

}