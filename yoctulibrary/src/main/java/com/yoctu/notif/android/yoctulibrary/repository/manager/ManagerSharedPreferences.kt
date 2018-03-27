package com.yoctu.notif.android.yoctulibrary.repository.manager

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.yoctu.notif.android.yoctulibrary.LibraryUtils
import com.yoctu.notif.android.yoctulibrary.models.User

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

    /**
     * Write user in shared preferences
     *
     * @param user
     */
    fun saveUser(user: User) {
        val gson = Gson()
        val obj = gson.toJson(user)
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

}