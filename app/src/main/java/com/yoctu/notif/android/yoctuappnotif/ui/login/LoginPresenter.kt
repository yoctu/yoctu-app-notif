package com.yoctu.notif.android.yoctuappnotif.ui.login

import android.content.Context
import android.util.Log
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.with
import com.google.firebase.messaging.FirebaseMessaging
import com.yoctu.notif.android.yoctuappnotif.YoctuApplication
import com.yoctu.notif.android.yoctuappnotif.repository.YoctuRepository
import com.yoctu.notif.android.yoctuappnotif.ui.notification.NotificationActivity
import com.yoctu.notif.android.yoctuappnotif.utils.YoctuUtils
import com.yoctu.notif.android.yoctulibrary.models.Channel
import com.yoctu.notif.android.yoctulibrary.models.ResponseChannels
import com.yoctu.notif.android.yoctulibrary.models.User
import com.yoctu.notif.android.yoctulibrary.models.ViewType
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

    private var mView : LoginContract.View? = null

    private var repository : YoctuRepository = YoctuApplication.kodein.with(mContext).instance()

    override fun askChannels() {
        repository.getChannels(this)
    }

    /**
     * check if there is an old list of toppics to unsubscribe and delete it
     * save the new list
     *
     * @param chosen
     */
    private fun manageChannels(chosen: ArrayList<ViewType>) {
        //if if there is old list
        val olToppics = repository.getToppics()
        if(olToppics != null) {
            olToppics.forEach { olToppic ->
                olToppic as Channel
                //FirebaseMessaging.getInstance().unsubscribeFromTopic(olToppic.name)
            }
            repository.deleteChannels()
        }
        chosen.forEach { t: ViewType? ->
            t as Channel
            Log.d(YoctuUtils.TAG_DEBUG,"channel is ".plus(t.name))
            //TODO subscribe toppic
            //FirebaseMessaging.getInstance().subscribeToTopic(t.name)
        }
    }

    /**
     * subscribe to toppic(s) and save in local the list
     * redirect user to notification view
     */
    override fun saveChannels(chosen: ArrayList<ViewType>) {
        manageChannels(chosen)
        repository.saveToppics(chosen)
        mContext.startActivity(NotificationActivity.newIntent(mContext))
    }

    /**
     * Show list and button to register the chosen
     */
    override fun showChannels() {
        mView?.let {
            mView!!.hideProgressBar()
            mView!!.getChannels(YoctuUtils.fakeChannels())
        }
    }

    override fun saveUserInLocal(user: User) {
        repository.saveUser(user)
    }

    override fun getUser() = repository.getUser()

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
        if(response is ResponseChannels) { // ask google sign in
            Log.d(YoctuUtils.TAG_DEBUG, " response channel is ".plus(response.data == null).plus(" ").plus(response.data!!.size))
            mView?.let {
                mView!!.googleSignIn()
            }
        }

    }

    override fun onComplete() {}

}