package com.yoctu.notif.android.yoctuappnotif.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.yoctu.notif.android.yoctulibrary.models.ViewType

/**
 * This interface represents the adapters in Delegate Adapter
 *
 * Created on 26.03.18.
 */
interface ViewTypeDelegateAdapter {

    fun onCreateViewHolder(parent : ViewGroup) : RecyclerView.ViewHolder
    fun onBindViewHolder (holder : RecyclerView.ViewHolder, item : ViewType, position : Int)
}
