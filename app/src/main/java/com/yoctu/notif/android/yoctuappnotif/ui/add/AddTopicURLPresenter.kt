package com.yoctu.notif.android.yoctuappnotif.ui.add

import android.content.Context

/**
 * Created by gael on 16.05.18.
 */

class AddTopicPresenter(context: Context): AddTopicContract.Presenter {
    private var mContext : Context
    private var mView : AddTopicContract.View? = null
    init {
        mContext = context
    }

    override fun takeView(view: AddTopicContract.View) {
        this.mView = view
    }

    override fun dropView() {
        this.mView = null
    }
}