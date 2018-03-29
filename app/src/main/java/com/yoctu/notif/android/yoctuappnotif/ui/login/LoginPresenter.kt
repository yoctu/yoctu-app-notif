package com.yoctu.notif.android.yoctuappnotif.ui.login

import android.content.Context
import android.util.Log
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.with
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
     * Channels saved in shared preferences
     *
     * @return list of toppics
     */
    override fun getToppics() = repository.getToppics()

    /**
     * check if there is an old list of toppics to unsubscribe and delete it
     * save the new list
     *
     * @param chosen
     */
    private fun manageChannels(chosen: ArrayList<ViewType>) {
        //if if there is old list
        val olToppics = getToppics()
        if(olToppics != null) {
            olToppics.forEach { olToppic ->
                olToppic as Channel
                //TODO unsubscribe toppics
                //FirebaseMessaging.getInstance().unsubscribeFromTopic(olToppic.name)
                Log.d(YoctuUtils.TAG_DEBUG,"remove channel ".plus(olToppic.name))
            }
            repository.deleteChannels()
        }
        chosen.forEach { t: ViewType? ->
            t as Channel
            Log.d(YoctuUtils.TAG_DEBUG,"add channel ".plus(t.name))
            //TODO subscribe toppic
            //FirebaseMessaging.getInstance().subscribeToTopic(t.name)
        }
    }

    override fun gotoNotifications() {
        mContext.startActivity(NotificationActivity.newIntent(mContext))
    }

    /**
     * subscribe to toppic(s) and save in local the list
     * redirect user to notification view
     */
    override fun saveChannels(chosen: ArrayList<ViewType>) {
        manageChannels(chosen)
        repository.saveToppics(chosen)
        Log.d(YoctuUtils.TAG_DEBUG,"**** register channels(".plus(chosen.size).plus(") ").plus("got to notif *** "))
        gotoNotifications()
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

    override fun sendDeviceId() {
        repository.saveDeviceId(getUser()!!.email, getUser()!!.firebaseToken,this)
    }

    override fun getUser() = repository.getUser()

    override fun changeToppics() = repository.getChangeToppics()

    override fun setChangeToppics(newValue: Boolean) {
        repository.changeToppics(newValue)
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
        if(response is ResponseChannels) { // ask google sign in
            Log.d(YoctuUtils.TAG_DEBUG, " response channel is ".plus(response.data == null).plus(" ").plus(response.data!!.size))
            mView?.let {
                mView!!.googleSignIn()
            }
        }
        if (response is String)
            Log.d(YoctuUtils.TAG_DEBUG,"save decive id ".plus(response))

    }

    override fun onComplete() {}

}