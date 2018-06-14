package com.yoctu.notif.android.yoctuappnotif

/**
 * Created on 14.06.18.
 */

interface GlobalPresenterContract {

    interface View: BaseView<Presenter> {}
    interface Presenter: BasePresenter<View> {

        fun jobSchedulerIsSet(): Boolean
        fun broadcastReceiverIsSet(): Boolean
        fun setJobSCheduler(value: Boolean)
        fun setBroadcastReceiver(value: Boolean)
    }
}