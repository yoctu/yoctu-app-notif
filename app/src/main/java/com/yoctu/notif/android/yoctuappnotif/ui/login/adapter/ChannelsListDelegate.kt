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
import com.yoctu.notif.android.yoctuappnotif.utils.YoctuUtils
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
    private var currentChannels: ArrayList<String>? = null//= ArrayList<String>()
    private var typeNames = 1
    private var typeChoice = 2

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val repository : YoctuRepository = YoctuApplication.kodein.with(mContext).instance()
        currentChannels = repository.getListToppicsName()
        return ChannelViewHolder(LayoutInflater.from(mContext).inflate(R.layout.login_item_channel,parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType, position: Int) {
        holder as ChannelViewHolder
        item as Channel

        if(item.checked)
            holder.viewCheck.setImageResource(R.drawable.ic_checked)
        else
            holder.viewCheck.setImageResource(R.drawable.ic_not_checked)
        //check from shared preferences
        if(currentChannels != null && currentChannels!!.size > 0)
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
            remove(currentItem)
            //Log.d(YoctuUtils.TAG_DEBUG, "new sizes are : ".plus(chosenChannels.size).plus(" ").plus(currentChannels))
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
        if (currentChannels != null && currentChannels!!.contains(currentItem.name)) {
            currentItem.checked = true
            currentViewHolder.viewCheck.setImageResource(R.drawable.ic_checked)
            chosenChannels.add(currentItem)
        }
    }

    /**
     * Remove channels in @currentChannels and @chosenChannels
     * @param currentItem
     */
    private fun remove(currentItem : Channel) {
        removeChannels(currentItem,typeChoice)
        removeChannels(currentItem,typeNames)
    }

    /**
     * Remove channel
     * type : names
     *        choice
     *
     * @param currentItem
     * @param type
     */
    private fun removeChannels(currentItem : Channel, type: Int) {
        var i = 0
        var found = false
        var saveInd = i
        var size = chosenChannels.size
        if(type == typeNames && currentChannels != null)
            size = currentChannels!!.size

        while(i < size && !found) {
            saveInd = i
            when(type) {
                typeNames -> {
                    found =  currentChannels != null && currentChannels!![i].equals(currentItem.name)
                }
                typeChoice -> {
                    found = (chosenChannels[i] as Channel).name.equals(currentItem.name)
                }
            }
            i++
        }
        if (found) {
            when(type) {
                typeNames -> currentChannels?.let { currentChannels!!.removeAt(saveInd) }
                typeChoice -> chosenChannels.removeAt(saveInd)
            }
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