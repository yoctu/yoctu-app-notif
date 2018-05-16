package com.yoctu.notif.android.yoctuappnotif.ui.topic

import com.yoctu.notif.android.yoctuappnotif.BasePresenter
import com.yoctu.notif.android.yoctuappnotif.BaseView

/**
 * Created by gael on 16.05.18.
 */

interface TopicContract {

    interface View: BaseView<Presenter> {}
    interface Presenter: BasePresenter<View> {}
}