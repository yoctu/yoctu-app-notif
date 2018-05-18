package com.yoctu.notif.android.yoctulibrary.realm

import android.content.Context
import android.os.HandlerThread
import android.util.Log
import com.yoctu.notif.android.yoctulibrary.LibraryUtils
import com.yoctu.notif.android.yoctulibrary.models.Notification
import com.yoctu.notif.android.yoctulibrary.models.ViewType
import com.yoctu.notif.android.yoctulibrary.realm.module.NotificationsModule
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import io.realm.RealmConfiguration
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

    fun deleteTopics() {
        val results = yoctuRealm.where<Notification>().findAll()
        if (results.size >= limit)
            yoctuRealm.executeTransaction { realm ->  results.deleteAllFromRealm()}
    }

    /**
     * remove a specific notification
     */
    fun deleteNotification(notification: Notification,observer: Observer<Any>): Observable<Any> {
        var obs = Observable.create { emitter: ObservableEmitter<Any> ->
            val result = yoctuRealm
                    .where<Notification>()
                    .equalTo("title",notification.title)
                    .equalTo("body",notification.body)
                    .findFirst()
            if (result != null) {
                try {
                    yoctuRealm.beginTransaction()
                    result.deleteFromRealm()
                    yoctuRealm.commitTransaction()
                    emitter.onNext(true)
                }catch (e: Throwable) {
                    e.printStackTrace()
                    emitter.onError(Throwable(e.message))
                }
            } else
                emitter.onNext(false)

            emitter.onComplete()
        }
        obs.subscribe(observer)
        return obs

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
            if(readNotification(notification) == null) {
                try {
                    yoctuRealm.beginTransaction()
                    var noti : Notification = yoctuRealm.createObject(Notification::class.java)
                    noti.body = notification.body
                    noti.title = notification.title
                    noti.time = Date().time
                    noti.topic = notification.topic
                    yoctuRealm.copyToRealm(noti)
                    yoctuRealm.commitTransaction()
                    emitter.onNext(noti.title.plus(" ").plus(noti.body).plus(" ").plus(noti.topic).plus(" saved"))
                    emitter.onComplete()
                }catch (e : Throwable) {
                    e.printStackTrace()
                    emitter.onError(Throwable(e.message))
                }
            } else {
                emitter.onNext(notification.title.plus(" already saved "))
                emitter.onComplete()
            }
        }
    }

    /**
     * Gives a notification which matches with the parameter
     * check title and body
     *
     * @param notification
     * @return Notification
     */
    private fun readNotification(notification: Notification) =
            yoctuRealm
            .where<Notification>()
            .equalTo("title",notification.title)
            .equalTo("body",notification.body)
            .findFirst()


    fun getMessages() = ArrayList<ViewType>(yoctuRealm.where(Notification::class.java).findAll().toList())
}