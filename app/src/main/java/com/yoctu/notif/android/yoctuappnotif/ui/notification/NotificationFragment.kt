package com.yoctu.notif.android.yoctuappnotif.ui.notification

import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.with
import com.yoctu.notif.android.yoctuappnotif.R
import com.yoctu.notif.android.yoctuappnotif.YoctuApplication
import com.yoctu.notif.android.yoctuappnotif.callback.CallbackBroadcast
import com.yoctu.notif.android.yoctuappnotif.ui.adapters.YoctuAdapter
import com.yoctu.notif.android.yoctuappnotif.utils.BroadcastUtils
import com.yoctu.notif.android.yoctuappnotif.utils.NotificationUtils
import com.yoctu.notif.android.yoctuappnotif.utils.YoctuUtils
import kotlinx.android.synthetic.main.default_toolbar.*
import kotlinx.android.synthetic.main.fragment_notification.*

/**
 * Created on 27.03.18.
 */

class NotificationFragment:
        Fragment(),
        NotificationContract.View,
        CallbackBroadcast {

    private var notificationPresenter : NotificationContract.Presenter? = null
    private lateinit var toolbar : Toolbar
    private lateinit var recyclerView : RecyclerView
    private lateinit var adapter : YoctuAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater!!.inflate(R.layout.fragment_notification,container,false)
        return v
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        notificationPresenter = YoctuApplication.kodein.with(activity).instance()
        manageToolbar()
        initializeRecyclerView()
    }

    override fun onStart() {
        super.onStart()

        manageBroadcast()
    }

    override fun onResume() {
        super.onResume()

        notificationPresenter?.let { notificationPresenter!!.takeView(this) }
    }

    private fun manageToolbar() {
        toolbar_notification_fragment?.let {
            toolbar = toolbar_notification_fragment
            (activity as AppCompatActivity).setSupportActionBar(toolbar)

            YoctuUtils.changeToolbarColor(toolbar,activity!!.resources.getColor(R.color.colorPrimaryNoActionBar))
            toolbar_standard_title?.let {
                toolbar_standard_title.text = activity.getString(R.string.notif_fragment_title_view)
            }
        }
    }

    /**
     * This function allows to register to local broadcast receiver
     * and register the fragment as call back
     */
    private fun manageBroadcast() {
        LocalBroadcastManager.getInstance(activity)
                .registerReceiver(BroadcastUtils.mNotificationReceiver,
                        IntentFilter(YoctuUtils.INTENT_FILTER_FCM))

        BroadcastUtils.registerNotifFragment(this)
    }

    /**
     * initialize recycler view and its adapter
     * ask messages to presenter
     */
    fun initializeRecyclerView() {
        recyclerView = notification_fragment_recycler_view
        recyclerView?.let {
            var layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(activity!!)
            recyclerView.layoutManager = layoutManager
            adapter = YoctuAdapter(activity)
            recyclerView.adapter = adapter

            populateRecyclerView()
        }
    }

    /**
     * This function allows to populate the list
     * show/hide text and logo
     */
    override fun populateRecyclerView() {
        notificationPresenter?.let {
            val list = notificationPresenter!!.getMessages()
            if(list.size > 0) {
                notification_fragment_container_img?.let { notification_fragment_container_img.visibility = View.GONE }
                notification_fragment_empty_list?.let { notification_fragment_empty_list.visibility = View.GONE }
                recyclerView.visibility = View.VISIBLE
                adapter.addItems(notificationPresenter!!.getMessages())
            } else {
                notification_fragment_empty_list?.let { notification_fragment_empty_list.visibility = View.VISIBLE }
                notification_fragment_container_img?.let { notification_fragment_container_img.visibility = View.VISIBLE }
                recyclerView.visibility = View.GONE
            }
        }
    }

    /**
     * Manage message from FCM
     * @param message
     */
    override fun getMessage(message: String) {
        val obj = YoctuUtils.getNotificationFromJson(message)
        NotificationUtils.createNotification(activity,obj.title,obj.body)
        notificationPresenter?.let {
            notificationPresenter!!.saveMessage(obj)
        }
    }

    override fun getToken(token: String) {}

    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(activity)
                .unregisterReceiver(BroadcastUtils.mNotificationReceiver)

    }

    override fun onDestroy() {
        super.onDestroy()
    }
}