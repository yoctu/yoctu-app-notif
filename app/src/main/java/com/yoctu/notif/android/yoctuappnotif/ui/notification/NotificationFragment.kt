package com.yoctu.notif.android.yoctuappnotif.ui.notification

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.with
import com.yoctu.notif.android.yoctuappnotif.R
import com.yoctu.notif.android.yoctuappnotif.YoctuApplication
import com.yoctu.notif.android.yoctuappnotif.utils.YoctuUtils
import kotlinx.android.synthetic.main.fragment_notification.*

/**
 * Created on 27.03.18.
 */

class NotificationFragment:
        Fragment(),
        NotificationContract.View {
    private var notificationPresenter : NotificationContract.Presenter? = null
    private lateinit var toolbar : Toolbar

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
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}