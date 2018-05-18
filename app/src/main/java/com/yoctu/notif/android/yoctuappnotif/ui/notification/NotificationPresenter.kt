package com.yoctu.notif.android.yoctuappnotif.ui.notification

import android.content.Context
import android.util.Log
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.with
import com.google.firebase.auth.FirebaseAuth
import com.yoctu.notif.android.yoctuappnotif.YoctuApplication
import com.yoctu.notif.android.yoctuappnotif.comparator.ComparatorNotification
import com.yoctu.notif.android.yoctuappnotif.repository.YoctuRepository
import com.yoctu.notif.android.yoctuappnotif.ui.add.AddTopicURLActivity
import com.yoctu.notif.android.yoctuappnotif.ui.login.LoginActivity
import com.yoctu.notif.android.yoctuappnotif.ui.topic.TopicActivity
import com.yoctu.notif.android.yoctuappnotif.utils.YoctuUtils
import com.yoctu.notif.android.yoctulibrary.models.Notification
import com.yoctu.notif.android.yoctulibrary.models.ViewType
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import java.util.*
import kotlin.collections.ArrayList

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

    override fun getMessages(): ArrayList<ViewType> {
        var list : ArrayList<ViewType> = repository.getNotifications()
        list.sortWith(ComparatorNotification)
        return list
    }

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
        //LoginActivity.newIntent(mContext)
        TopicActivity.newIntent(mContext)
    }

    /**
     * redirect to view to manage topic URL
     */
    override fun redirectToManageTopicURL() {
        AddTopicURLActivity.newInten(mContext)
    }

    override fun dropView() {
        mView = null
    }

    override fun deleteNotification(notification: Notification) {
        repository.deleteNotification(notification,this)
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

        if (result is Boolean) {
            Log.d(YoctuUtils.TAG_DEBUG," result is ".plus(result))
            when(result) {
                false -> { mView?.let { v -> v.deleteError() } }
                true -> { mView?.let { v -> v.deletedWithSuccess() } }
            }
        }
    }

    override fun onComplete() {}
}