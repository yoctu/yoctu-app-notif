package com.yoctu.notif.android.yoctuappnotif.dependencies

import android.content.Context
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.multiton
import com.github.salomonbrys.kodein.singleton
import com.yoctu.notif.android.yoctuappnotif.managers.ManageGoogleSignin
import com.yoctu.notif.android.yoctuappnotif.repository.YoctuRepository
import com.yoctu.notif.android.yoctuappnotif.ui.login.LoginContract
import com.yoctu.notif.android.yoctuappnotif.ui.login.LoginFragment
import com.yoctu.notif.android.yoctuappnotif.ui.login.LoginPresenter
import com.yoctu.notif.android.yoctuappnotif.ui.notification.NotificationActivity
import com.yoctu.notif.android.yoctuappnotif.ui.notification.NotificationContract
import com.yoctu.notif.android.yoctuappnotif.ui.notification.NotificationFragment
import com.yoctu.notif.android.yoctuappnotif.ui.notification.NotificationPresenter
import com.yoctu.notif.android.yoctulibrary.repository.manager.ManagerSharedPreferences
import com.yoctu.notif.android.yoctulibrary.repository.retrofit.YoctuService
import retrofit2.Retrofit

/**
 * Define the Dependencies for the project
 *
 * Created on 26.03.18.
 */

object YoctuModule {
    val kodein = Kodein.Module {

        //mvp
        // ** log in **
        bind<LoginContract.Presenter>() with multiton { context: Context -> LoginPresenter(context) }
        bind<LoginFragment>() with singleton { LoginFragment() }
        // ** notification **
        bind<NotificationContract.Presenter>() with multiton { context: Context -> NotificationPresenter(context) }
        bind<NotificationFragment>() with singleton { NotificationFragment() }

        //managers
        bind<ManageGoogleSignin>() with multiton { context: Context -> ManageGoogleSignin(context) }
        bind<ManagerSharedPreferences>() with multiton { context: Context -> ManagerSharedPreferences(context) }

        //retrofit
        bind<YoctuService>() with  multiton { retrofit: Retrofit -> retrofit.create(YoctuService::class.java) }

        //repository
        bind<YoctuRepository>() with multiton { context: Context -> YoctuRepository(context) }
    }
}