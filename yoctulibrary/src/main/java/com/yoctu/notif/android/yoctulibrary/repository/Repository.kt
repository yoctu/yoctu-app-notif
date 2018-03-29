package com.yoctu.notif.android.yoctulibrary.repository

import com.yoctu.notif.android.yoctulibrary.models.Notification
import com.yoctu.notif.android.yoctulibrary.models.User
import com.yoctu.notif.android.yoctulibrary.models.ViewType
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

    /**
     * This function gives a saved user
     * @return user
     */
    fun getUser(): User?


    /**
     * This function allows to remove a user
     */
    fun deleteUser()

    /**
     * Save the list of toppics
     *
     * @param list
     */
    fun saveToppics(list: ArrayList<ViewType>)

    /**
     * @return list of toppics
     */
    fun getToppics(): ArrayList<ViewType> ?

    /**
     * @return a list of toppics' name
     */
    fun getListToppicsName(): ArrayList<String>?

    /**
     * delete all the chosen toppics
     */
    fun deleteChannels()

    /**
     * save a notification in local database
     * @param notification
     * @param observer
     */
    fun saveNotification(notification: Notification, observer: Observer<Any>)

    /**
     * @return list of Notifications
     */
    fun getNotifications(): ArrayList<ViewType>

    /**
     * This function allows to save an email in shared preferences
     *
     * @param email
     */
    fun saveEmail(email: String)

    /**
     * Read an email saved in shared preferences
     *
     * @return String
     */
    fun getEmail(): String?

    /**
     * Indicate thta user wanst to change his list of toppics
     * @param change
     */
    fun changeToppics(change: Boolean)

    /**
     * Indicate if user wants to change list of toppics
     * @return Boolean
     */
    fun getChangeToppics() : Boolean
}