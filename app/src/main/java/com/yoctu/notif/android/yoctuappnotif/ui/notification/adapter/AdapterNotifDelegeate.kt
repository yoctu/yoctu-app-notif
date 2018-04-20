package com.yoctu.notif.android.yoctuappnotif.ui.notification.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.yoctu.notif.android.yoctuappnotif.R
import com.yoctu.notif.android.yoctuappnotif.ui.adapters.ViewTypeDelegateAdapter
import com.yoctu.notif.android.yoctuappnotif.ui.login.adapter.ChannelsListDelegate
import com.yoctu.notif.android.yoctulibrary.models.Notification
import com.yoctu.notif.android.yoctulibrary.models.ViewType

/**
 * Created on 28.03.18.
 */
class AdapterNotifDelegeate (context: Context): ViewTypeDelegateAdapter {
    private var mContext : Context
    init {
        mContext = context
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return NotificationViewHolder(LayoutInflater.from(mContext).inflate(R.layout.notification_item_message, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType, position: Int) {
        holder as NotificationViewHolder
        item as Notification

        if (item.topic.isEmpty()) {
            holder.layoutTopic.visibility = View.GONE
        } else {
            holder.viewTopic.text = item.topic
            holder.layoutTopic.visibility = View.VISIBLE
        }
        holder.viewTime.text = item.formatTime()
        holder.viewTitle.text = item.title
        holder.viewText.text = item.body
    }

    class NotificationViewHolder (view : View): RecyclerView.ViewHolder(view) {
        var viewTime : TextView
        var viewTitle : TextView
        var viewText : TextView
        var viewTopic: TextView
        var layoutTopic: LinearLayout
        init {
            viewTime = view.findViewById(R.id.notification_item_time)
            viewTitle = view.findViewById(R.id.notification_item_title)
            viewText = view.findViewById(R.id.notification_item_text)
            viewTopic = view.findViewById(R.id.notification_item_topic)
            layoutTopic = view.findViewById(R.id.notification_item_layout_topic)
        }
    }
}