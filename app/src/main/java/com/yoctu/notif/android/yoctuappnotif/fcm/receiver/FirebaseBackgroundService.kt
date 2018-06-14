package com.yoctu.notif.android.yoctuappnotif.fcm.receiver

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.with
import com.yoctu.notif.android.yoctuappnotif.GlobalPresenter
import com.yoctu.notif.android.yoctuappnotif.GlobalPresenterContract
import com.yoctu.notif.android.yoctuappnotif.YoctuApplication
import com.yoctu.notif.android.yoctuappnotif.fcm.MyFirebaseMessagingService
import com.yoctu.notif.android.yoctuappnotif.repository.YoctuRepository
import com.yoctu.notif.android.yoctuappnotif.utils.YoctuUtils
import com.yoctu.notif.android.yoctulibrary.models.Notification
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * Create a Broadcast receiver to get notification
 * in state background and killed
 * to save it in local database without user tap onto it !
 * case :
 *  -first launch of job service because there's a latency !
 *  - fr api < 23
 *
 * Created on 13.06.18.
 */

class FirebaseBackgroundService:
        BroadcastReceiver(),
        Observer<Any> {

    /**
     * This function is used for api <23 and when the job scheduler is launched for the first time
     * because there is a latency to get the notification
     *
     * FYI: we disable the broadcast if job scheduler is launched
     */
    override fun onReceive(context: Context?, intent: Intent?) {
        if (!YoctuUtils.ForegroundCheck().execute(context).get()) {
            intent?.let { i ->
                i.extras?.let { set ->
                    Log.d("debug"," is in foreground ? false ")
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
                    Log.d("debug","(killed/background app.) notification is ".plus(notification.toString()))
                    (YoctuApplication.kodein.with(context).instance() as YoctuRepository).saveNotification(notification,this)
                    if ( (YoctuApplication.kodein.instance() as GlobalPresenterContract.Presenter).jobSchedulerIsSet() ) {
                        Log.d("debug","disable itself for job scheduler !")
                        (YoctuApplication.kodein.instance() as GlobalPresenterContract.Presenter).setBroadcastReceiver(false)
                        val component = ComponentName(context, FirebaseBackgroundService::class.java)
                        context?.packageManager?.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP)
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
}