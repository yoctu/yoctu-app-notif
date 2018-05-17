package com.yoctu.notif.android.yoctuappnotif.ui.add

import com.yoctu.notif.android.yoctuappnotif.BasePresenter
import com.yoctu.notif.android.yoctuappnotif.BaseView

/**
 * Created by gael on 16.05.18.
 */
interface AddTopicURLContract {
    interface View: BaseView<Presenter> {
        fun showErrorMessage(message: String)
    }
    interface Presenter: BasePresenter<View> {
        fun saveTopicURL(url: String)
    }
}