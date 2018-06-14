package com.yoctu.notif.android.yoctuappnotif

/**
 * Created by gael on 14.06.18.
 */

class GlobalPresenter: GlobalPresenterContract.Presenter {

    private var jobSchedulerIsSet = false
    private var broadcastReceiverIsSet = false

    override fun takeView(view: GlobalPresenterContract.View) {}
    override fun dropView() {}

    override fun jobSchedulerIsSet() = jobSchedulerIsSet

    override fun broadcastReceiverIsSet() = broadcastReceiverIsSet


    override fun setJobSCheduler(value: Boolean) {
       jobSchedulerIsSet = value
    }

    override fun setBroadcastReceiver(value: Boolean) {
        broadcastReceiverIsSet = value
    }

}