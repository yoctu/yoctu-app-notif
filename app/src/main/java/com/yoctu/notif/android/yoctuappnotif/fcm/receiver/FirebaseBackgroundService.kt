package com.yoctu.notif.android.yoctuappnotif.fcm.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.with
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
 *
 * Created on 13.06.18.
 */

class FirebaseBackgroundService:
        BroadcastReceiver(),
        Observer<Any> {

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