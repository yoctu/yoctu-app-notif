package com.yoctu.notif.android.yoctuappnotif.ui.topic

import com.yoctu.notif.android.yoctuappnotif.BasePresenter
import com.yoctu.notif.android.yoctuappnotif.BaseView
import com.yoctu.notif.android.yoctulibrary.models.ViewType

/**
 * Created by gael on 16.05.18.
 */

interface TopicContract {

    interface View: BaseView<Presenter> {
        fun getChannels(list : ArrayList<ViewType>)
    }
    interface Presenter: BasePresenter<View> {
        fun askChannels()
        fun saveChannels(chosen : ArrayList<ViewType>)
        fun gotoNotifications()
        fun getTopics(): ArrayList<ViewType>?
    }
}