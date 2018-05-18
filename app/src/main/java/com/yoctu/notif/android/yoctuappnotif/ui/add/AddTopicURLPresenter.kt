package com.yoctu.notif.android.yoctuappnotif.ui.add

import android.content.Context
import android.util.Log
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.with
import com.yoctu.notif.android.yoctuappnotif.R
import com.yoctu.notif.android.yoctuappnotif.YoctuApplication
import com.yoctu.notif.android.yoctuappnotif.utils.YoctuUtils
import com.yoctu.notif.android.yoctulibrary.repository.manager.ManagerSharedPreferences

/**
 * Created by gael on 16.05.18.
 */

class AddTopicURLPresenter(context: Context): AddTopicURLContract.Presenter {
    private var mContext : Context
    private var mView : AddTopicURLContract.View? = null
    init {
        mContext = context
    }
    private var managerSharedPreferences: ManagerSharedPreferences = YoctuApplication.kodein.with(mContext.applicationContext).instance()

    override fun takeView(view: AddTopicURLContract.View) {
        this.mView = view
    }

    override fun saveTopicURL(url: String) {
        if (!url.isEmpty() && YoctuUtils.isValidScheme(url)) {
            Log.d(YoctuUtils.TAG_DEBUG," TRUE ")
            managerSharedPreferences.saveTopicURL(url)
        } else {
            Log.d(YoctuUtils.TAG_DEBUG," FALSE ")
            mView?.let { v -> v.showErrorMessage(mContext.resources.getString(R.string.add_topic_url_error_message_not_valid)) }
        }
    }

    override fun dropView() {
        this.mView = null
    }
}