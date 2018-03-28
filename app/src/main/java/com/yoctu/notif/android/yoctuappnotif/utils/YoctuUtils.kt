package com.yoctu.notif.android.yoctuappnotif.utils

import android.app.Dialog
import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.Window
import com.yoctu.notif.android.yoctuappnotif.R
import com.yoctu.notif.android.yoctulibrary.models.Channel
import com.yoctu.notif.android.yoctulibrary.models.ViewType

/**
 * Util methods for the project
 *
 * Created on 26.03.18.
 */

object YoctuUtils {

    val TAG_DEBUG = "debug"
    val TAG_ERROR = "E"

    val CODE_GOOGLE_SIGN_IN = 38

    val KEY_MESSAGE_FCM = "message_fcm"
    val KEY_TOKEN_FCM = "token_fcm"

    val INTENT_FILTER_FCM = "com.yoctu.notif.android.yoctuappnotif.fcm"

    /**
     * @param supportManger
     * @param containerView
     * @param tag
     * @return Fragment
     */
    fun getFragment(supportManger : FragmentManager, containerView : Int, tag : String = "") = supportManger.findFragmentById(containerView)

    /**
     * @param supportManger
     * @param fragment
     * @param containerView
     * @param tag
     */
    fun addFragment(supportManger : FragmentManager, fragment : Fragment, containerView : Int,tag : String = "") {
        supportManger
                .beginTransaction()
                .add(containerView,fragment,"")
                .commit()
    }

    /**
     * This function allows to check network state
     *
     * @param context
     * @return Boolean
     */
    fun checkConnectivity(context: Context) : Boolean {
        var connected = true
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = connectivityManager.activeNetworkInfo
        if (netInfo == null)
            connected = false
        else
            connected = netInfo.isConnected

        return connected
    }

    /**
     * This function allows to create a dialog
     *
     * @param context
     * @param callback
     */
    fun dialogConnectivity(context: Context) : Dialog {
        var dialog = Dialog (context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_connectivity)

        return dialog
    }


    /**
     * @param view
     * @param message
     */
    fun displaySnackBar(view : View, message : String) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
    }


    /**
     * This function allows to close a Dialog
     *
     * @param d
     */
    fun closeDialog(d : Dialog) {
        if(d.isShowing)
            d.dismiss()
    }

    fun fakeChannels() : ArrayList<ViewType> {

        var fakeList = ArrayList<ViewType>()
        fakeList.add(Channel("intern"))
        fakeList.add(Channel("test"))
        fakeList.add(Channel("debug"))
        fakeList.add(Channel("production"))
        return fakeList
    }

    /**
     * Change the toolbar's color
     *
     * @param toolbar
     * @param colorId
     */
    fun changeToolbarColor(toolbar: android.support.v7.widget.Toolbar, colorId : Int) {
        toolbar.setBackgroundColor(colorId)
    }

    /**
     * Change the status bar color
     *
     * @param activity
     * @param colorId
     */
    fun changeStatusBarColor(activity: AppCompatActivity, colorId : Int) {
        var window = activity.window
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            window.statusBarColor = colorId
    }


}