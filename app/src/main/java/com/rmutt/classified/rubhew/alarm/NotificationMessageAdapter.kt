package com.rmutt.classified.rubhew.alarm

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.rmutt.classified.rubhew.R
import com.rmutt.classified.rubhew.utils.ColorCircleDrawable
import com.rmutt.classified.rubhew.webservices.notificationmessage.NotificationDataModel
import kotlinx.android.synthetic.main.notification_message_adapter.view.*

class NotificationMessageAdapter(val mNotificationMessageListModel: List<NotificationDataModel>): RecyclerView.Adapter<NotificationMessageAdapter.ChatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.notification_message_adapter, parent, false)
        return ChatViewHolder(view)
    }

    override fun getItemCount(): Int {
        if (mNotificationMessageListModel != null) return mNotificationMessageListModel.size else return 0
    }

    override fun onBindViewHolder(chatViewHolder: ChatViewHolder, position: Int) {
        val notificationMessageModel = mNotificationMessageListModel.get(position)
        chatViewHolder.notificationMessage.text = notificationMessageModel.message
        chatViewHolder.notificationMessageSenderName.text = notificationMessageModel.senderName
        if (!notificationMessageModel.senderName.isNullOrEmpty()) {
            chatViewHolder.notificationMessageSenderNameFirstLetter.text = notificationMessageModel.senderName!!.toCharArray()[0].toString().toUpperCase()
            chatViewHolder.notificationMessageSenderNameFirstLetter.background = ColorCircleDrawable(chatViewHolder.notificationMessageSenderNameFirstLetter.context.resources.getColor(R.color.android_def_cursor_color))
        }
    }

    class ChatViewHolder(view: View): RecyclerView.ViewHolder(view) {
        internal var notificationMessage = view.notification_message_text_view
        internal var notificationMessageSenderName = view.notification_sender_name
        internal var notificationMessageSenderNameFirstLetter = view.notification_sender_name_circular_first_name

    }
}