package com.yoctu.notif.android.yoctuappnotif.ui.notification

import com.yoctu.notif.android.yoctuappnotif.BasePresenter
import com.yoctu.notif.android.yoctuappnotif.BaseView
import com.yoctu.notif.android.yoctulibrary.models.Notification
import com.yoctu.notif.android.yoctulibrary.models.ViewType

/**
 * Created on 27.03.18.
 */

interface NotificationContract {

    interface View : BaseView<Presenter> {
        fun populateRecyclerView()
        fun notLogged()
    }
    interface Presenter : BasePresenter<View> {
        fun getMessages(): ArrayList<ViewType>
        fun saveMessage(notification: Notification)
        fun googleSignOut()
        fun redirectToChannels()
        fun redirectToManageTopicURL()
        fun logged()
    }
}