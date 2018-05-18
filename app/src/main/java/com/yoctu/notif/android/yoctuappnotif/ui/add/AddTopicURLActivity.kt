package com.yoctu.notif.android.yoctuappnotif.ui.add

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.yoctu.notif.android.yoctuappnotif.R
import com.yoctu.notif.android.yoctuappnotif.utils.YoctuUtils

/**
 * Created by gael on 16.05.18.
 */

class AddTopicURLActivity: AppCompatActivity() {

    companion object {

        fun newInten(context: Context) = context.startActivity(Intent(context, AddTopicURLActivity::class.java))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_add_topic_url)

    }

    override fun onResume() {
        super.onResume()
        onNewIntent(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        var addTopicURLFragment : AddTopicURLFragment? = YoctuUtils.getFragment(supportFragmentManager, R.id.activity_add_topic_container_fragment) as? AddTopicURLFragment
        if (addTopicURLFragment == null) {
            addTopicURLFragment = AddTopicURLFragment.newInstance()
            YoctuUtils.addFragment(
                    supportFragmentManager,
                    addTopicURLFragment,
                    R.id.activity_add_topic_container_fragment)
        }
    }
}