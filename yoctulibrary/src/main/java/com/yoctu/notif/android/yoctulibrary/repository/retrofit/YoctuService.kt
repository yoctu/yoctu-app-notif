package com.yoctu.notif.android.yoctulibrary.repository.retrofit

import com.yoctu.notif.android.yoctulibrary.models.Channel
import io.reactivex.Observable
import retrofit2.http.GET

/**
 * Contains all urls which will be used by Retrofit
 *
 * Created on 26.03.18.
 */

interface YoctuService {

    @GET("/api/channels")
    fun getChannels(): Observable<List<Channel>>
}