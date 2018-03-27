package com.yoctu.notif.android.yoctulibrary.repository.retrofit

import com.yoctu.notif.android.yoctulibrary.models.Channel
import com.yoctu.notif.android.yoctulibrary.models.ParamBodyDeviceID
import com.yoctu.notif.android.yoctulibrary.models.ResponseChannels
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 * Contains all urls which will be used by Retrofit
 *
 * Created on 26.03.18.
 */

interface YoctuService {

    @GET("/api/channels")
    fun getChannels(): Observable<ResponseChannels>

    @Headers("Content-Type: application/json")
    @POST("/api/notifications")
    fun sendDeviceId(@Body params : ParamBodyDeviceID)
}