package com.yoctu.notif.android.yoctulibrary

import android.content.Context
import com.yoctu.notif.android.yoctulibrary.realm.module.NotificationsModule
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import io.realm.RealmConfiguration
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created on 27.03.18.
 */

object LibraryUtils {

    val TAG_DEBUG = "debug"
    val TAG_ERROR = "E"

    private var realmFileName = ""
    private var encryptKey ="5454GSJJLJJbvkl525app,nbKLKJL51/*985koi$**+§'!kgfhyt320189Yoctu"

    /**
     * initialize realm
     */
    fun configureRealm(context: Context) {
        if(BuildConfig.DEBUG)
            realmFileName = "yoctuappdebug.realmRepo"
        else
            realmFileName = "yoctuapp.realmRepo"

        Realm.init(context)
        val configurationChallengeMe = RealmConfiguration
                .Builder()
                .name(realmFileName)
                .encryptionKey(encryptKey.toByteArray())
                .schemaVersion(1)
                .modules(NotificationsModule())
                .build()

        Realm.setDefaultConfiguration(configurationChallengeMe)
    }

    /**
     * set schedulers and thread for RX
     */
    fun setObservable(observable: Observable<Any>, subscriber : Observer<Any>) {
        observable
                .subscribeOn(Schedulers.newThread()) //observable
                .observeOn(AndroidSchedulers.mainThread()) //observer
                .subscribe(subscriber)

    }

    /**
     * Transfrm time in String
     *
     * @param time by default is the current date
     * @return String
     */
    fun formatDate(time : Long = Date().time) : String {
        val d = Date(time)
        val simpleFormat = SimpleDateFormat("dd/MM/yyyy hh:mm")
        return simpleFormat.format(d)
    }
}
