package com.yoctu.notif.android.yoctuappnotif.ui.login.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.with
import com.yoctu.notif.android.yoctuappnotif.R
import com.yoctu.notif.android.yoctuappnotif.YoctuApplication
import com.yoctu.notif.android.yoctuappnotif.repository.YoctuRepository
import com.yoctu.notif.android.yoctuappnotif.ui.adapters.ViewTypeDelegateAdapter
import com.yoctu.notif.android.yoctulibrary.models.Channel
import com.yoctu.notif.android.yoctulibrary.models.ViewType
import java.util.ArrayList

/**
 * Created on 26.03.18.
 */

class ChannelsListDelegate(context: Context): ViewTypeDelegateAdapter {
    private var mContext : Context
    init {
        mContext = context
    }
    // user's choices
    private var chosenChannels = ArrayList<ViewType>()
    // list from shared preferences
    private var currentChannels = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val repository : YoctuRepository = YoctuApplication.kodein.with(mContext).instance()
        currentChannels = repository.getListToppicsName()!!
        return ChannelViewHolder(LayoutInflater.from(mContext).inflate(R.layout.login_item_channel,parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType, position: Int) {
        holder as ChannelViewHolder
        item as Channel

        if(item.checked)
            holder.viewCheck.setImageResource(R.drawable.ic_checked)
        else
            holder.viewCheck.setImageResource(R.drawable.ic_not_checked)
        //check from shread preferences
        if(currentChannels != null && currentChannels.size > 0)
            manageItems(item,holder)

        holder.textChannel.text = item.name
        holder.mainContent.setOnClickListener { _ ->
            updateChosenCHannels(item,holder)
        }
    }

    /**
     * Manage the tap event for un channel
     *
     * @param currentItem
     * @param currentViewHolder
     */
    private fun updateChosenCHannels(currentItem : Channel, currentViewHolder : ChannelViewHolder) {
        if(currentItem.checked){
            currentItem.checked = false
            currentViewHolder.viewCheck.setImageResource(R.drawable.ic_not_checked)
            val resp = chosenChannels.remove(currentItem)
            //Log.d("debug",currentItem.name.plus(" is removed ? ").plus(resp).plus(" - ").plus(chosenChannels.size))
        } else {
            currentItem.checked = true
            currentViewHolder.viewCheck.setImageResource(R.drawable.ic_checked)
            chosenChannels.add(currentItem)
            //Log.d("debug",currentItem.name.plus(" added ").plus(chosenChannels.size))
        }
    }

    fun getChosenChannels() = chosenChannels

    /**
     * Set item's checked to good value from channels into shared preferences
     * This method is important because each time that user goes to view, the list is reload !
     * save channel in @chosenChannels
     *
     * @param currentItem
     * @param currentViewHolder
     */
    private fun manageItems(currentItem : Channel, currentViewHolder : ChannelViewHolder) {
        if (currentChannels != null && currentChannels.contains(currentItem.name)) {
            currentViewHolder.viewCheck.setImageResource(R.drawable.ic_checked)
            chosenChannels.add(currentItem)
        }
    }

    class ChannelViewHolder (view : View): RecyclerView.ViewHolder(view) {
        var viewCheck : ImageView
        var textChannel : TextView
        var mainContent : LinearLayout
        init {
            viewCheck = view.findViewById(R.id.dialog_item_check)
            textChannel = view.findViewById(R.id.dialog_item_channel)
            mainContent = view.findViewById(R.id.dialog_item_main_linear_layout)
        }
    }
}