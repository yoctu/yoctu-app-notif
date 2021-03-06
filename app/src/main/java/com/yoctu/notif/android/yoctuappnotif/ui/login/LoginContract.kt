package com.yoctu.notif.android.yoctuappnotif.ui.login

import com.yoctu.notif.android.yoctuappnotif.BasePresenter
import com.yoctu.notif.android.yoctuappnotif.BaseView
import com.yoctu.notif.android.yoctulibrary.models.Channel
import com.yoctu.notif.android.yoctulibrary.models.ViewType

/**
 * Created on 26.03.18.
 */

interface LoginContract {

    interface View : BaseView<Presenter>{
        fun hideProgressBar()
        fun getChannels(list : ArrayList<ViewType>)
    }
    interface Presenter : BasePresenter<View>{
        fun askChannels()
        fun saveChannels(chosen : ArrayList<ViewType>)
    }
}