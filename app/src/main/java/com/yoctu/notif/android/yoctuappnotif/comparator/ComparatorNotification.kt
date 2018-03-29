package com.yoctu.notif.android.yoctuappnotif.comparator

import com.yoctu.notif.android.yoctulibrary.models.Notification
import com.yoctu.notif.android.yoctulibrary.models.ViewType
import java.util.*

/**
 * Created on 29.03.18.
 */

class ComparatorNotification {

    /*companion object: Comparator<Notification> {

        override fun compare(o1: Notification?, o2: Notification?): Int {
            val d1 = Date(o1!!.time)
            val d2 = Date(o2!!.time)

            if (d1.after(d2))
                return -1
            else
                return 1
        }
    }*/

        companion object: Comparator<ViewType> {

        override fun compare(o1: ViewType?, o2: ViewType?): Int {
            o1 as Notification
            o2 as Notification
            val d1 = Date(o1.time)
            val d2 = Date(o2.time)

            if (d1.after(d2))
                return -1
            else
                return 1
        }
    }
}