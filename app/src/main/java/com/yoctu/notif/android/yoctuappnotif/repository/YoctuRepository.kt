package com.yoctu.notif.android.yoctuappnotif.repository

import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.with
import com.yoctu.notif.android.yoctuappnotif.YoctuApplication
import com.yoctu.notif.android.yoctulibrary.models.User
import com.yoctu.notif.android.yoctulibrary.repository.Repository
import com.yoctu.notif.android.yoctulibrary.repository.retrofit.YoctuService
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created on 26.03.18.
 */

class YoctuRepository : Repository {
    private val request : YoctuService = YoctuApplication.kodein.with(YoctuApplication.retrofit).instance()

    override fun getChannels(observer: Observer<Any>) {
        val obs = request.getChannels()
        obs
                .subscribeOn(Schedulers.newThread()) //observable
                .observeOn(AndroidSchedulers.mainThread()) //observer
                .subscribe(observer)

    }

    //TODO
    override fun saveDeviceId(email: String, token: String, observer: Observer<Any>) {}

    //TODO
    override fun saveUser(user: User) {}

    fun setObservable(observable: Observable<Any>, subscriber : Observer<Any>) {
        observable
                .subscribeOn(Schedulers.newThread()) //observable
                .observeOn(AndroidSchedulers.mainThread()) //observer
                .subscribe(subscriber)

    }
}