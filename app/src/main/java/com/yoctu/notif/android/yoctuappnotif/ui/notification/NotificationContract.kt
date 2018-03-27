package com.yoctu.notif.android.yoctuappnotif.ui.notification

import com.yoctu.notif.android.yoctuappnotif.BasePresenter
import com.yoctu.notif.android.yoctuappnotif.BaseView

/**
 * Created on 27.03.18.
 */

interface NotificationContract {

    interface View : BaseView<Presenter> {}
    interface Presenter : BasePresenter<View> {}
}