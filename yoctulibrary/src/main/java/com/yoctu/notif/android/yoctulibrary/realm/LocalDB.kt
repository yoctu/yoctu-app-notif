package com.yoctu.notif.android.yoctulibrary.realm

import android.content.Context
import android.os.HandlerThread
import android.util.Log
import com.yoctu.notif.android.yoctulibrary.LibraryUtils
import com.yoctu.notif.android.yoctulibrary.models.Notification
import com.yoctu.notif.android.yoctulibrary.models.ViewType
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import io.realm.kotlin.where
import java.lang.IllegalArgumentException
import java.util.*
import java.util.concurrent.Executors

/**
 * This class represents the local database
 *
 * Created on 28.03.18.
 */

class LocalDB(context: Context) {
    var yoctuRealm : Realm
    var mContext: Context
    init {
        mContext = context
        yoctuRealm = Realm.getDefaultInstance()
        Log.d(LibraryUtils.TAG_DEBUG," ----- database created ----- ")
    }
    private var limit = 100

    /**
     * THis function allows to close the opened database
     */
    fun closeMRealm() {
        if(!yoctuRealm.isClosed) {
            yoctuRealm.close()
            Log.d(LibraryUtils.TAG_DEBUG," ------ database closed ----- ")
        }
    }

    /**
     * This function check if the table has 100 elements to delete it
     *
     */
    fun checkSizeNotificationTable (notification: Notification, observer: Observer<Any>) {
       val results = yoctuRealm.where<Notification>().findAll()
        if (results.size >= limit) {
            results.subList(0,99)
            yoctuRealm.executeTransaction { realm ->  results.deleteAllFromRealm()}
        }
        insertNotification(notification).subscribe(observer)
    }

    /**
     * @param notification
     * @param observer
     */
    private fun insertNotification(notification: Notification): Observable<Any> {
        return Observable.create { emitter: ObservableEmitter<Any> ->
            try {
                yoctuRealm.beginTransaction()
                var noti : Notification = yoctuRealm.createObject(Notification::class.java)
                noti.body = notification.body
                noti.title = notification.title
                noti.time = Date().time
                yoctuRealm.copyToRealm(noti)
                yoctuRealm.commitTransaction()
                emitter.onNext("saved")
                emitter.onComplete()
            }catch (e : IllegalArgumentException) {
                e.printStackTrace()
                emitter.onError(Throwable(e.message))
            }
        }
    }

    fun getMessages() = ArrayList<ViewType>(yoctuRealm.where(Notification::class.java).findAll().toList())
}