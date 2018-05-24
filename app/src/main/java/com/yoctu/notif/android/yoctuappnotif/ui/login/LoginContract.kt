package com.yoctu.notif.android.yoctuappnotif.ui.login

import com.yoctu.notif.android.yoctuappnotif.BasePresenter
import com.yoctu.notif.android.yoctuappnotif.BaseView
import com.yoctu.notif.android.yoctulibrary.models.Channel
import com.yoctu.notif.android.yoctulibrary.models.Notification
import com.yoctu.notif.android.yoctulibrary.models.User
import com.yoctu.notif.android.yoctulibrary.models.ViewType

/**
 * Created on 26.03.18.
 */

interface LoginContract {

    interface View : BaseView<Presenter>{
        fun hideProgressBar()
        fun googleSignIn()
    }
    interface Presenter : BasePresenter<View>{
        fun saveUserInLocal(user: User)
        fun sendDeviceId()
        fun getUser(): User?
        fun gotoNotifications()
        fun changeToppics(): Boolean
        fun setChangeToppics(newValue: Boolean)
        fun saveMessage(notification: Notification)
    }
}