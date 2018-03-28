package com.yoctu.notif.android.yoctuappnotif.ui.login

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

    private var loginPresenter : LoginContract.Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loginPresenter = YoctuApplication.kodein.with(this).instance()

        setContentView(R.layout.activity_login)

        displayGoodView()
    }

    private fun displayGoodView() {
        loginPresenter?.let {
            val currentUser = loginPresenter!!.getUser()
            val currentToppics = loginPresenter!!.getToppics()

            if ( (currentUser == null) || (currentUser != null && currentToppics == null) ) { //first time OR no currentToppics
                displayLoginFragment()
            } else if (currentUser != null && currentToppics != null) {
                startActivity(NotificationActivity.newIntent(this))
            }
        }
    }

    private fun displayLoginFragment() {
        var loginFragment : LoginFragment? = YoctuUtils.getFragment(supportFragmentManager,R.id.login_container_fragment) as? LoginFragment
        if(loginFragment == null) {
            loginFragment = YoctuApplication.kodein.instance()
            YoctuUtils.addFragment(
                    supportFragmentManager,
                    loginFragment,
                    R.id.login_container_fragment)
        }
    }
}