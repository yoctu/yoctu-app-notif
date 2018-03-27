package com.yoctu.notif.android.yoctuappnotif.repository

import android.content.Context
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.with
import com.yoctu.notif.android.yoctuappnotif.YoctuApplication
import com.yoctu.notif.android.yoctulibrary.models.User
import com.yoctu.notif.android.yoctulibrary.models.ViewType
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

    override fun getChannels(observer: Observer<Any>) {
        val obs = request.getChannels()
        obs
                .subscribeOn(Schedulers.newThread()) //observable
                .observeOn(AndroidSchedulers.mainThread()) //observer
                .subscribe(observer)

    }

    //TODO
    override fun saveDeviceId(email: String, token: String, observer: Observer<Any>) {}

    override fun saveUser(user: User) {
        localManager.saveUser(user)
    }

    fun setObservable(observable: Observable<Any>, subscriber : Observer<Any>) {
        observable
                .subscribeOn(Schedulers.newThread()) //observable
                .observeOn(AndroidSchedulers.mainThread()) //observer
                .subscribe(subscriber)

    }

    override fun getUser() = localManager.getUser()

    override fun deleteUser() {
        localManager.deleteUser()
    }

    override fun saveToppics(list: ArrayList<ViewType>) {
        localManager.saveChannels(list)
    }

    override fun getToppics() = localManager.getChannels()

    override fun deleteChannels() {
        localManager.deleteChannels()
    }
}