package com.yoctu.notif.android.yoctuappnotif.ui.adapters

import android.content.Context
import android.support.v4.util.SparseArrayCompat
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.yoctu.notif.android.yoctuappnotif.ui.login.adapter.ChannelsListDelegate
import com.yoctu.notif.android.yoctuappnotif.ui.notification.adapter.AdapterNotifDelegeate
import com.yoctu.notif.android.yoctulibrary.models.ConstantsViewType
import com.yoctu.notif.android.yoctulibrary.models.ViewType

/**
 * Created on 26.03.18.
 */
class YoctuAdapter(context : Context): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mContext : Context
    private var items = ArrayList<ViewType>()
    private var delegateAdapters = SparseArrayCompat<ViewTypeDelegateAdapter>()
    private var adapterChannelListDelegate : ChannelsListDelegate
    init {
        mContext = context

        adapterChannelListDelegate = ChannelsListDelegate(mContext)

        delegateAdapters.put(ConstantsViewType.VIEW_TYPE_CHANNEL,adapterChannelListDelegate)
        delegateAdapters.put(ConstantsViewType.VIEW_TYPE_NOTIFICATION,AdapterNotifDelegeate(mContext))
    }


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        return delegateAdapters.get(viewType).onCreateViewHolder(parent!!)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        delegateAdapters.get(getItemViewType(position)).onBindViewHolder(holder!!,items[position],position)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].getViewType()
    }

    /**
     * This function allows to add several @ViewType
     *
     * @param newItems the callbackReqPerm types
     */
    fun addItems(newItems : ArrayList<ViewType>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    /**
     * @return list of chosen channels
     */
    fun getChosenChannels() = adapterChannelListDelegate.getChosenChannels()

}