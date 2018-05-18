package com.yoctu.notif.android.yoctuappnotif.ui.topic

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.yoctu.notif.android.yoctuappnotif.R
import com.yoctu.notif.android.yoctuappnotif.utils.YoctuUtils

/**
 * Created by gael on 16.05.18.
 */
class TopicActivity: AppCompatActivity() {

    companion object {

        fun newIntent(context: Context) = context.startActivity(Intent(context,TopicActivity::class.java))
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun getLayoutResId() = R.layout.activity_topic

    override fun onResume() {
        super.onResume()
        onNewIntent(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setContentView(getLayoutResId())
        var topicFragment : TopicFragment? = YoctuUtils.getFragment(supportFragmentManager, R.id.notification_container_fragment) as? TopicFragment
        if (topicFragment == null) {
            topicFragment = TopicFragment.newInstance()
            YoctuUtils.addFragment(
                    supportFragmentManager,
                    topicFragment,
                    R.id.activity_topic_container)
        }
    }

}