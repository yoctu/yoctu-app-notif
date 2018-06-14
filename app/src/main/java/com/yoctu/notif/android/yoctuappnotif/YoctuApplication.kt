package com.yoctu.notif.android.yoctuappnotif

import android.app.Application
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.PersistableBundle
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.yoctu.notif.android.yoctuappnotif.dependencies.YoctuModule
import com.yoctu.notif.android.yoctuappnotif.fcm.receiver.FirebaseBackgroundService
import com.yoctu.notif.android.yoctuappnotif.schedulers.FCMBackgroundJobService
import com.yoctu.notif.android.yoctulibrary.BuildConfig
import com.yoctu.notif.android.yoctulibrary.LibraryUtils
import com.yoctu.notif.android.yoctulibrary.repository.interceptor.YoctuInterceptor
import io.realm.Realm
import io.realm.RealmConfiguration
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created on 26.03.18.
 */

class YoctuApplication : Application() {

    companion object {
        lateinit var retrofit : Retrofit
        lateinit var kodein : Kodein
    }

    override fun onCreate() {
        super.onCreate()
        initialiseRepository()
        LibraryUtils.configureRealm(applicationContext)
        launchJobSCheduler()
    }

    /**
     * initialize retrofit 2
     */
    private fun initialiseRepository() {
        retrofit =
                Retrofit
                        .Builder()
                        .baseUrl(BuildConfig.SERVER_URL)
                        .client(initializeOkhhtp3())
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .build()

        kodein = Kodein { import(YoctuModule.kodein) }
    }

    /**
     * initialize okhttp 3
     */
    private fun initializeOkhhtp3() =
            OkHttpClient
            .Builder()
            .addInterceptor(YoctuInterceptor())
            .build()

    /**
     * Launch services
     * check if Android version is 23 to set Jobscheduler
     * else enable broadcast receiver
     */
    private fun launchJobSCheduler() {
        val presenter = kodein.instance() as GlobalPresenterContract.Presenter
        presenter.setBroadcastReceiver(false)
        val component = ComponentName(this, FirebaseBackgroundService::class.java)
        applicationContext.packageManager.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP)
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            presenter.setJobSCheduler(true)
            val serviceComponentName = ComponentName(applicationContext, FCMBackgroundJobService::class.java)
            var builder : JobInfo.Builder = JobInfo.Builder(0,serviceComponentName)
            builder.setMinimumLatency(100)
            val jobScheduler : JobScheduler = applicationContext.getSystemService(JobScheduler::class.java)
            jobScheduler.schedule(builder.build())
        }
    }
}