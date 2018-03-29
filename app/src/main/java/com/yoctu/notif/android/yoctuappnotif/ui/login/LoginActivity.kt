package com.yoctu.notif.android.yoctuappnotif.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.with
import com.yoctu.notif.android.yoctuappnotif.R
import com.yoctu.notif.android.yoctuappnotif.YoctuApplication
import com.yoctu.notif.android.yoctuappnotif.ui.notification.NotificationActivity
import com.yoctu.notif.android.yoctuappnotif.utils.YoctuUtils

/**
 * Created on 26.03.18.
 */

class LoginActivity : AppCompatActivity() {

    companion object {
        val KEY_SIGN_OUT = "key_sign_out"
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
    }

    private var loginPresenter : LoginContract.Presenter? = null
    private var signOut = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        onNewIntent(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        loginPresenter = YoctuApplication.kodein.with(this).instance()

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
                val currentUser = loginPresenter!!.getUser()
                val changeToppics = loginPresenter!!.changeToppics()

                if ( (currentUser == null) || (currentUser != null && changeToppics) ) { //first time OR want change toppics
                    if(changeToppics)
                        loginPresenter!!.setChangeToppics(false)

                    displayLoginFragment()
                } else if (currentUser != null && !changeToppics) {
                    finish()
                    startActivity(NotificationActivity.newIntent(this))
                }
            }
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
        super.onBackPressed()
    }
}