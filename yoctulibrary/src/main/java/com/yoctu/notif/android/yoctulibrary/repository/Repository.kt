package com.yoctu.notif.android.yoctulibrary.repository

import com.yoctu.notif.android.yoctulibrary.models.User
import io.reactivex.Observer

/**
 * Define methods to communicate with the server
 *
 * Created on 26.03.18.
 */

interface Repository {

    /**
     * allows to get list of toppics from server
     *
     * @param observer
     */
    fun getChannels(observer : Observer<Any>)

    /**
     * allows to send device id for a user to server
     *
     * @param email
     * @param token
     * @param observer
     */
    fun saveDeviceId(email : String, token : String, observer : Observer<Any>)

    /**
     * allows to save user in local database
     *
     * @param user
     */
    fun saveUser (user: User)
}