package com.yoctu.notif.android.yoctulibrary.repository.manager

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yoctu.notif.android.yoctulibrary.LibraryUtils
import com.yoctu.notif.android.yoctulibrary.models.Channel
import com.yoctu.notif.android.yoctulibrary.models.User
import com.yoctu.notif.android.yoctulibrary.models.ViewType

/**
 * This class allows to manage Shared preferences
 *
 * Created on 27.03.18.
 */

class ManagerSharedPreferences(context: Context) {

    private var mContext : Context
    private var preferencesApplication : SharedPreferences
    init {
        mContext = context
        preferencesApplication = mContext.getSharedPreferences(mContext.packageName, Context.MODE_PRIVATE)
    }

    private val KEY_USER = "key_user"
    private val KEY_CHANNELS = "key_channels"
    private val KEY_EMAIL = "key_saved_email"
    private val KEY_WANT_CHANGE_TOPPICS = "key_change_toppics"

    /**
     * Write user in shared preferences
     *
     * @param user
     */
    fun saveUser(user: User) {
        /*val gson = Gson()
        val obj = gson.toJson(user)*/
        val obj = getGson(user)
        Log.d(LibraryUtils.TAG_DEBUG," write user".plus(obj).plus(" in shared preferences"))
        var editor = preferencesApplication.edit()
        editor.putString(KEY_USER,obj)
        editor.commit()
    }

    /**
     * @return user
     */
    fun getUser(): User? {
        if (preferencesApplication.contains(KEY_USER)) {
            val gson = Gson()
            val obj = preferencesApplication.getString(KEY_USER,"")
            Log.d(LibraryUtils.TAG_DEBUG," object user ".plus(obj))
            return gson.fromJson(obj,User::class.java)
        }
        return null
    }

    /**
     * This method allows to remove saved user into shared preferences
     */
    fun deleteUser() {
        var editor = preferencesApplication.edit()
        editor.remove(KEY_USER).commit()
    }

    /**
     * Save the list of channels in shared preferences
     *
     * @param list
     */
    fun saveChannels(list : ArrayList<ViewType>) {
        /*val gson = Gson()
        val obj = gson.toJson(user)*/
        val obj = getGson(list)
        Log.d(LibraryUtils.TAG_DEBUG," write list of channels".plus(obj).plus(" in shared preferences"))
        var editor = preferencesApplication.edit()
        editor.putString(KEY_CHANNELS,obj)
        editor.commit()
    }

    /**
     * Gives a model in format String
     *
     * @return String
     */
    private fun getGson(p : Any) : String{
        val gson = Gson()
        return gson.toJson(p)
    }

    /**
     * @return list of channels
     */
    fun getChannels() : ArrayList<ViewType>? {
        if (preferencesApplication.contains(KEY_CHANNELS)) {
            val gson = Gson()
            val obj = preferencesApplication.getString(KEY_CHANNELS,"")
            val type = object : TypeToken<ArrayList<Channel>>() {}.type
            Log.d(LibraryUtils.TAG_DEBUG," object channels ".plus(obj))
            return gson.fromJson(obj,type)
        }
        return null
    }

    /**
     * Give a list of channels' name
     * @return list of String
     */
    fun getChannelsName(): ArrayList<String>? {
        var names = ArrayList<String>()
        if (preferencesApplication.contains(KEY_CHANNELS)) {
            val gson = Gson()
            val obj = preferencesApplication.getString(KEY_CHANNELS,"")
            val type = object : TypeToken<ArrayList<Channel>>() {}.type
            val list : ArrayList<Channel> = gson.fromJson(obj,type)
            list.forEach { channel: Channel -> names.add(channel.name) }
            return names
        }
        return null
    }

    /**
     * This method allows to remove channels into shared preferences
     */
    fun deleteChannels() {
        var editor = preferencesApplication.edit()
        editor.remove(KEY_CHANNELS).commit()
    }

    /**
     * Save email to use it for futur sign in
     *
     * @param email
     */
    fun saveEmail(email: String) {
        var editor = preferencesApplication.edit()
        editor.putString(KEY_EMAIL,email)
        editor.commit()
    }

    /**
     * Give the email saved in shared preference for sign in
     * @return String
     */
    fun getEmail(): String? {
        if (preferencesApplication.contains(KEY_EMAIL)) {
            return preferencesApplication.getString(KEY_EMAIL,"")
        }
        return null
    }

    /**
     * Set change toppics
     * @param change
     */
    fun changeToppics(change: Boolean) {
        var editor = preferencesApplication.edit()
        editor.putBoolean(KEY_WANT_CHANGE_TOPPICS,change)
        editor.commit()
    }

    /**
     * Indicate if user wants change toppics
     * @return Boolean
     */
    fun getWantChangeToppics(): Boolean {
        if (preferencesApplication.contains(KEY_WANT_CHANGE_TOPPICS)) {
            return preferencesApplication.getBoolean(KEY_WANT_CHANGE_TOPPICS,false)
        }
        return false
    }

}