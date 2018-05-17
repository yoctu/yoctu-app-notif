package com.yoctu.notif.android.yoctuappnotif.ui.add

import android.content.Context
import android.util.Log
import com.yoctu.notif.android.yoctuappnotif.R
import com.yoctu.notif.android.yoctuappnotif.utils.YoctuUtils

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

    override fun saveTopicURL(url: String) {
        if (!url.isEmpty() && YoctuUtils.isValidScheme(url)) {
            Log.d(YoctuUtils.TAG_DEBUG," TRUE ")
        } else {
            Log.d(YoctuUtils.TAG_DEBUG," FALSE ")
            mView?.let { v -> v.showErrorMessage(mContext.resources.getString(R.string.add_topic_url_error_message_not_valid)) }
        }
    }

    override fun dropView() {
        this.mView = null
    }
}