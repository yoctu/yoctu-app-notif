package com.yoctu.notif.android.yoctuappnotif

/**
 * Created on 26.03.18.
 */

interface BasePresenter<T> {
    /**
     * Binds presenter with a view when resumed. The presenter will perform initialization here.
     *
     * @param view the view associated with the presenter
     */
    fun takeView(view : T)

    /**
     * Drops the reference to the view when destroyed
     */
    fun dropView()

}