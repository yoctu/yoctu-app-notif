package com.yoctu.notif.android.yoctuappnotif.ui.topic

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.with
import com.google.firebase.messaging.FirebaseMessaging
import com.yoctu.notif.android.yoctuappnotif.R
import com.yoctu.notif.android.yoctuappnotif.YoctuApplication
import com.yoctu.notif.android.yoctuappnotif.callback.CallbackChannelsResponse
import com.yoctu.notif.android.yoctuappnotif.repository.YoctuRepository
import com.yoctu.notif.android.yoctuappnotif.ui.notification.NotificationActivity
import com.yoctu.notif.android.yoctuappnotif.ui.topic.asynctask.ChannelsAsynctaskHTTP
import com.yoctu.notif.android.yoctuappnotif.ui.topic.asynctask.ChannelsAsynctaskHTTPS
import com.yoctu.notif.android.yoctuappnotif.utils.YoctuUtils
import com.yoctu.notif.android.yoctulibrary.models.Channel
import com.yoctu.notif.android.yoctulibrary.models.ResponseChannels
import com.yoctu.notif.android.yoctulibrary.models.ViewType
import com.yoctu.notif.android.yoctulibrary.repository.manager.ManagerSharedPreferences
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * Created by gael on 16.05.18.
 */

class TopicPresenter(context: Context):
        TopicContract.Presenter,
        CallbackChannelsResponse {

    private var mContext : Context
    private var mView : TopicContract.View? = null
    init {
        mContext = context
    }
    private var managerSharedPreferences: ManagerSharedPreferences = YoctuApplication.kodein.with(mContext).instance()
    private var repository : YoctuRepository = YoctuApplication.kodein.with(mContext).instance()

    override fun takeView(view: TopicContract.View) {
        this.mView = view
    }

    override fun askChannels() {
        val url = managerSharedPreferences.getTopicURL()
        var asynctask: AsyncTask<String,Void,String>? = null
        url?.let { u ->
            if (u.startsWith(YoctuUtils.TYPE_HTTPS))
                asynctask = ChannelsAsynctaskHTTPS(this)
            else
                asynctask = ChannelsAsynctaskHTTP(this)

            asynctask?.execute(u)
        }
    }

    override fun getChannels(code: Int, response: ResponseChannels?, error: String?) {
        when(code) {
            -5, 404 -> { mView?.let { v -> v.showError(mContext.resources.getString(R.string.topic_view_bad_url)) } }
            200 -> {
                mView?.let { v -> v.getChannels(response?.data as ArrayList<ViewType>) }
            }
        }
    }
    /**
     * Channels saved in shared preferences
     *
     * @return list of toppics
     */
    override fun getTopics() = repository.getToppics()

    /**
     * check if there is an old list of toppics to unsubscribe and delete it
     * save the new list
     *
     * @param chosen
     */
    private fun manageChannels(chosen: ArrayList<ViewType>) {
        //if if there is old list
        val olToppics = getTopics()
        if(olToppics != null) {
            olToppics.forEach { olToppic ->
                olToppic as Channel
                FirebaseMessaging.getInstance().unsubscribeFromTopic(olToppic.name)
                //Log.d(YoctuUtils.TAG_DEBUG,"remove channel ".plus(olToppic.name))
            }
            repository.deleteChannels()
        }
        chosen.forEach { t: ViewType? ->
            t as Channel
            // Log.d(YoctuUtils.TAG_DEBUG,"add channel ".plus(t.name))
            FirebaseMessaging.getInstance().subscribeToTopic(t.name)
        }
    }

    //redirect user to notifications view
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

    override fun dropView() {
        this.mView = null
    }
}