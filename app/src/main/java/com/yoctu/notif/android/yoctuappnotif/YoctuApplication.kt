package com.yoctu.notif.android.yoctuappnotif

import android.app.Application
import com.github.salomonbrys.kodein.Kodein
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.yoctu.notif.android.yoctuappnotif.dependencies.YoctuModule
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

}