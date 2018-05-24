package com.yoctu.notif.android.yoctuappnotif.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.with
import com.yoctu.notif.android.yoctuappnotif.R
import com.yoctu.notif.android.yoctuappnotif.YoctuApplication
import com.yoctu.notif.android.yoctuappnotif.callback.CallbackNavBack
import com.yoctu.notif.android.yoctuappnotif.ui.notification.NotificationActivity
import com.yoctu.notif.android.yoctuappnotif.ui.topic.TopicActivity
import com.yoctu.notif.android.yoctuappnotif.utils.YoctuUtils
import com.yoctu.notif.android.yoctulibrary.models.Notification

/**
 * Created on 26.03.18.
 */

class LoginActivity :
        AppCompatActivity(),
        CallbackNavBack {

    companion object {
        val KEY_SIGN_OUT = "key_sign_out"
        val KEY_HAS_NOTIFICATION = "key_has_notif"
        val KEY_NOTIFICATION_TITLE = "key_notif_title"
        val KEY_NOTIFICATION_TEXT = "key_notif_text"
        val KEY_DATA_TITLE = "title"
        val KEY_DATA_BODY = "body"
        val KEY_DATA_TOPIC = "topic"

        fun newIntent(context: Context) {
            var intent = Intent(context,LoginActivity::class.java)
            intent.putExtra(KEY_SIGN_OUT, false)
            context.startActivity(intent)
        }

        fun newIntent(context: Context,signout: Boolean = true) {
            var intent = Intent(context,LoginActivity::class.java)
            intent.putExtra(KEY_SIGN_OUT, signout)
            context.startActivity(intent)
        }

        fun NotificationIntent(context: Context, title: String, text: String, hasNotification: Boolean = true) {
            var intent = Intent(context,LoginActivity::class.java)
            intent.putExtra(KEY_HAS_NOTIFICATION, hasNotification)
            intent.putExtra(KEY_NOTIFICATION_TITLE, title)
            intent.putExtra(KEY_NOTIFICATION_TEXT, text)
            context.startActivity(intent)
        }
    }

    private var loginPresenter : LoginContract.Presenter? = null
    private var signOut = false
    private var title: String = ""
    private var text: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //onNewIntent(intent)
    }

    override fun onResume() {
        super.onResume()

        onNewIntent(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        loginPresenter = YoctuApplication.kodein.with(this).instance()


        //notification when app is in background
        if (intent != null && intent.hasExtra(KEY_DATA_TITLE) && intent.hasExtra(KEY_DATA_BODY)) {
            loginPresenter?.let {
                var n = Notification(intent.getStringExtra(KEY_DATA_TITLE),intent.getStringExtra(KEY_DATA_BODY))
                if (intent.hasExtra(KEY_DATA_TOPIC))
                    n.topic = intent.getStringExtra(KEY_DATA_TOPIC)
                loginPresenter!!.saveMessage(n)
            }
        }

        //manage sign out
        if (intent != null && intent.hasExtra(KEY_SIGN_OUT)) {
            signOut = intent!!.getBooleanExtra(KEY_SIGN_OUT,false)
            intent.removeExtra(KEY_SIGN_OUT)
        }

        setContentView(R.layout.activity_login)

        displayGoodView()
    }

    private fun displayGoodView() {
        if (signOut) {
            val loginFragment = LoginFragment.newInstance(signOut)
            YoctuUtils.addFragment(
                    supportFragmentManager,
                    loginFragment,
                    R.id.login_container_fragment)
        } else {
            loginPresenter?.let {
                var currentUser = loginPresenter!!.getUser()
                val changeToppics = loginPresenter!!.changeToppics()

                if ( (currentUser == null)) { //first time OR (want change topics OR not yet chosen)
                    displayLoginFragment()
                } else if (currentUser != null) {
                    finish()
                    startActivity(NotificationActivity.newIntent(this))
                } else if (changeToppics) {
                    finish()
                    TopicActivity.newIntent(this)
                }
            }
        }
    }

    //TODO if from background ??
    private fun mangeNotification() {
        if(intent != null && intent.hasExtra(KEY_HAS_NOTIFICATION)) {
            title = intent.getStringExtra(KEY_NOTIFICATION_TITLE)
            text = intent.getStringExtra(KEY_NOTIFICATION_TEXT)
            //TODO save in local db
        }
    }

    private fun displayLoginFragment() {
            val loginFragment = LoginFragment.newInstance()
            YoctuUtils.addFragment(
                    supportFragmentManager,
                    loginFragment,
                    R.id.login_container_fragment)
    }

    override fun onBackPressed() {
        //super.onBackPressed()
    }

    override fun goBack() {
        onBackPressed()
    }
}