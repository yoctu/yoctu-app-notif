package com.yoctu.notif.android.yoctuappnotif.ui.add

import android.content.Context

/**
 * Created by gael on 16.05.18.
 */

class AddTopicURLPresenter(context: Context): AddTopicURLContract.Presenter {
    private var mContext : Context
    private var mView : AddTopicURLContract.View? = null
    init {
        mContext = context
    }

    override fun takeView(view: AddTopicURLContract.View) {
        this.mView = view
    }

    override fun dropView() {
        this.mView = null
    }
}