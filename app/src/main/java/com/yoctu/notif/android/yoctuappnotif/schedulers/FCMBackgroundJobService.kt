package com.yoctu.notif.android.yoctuappnotif.schedulers

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.with
import com.yoctu.notif.android.yoctuappnotif.GlobalPresenter
import com.yoctu.notif.android.yoctuappnotif.YoctuApplication
import com.yoctu.notif.android.yoctuappnotif.fcm.MyFirebaseMessagingService
import com.yoctu.notif.android.yoctuappnotif.repository.YoctuRepository
import com.yoctu.notif.android.yoctuappnotif.utils.YoctuUtils
import com.yoctu.notif.android.yoctulibrary.models.Notification
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * Manage the scheduled task(s)
 * Created by gael on 14.06.18.
 */

class FCMBackgroundJobService:
        JobService(),
        Observer<Any> {

    /**
     * Broadcast receiver to get intent
     */
    private var fcmReceiver: BroadcastReceiver? = null

    /**
     * The job starts
     */
    override fun onStartJob(params: JobParameters?): Boolean {
        Log.d("debug"," -- start the job !")
        initializeBroadcastReceievr()
        var intentFilter = IntentFilter()
        intentFilter.addAction("com.google.android.c2dm.intent.RECEIVE")
        registerReceiver(fcmReceiver, intentFilter)
        return true
    }

    /**
     * THe job finishes
     */
    override fun onStopJob(params: JobParameters?): Boolean {
        Log.d("debug"," -- finish the job !")
        return true
    }

    /**
     * Initialize the broadcast receiver
     */
    private fun initializeBroadcastReceievr() {
        val obs = this as Observer<Any>
        fcmReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (!YoctuUtils.ForegroundCheck().execute(context).get()) {
                    intent?.let { i ->
                        i.extras?.let { set ->
                            Log.d("debug"," (Job) is in foreground ? false ")
                            val it = set.keySet().iterator()
                            var cpt = 0
                            val total = 3
                            var notification = Notification()
                            while (it.hasNext() && cpt <= total) {
                                val elt = it.next().toString()
                                when(elt) {
                                    MyFirebaseMessagingService.KEY_MESSAGE -> {
                                        notification.body = set.get(elt).toString()
                                        cpt++
                                    }
                                    MyFirebaseMessagingService.KEY_TITLE -> {
                                        notification.title = set.get(elt).toString()
                                        cpt++
                                    }
                                    MyFirebaseMessagingService.KEY_TOPIC -> {
                                        notification.topic = set.get(elt).toString()
                                        cpt++
                                    }
                                }
                            }
                            Log.d("debug","Job (killed/background app.) notification is ".plus(notification.toString()))
                            (YoctuApplication.kodein.with(context).instance() as YoctuRepository).saveNotification(notification,obs)
                        }
                    }
                }
            }
        }
    }

    override fun onSubscribe(d: Disposable) {}
    override fun onError(e: Throwable) {}
    override fun onNext(t: Any) {
        Log.d("debug"," response is ".plus(t.toString()))
    }
    override fun onComplete() {}

    override fun onDestroy() {
        super.onDestroy()
        fcmReceiver?.let { receiver -> unregisterReceiver(receiver) }
    }

}