package com.yoctu.notif.android.yoctuappnotif.ui.topic

import android.content.Context

/**
 * Created by gael on 16.05.18.
 */

class TopicPresenter(context: Context): TopicContract.Presenter {
    private var mContext : Context
    private var mView : TopicContract.View? = null
    init {
        mContext = context
    }

    override fun takeView(view: TopicContract.View) {
        this.mView = view
    }

    override fun dropView() {
        this.mView = null
    }
}