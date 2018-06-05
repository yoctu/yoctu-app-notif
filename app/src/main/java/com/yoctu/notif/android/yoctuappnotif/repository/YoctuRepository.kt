package com.yoctu.notif.android.yoctuappnotif.repository

import android.content.Context
import android.util.Log
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.with
import com.yoctu.notif.android.yoctuappnotif.YoctuApplication
import com.yoctu.notif.android.yoctuappnotif.utils.YoctuUtils
import com.yoctu.notif.android.yoctulibrary.LibraryUtils
import com.yoctu.notif.android.yoctulibrary.models.Notification
import com.yoctu.notif.android.yoctulibrary.models.ParamBodyDeviceID
import com.yoctu.notif.android.yoctulibrary.models.User
import com.yoctu.notif.android.yoctulibrary.models.ViewType
import com.yoctu.notif.android.yoctulibrary.realm.LocalDB
import com.yoctu.notif.android.yoctulibrary.repository.Repository
import com.yoctu.notif.android.yoctulibrary.repository.manager.ManagerSharedPreferences
import com.yoctu.notif.android.yoctulibrary.repository.retrofit.YoctuService
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Repository to save and get models
 *
 * Created on 26.03.18.
 */

class YoctuRepository(context: Context) : Repository {
    private var mContext: Context
    init {
        mContext = context
    }
    private val request : YoctuService = YoctuApplication.kodein.with(YoctuApplication.retrofit).instance()
    private val localManager : ManagerSharedPreferences = YoctuApplication.kodein.with(mContext).instance()
    private val database : LocalDB = YoctuApplication.kodein.with(mContext).instance()

    override fun getChannels(observer: Observer<Any>) {
        val obs = request.getChannels()
        obs
                .subscribeOn(Schedulers.newThread()) //observable
                .observeOn(AndroidSchedulers.mainThread()) //observer
                .subscribe(observer)

    }


    override fun saveDeviceId(email: String, token: String, observer: Observer<Any>) {
        request.sendDeviceId(ParamBodyDeviceID(email,token))
                .subscribeOn(Schedulers.newThread()) //observable
                .observeOn(AndroidSchedulers.mainThread()) //observer
                .subscribe(observer)
    }

    override fun saveUser(user: User) {
        localManager.saveUser(user)
    }

    override fun getUser() = localManager.getUser()

    override fun deleteUser() {
        localManager.deleteUser()
    }

    override fun saveToppics(list: ArrayList<ViewType>) {
        localManager.saveChannels(list)
    }

    override fun getToppics() = localManager.getChannels()

    override fun getListToppicsName() = localManager.getChannelsName()

    override fun deleteChannels() {
        localManager.deleteChannels()
    }

    override fun saveNotification(notification: Notification,observer: Observer<Any>) {
        database.checkSizeNotificationTable(notification,observer)
    }

    override fun getNotifications() = database.getMessages()

    override fun saveEmail(email: String) {
        localManager.saveEmail(email)
    }

    override fun getEmail() = localManager.getEmail()

    override fun changeToppics(change: Boolean) {
        localManager.changeToppics(change)
    }

    override fun getChangeToppics() = localManager.getWantChangeToppics()

    override fun deleteTopics() {
        database.deleteTopics()
    }

    override fun deleteNotification(notification: Notification, observer: Observer<Any>) {
        database.deleteNotification(notification,observer)
    }

    override fun removeTopicURL() {
        localManager.deleteTopicURL()
    }

    override fun saveTopicURL(url: String) {
        localManager.saveTopicURL(url)
    }

    override fun getTopicURL() = localManager.getTopicURL()

    override fun saveApiKey(apiKey: String) {
        localManager.saveApiKey(apiKey)
    }

    override fun getApiKey() = localManager.getApiKey()
}