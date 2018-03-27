package com.yoctu.notif.android.yoctuappnotif.ui.notification

import android.content.Context
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.with
import com.yoctu.notif.android.yoctuappnotif.YoctuApplication
import com.yoctu.notif.android.yoctuappnotif.repository.YoctuRepository

/**
 * Created on 27.03.18.
 */

class NotificationPresenter(context: Context): NotificationContract.Presenter {
    private var mContext : Context
    private var mView : NotificationContract.View? = null
    init {
        mContext = context
    }
    private var repository : YoctuRepository = YoctuApplication.kodein.with(mContext).instance()

    override fun takeView(view: NotificationContract.View) {
        mView = view
    }

    override fun dropView() {
        mView = null
    }
}