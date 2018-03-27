package com.yoctu.notif.android.yoctuappnotif.ui.login

import android.util.Log
import com.github.salomonbrys.kodein.instance
import com.yoctu.notif.android.yoctuappnotif.YoctuApplication
import com.yoctu.notif.android.yoctuappnotif.repository.YoctuRepository
import com.yoctu.notif.android.yoctuappnotif.utils.YoctuUtils
import com.yoctu.notif.android.yoctulibrary.models.Channel
import com.yoctu.notif.android.yoctulibrary.models.ViewType
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * Created on 26.03.18.
 */

class LoginPresenter :
        LoginContract.Presenter,
        Observer<Any> {

    private var mView : LoginContract.View? = null

    private var repository : YoctuRepository = YoctuApplication.kodein.instance()

    override fun askChannels() {
        repository.getChannels(this)
    }

    override fun saveChannels(chosen: ArrayList<ViewType>) {
        chosen.forEach { t: ViewType? ->
            t as Channel
            Log.d("debug","channel is ".plus(t.name))
        }
    }

    override fun takeView(view: LoginContract.View) {
        mView = view
    }

    override fun dropView() {
        mView = null
    }

    override fun onSubscribe(d: Disposable) {
        Log.d("debug","on subscribe !! ")
    }

    override fun onError(e: Throwable) {
        Log.d("debug","error is ".plus(e.message))
        mView?.let {
            mView!!.hideProgressBar()
            mView!!.getChannels(YoctuUtils.fakeChannels())
        }
    }

    override fun onNext(t: Any) {
        Log.d("debug","in on next !! ")
        //TODO list of channels
    }

    override fun onComplete() {
        Log.d("debug","on completed !! ")
    }

}