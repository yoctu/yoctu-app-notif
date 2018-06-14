package com.yoctu.notif.android.yoctuappnotif.ui.login

import android.content.Context
import android.util.Log
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.with
import com.google.firebase.messaging.FirebaseMessaging
import com.yoctu.notif.android.yoctuappnotif.YoctuApplication
import com.yoctu.notif.android.yoctuappnotif.repository.YoctuRepository
import com.yoctu.notif.android.yoctuappnotif.ui.notification.NotificationActivity
import com.yoctu.notif.android.yoctuappnotif.utils.BroadcastUtils
import com.yoctu.notif.android.yoctuappnotif.utils.YoctuUtils
import com.yoctu.notif.android.yoctulibrary.models.*
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * Created on 26.03.18.
 */

class LoginPresenter(context: Context) :
        LoginContract.Presenter,
        Observer<Any> {

    private var mContext: Context
    init {
        mContext = context
    }
    //indicates if we have to reload notifications
    private var haveToReloadNotifications = false

    private var mView : LoginContract.View? = null

    private var repository : YoctuRepository = YoctuApplication.kodein.with(mContext).instance()

    override fun gotoNotifications() {
        mContext.startActivity(NotificationActivity.newIntent(mContext))
    }

    override fun saveUserInLocal(user: User) {
        repository.saveUser(user)
    }

    override fun sendDeviceId() {
        repository.saveDeviceId(getUser()!!.email, getUser()!!.firebaseToken,this)
    }

    override fun getUser() = repository.getUser()

    override fun changeToppics() = repository.getChangeToppics()

    override fun setChangeToppics(newValue: Boolean) {
        repository.changeToppics(newValue)
    }

    override fun saveMessage(notification: Notification) {
        repository.saveNotification(notification,this)
    }

    override fun haveToReloadNotifications(value: Boolean) {
        this.haveToReloadNotifications = value
    }

    override fun takeView(view: LoginContract.View) {
        mView = view
    }

    override fun dropView() {
        mView = null
    }

    override fun onSubscribe(d: Disposable) {}

    override fun onError(e: Throwable) {
        Log.d(YoctuUtils.TAG_ERROR,"error is ".plus(e.message))
    }

    override fun onNext(response: Any) {
        Log.d(YoctuUtils.TAG_DEBUG," --- ".plus(response).plus(" --- "))
        if (response is String && haveToReloadNotifications) {
            haveToReloadNotifications = false
            Log.d(YoctuUtils.TAG_DEBUG," ---  have to reload !!!! ")
            BroadcastUtils.reloadNotification(mContext)
        }
        if (response is ResponseDeviceId)
            Log.d(YoctuUtils.TAG_DEBUG,"save device id ".plus(response.status))
    }

    override fun onComplete() {}

}