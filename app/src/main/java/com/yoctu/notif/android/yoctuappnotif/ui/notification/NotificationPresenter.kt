package com.yoctu.notif.android.yoctuappnotif.ui.notification

import android.content.Context
import android.util.Log
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.with
import com.google.firebase.auth.FirebaseAuth
import com.yoctu.notif.android.yoctuappnotif.YoctuApplication
import com.yoctu.notif.android.yoctuappnotif.repository.YoctuRepository
import com.yoctu.notif.android.yoctuappnotif.ui.login.LoginActivity
import com.yoctu.notif.android.yoctuappnotif.utils.YoctuUtils
import com.yoctu.notif.android.yoctulibrary.models.Notification
import com.yoctu.notif.android.yoctulibrary.models.ViewType
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * Created on 27.03.18.
 */

class NotificationPresenter(context: Context):
        NotificationContract.Presenter,
        Observer<Any> {

    private var mContext : Context
    private var mView : NotificationContract.View? = null
    init {
        mContext = context
    }
    private var repository : YoctuRepository = YoctuApplication.kodein.with(mContext).instance()

    override fun takeView(view: NotificationContract.View) {
        mView = view
    }

    private fun gotoLogin() {
        LoginActivity.newIntent(mContext,true)
    }

    override fun logged() {
        if (repository.getUser() == null)
            mView?.let { mView!!.notLogged() }
    }

    override fun getMessages() = repository.getNotifications()

    override fun saveMessage(notification: Notification) {
        repository.saveNotification(notification,this)
    }


    /**
     * delete user in shared preference
     * redirect to login view
     */
    override fun googleSignOut() {
        repository.deleteUser()
        gotoLogin()
    }

    /**
     * redirect user to toppics view to choose other toppics
     */
    override fun redirectToChannels() {
        repository.changeToppics(true)
        LoginActivity.newIntent(mContext)
    }

    override fun dropView() {
        mView = null
    }

    override fun onSubscribe(d: Disposable) {}

    override fun onError(e: Throwable) {
        e.printStackTrace()
        Log.e(YoctuUtils.TAG_ERROR,e.message)
    }


    override fun onNext(result: Any) {
        if (result is String) { //notify the adapter
            Log.d(YoctuUtils.TAG_DEBUG," response from db is ".plus(result))
            mView?.let {
                mView!!.populateRecyclerView()
            }
        }
    }

    override fun onComplete() {}
}