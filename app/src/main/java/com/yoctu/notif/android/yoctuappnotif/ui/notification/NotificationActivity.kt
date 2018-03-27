package com.yoctu.notif.android.yoctuappnotif.ui.notification

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.github.salomonbrys.kodein.instance
import com.yoctu.notif.android.yoctuappnotif.R
import com.yoctu.notif.android.yoctuappnotif.YoctuApplication
import com.yoctu.notif.android.yoctuappnotif.utils.YoctuUtils

/**
 * Created on 27.03.18.
 */

class NotificationActivity: AppCompatActivity() {

    companion object {
        fun newIntent(context: Context) = Intent(context, NotificationActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_notification)

        var notifFragment : NotificationFragment? = YoctuUtils.getFragment(supportFragmentManager, R.id.notification_container_fragment) as? NotificationFragment
        if (notifFragment == null) {
            notifFragment = YoctuApplication.kodein.instance()
            YoctuUtils.addFragment(
                    supportFragmentManager,
                    notifFragment,
                    R.id.notification_container_fragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}