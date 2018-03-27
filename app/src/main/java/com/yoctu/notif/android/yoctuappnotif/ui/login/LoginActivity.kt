package com.yoctu.notif.android.yoctuappnotif.ui.login

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.github.salomonbrys.kodein.instance
import com.yoctu.notif.android.yoctuappnotif.R
import com.yoctu.notif.android.yoctuappnotif.YoctuApplication
import com.yoctu.notif.android.yoctuappnotif.utils.YoctuUtils

/**
 * Created on 26.03.18.
 */

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

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