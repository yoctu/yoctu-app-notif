package com.yoctu.notif.android.yoctuappnotif.ui.topic

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.with
import com.yoctu.notif.android.yoctuappnotif.R
import com.yoctu.notif.android.yoctuappnotif.YoctuApplication
import com.yoctu.notif.android.yoctuappnotif.callback.CallbackNavBack
import com.yoctu.notif.android.yoctuappnotif.ui.adapters.YoctuAdapter
import com.yoctu.notif.android.yoctuappnotif.utils.YoctuUtils
import com.yoctu.notif.android.yoctulibrary.repository.manager.ManagerSharedPreferences
import kotlinx.android.synthetic.main.default_toolbar.*
import kotlinx.android.synthetic.main.fragment_notification.*
import kotlinx.android.synthetic.main.topic_fragment.*

/**
 * Created by gael on 16.05.18.
 */

class TopicFragment: Fragment() {

    companion object {
         fun newInstance() = TopicFragment()
    }
    private var callbackNav : CallbackNavBack? = null
    private lateinit var toolbar : Toolbar
    private lateinit var recyclerView : RecyclerView
    private lateinit var adapter : YoctuAdapter

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        val activity = context as AppCompatActivity
        //callbackNav = activity as CallbackNavBack
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun getLayoutResId() = R.layout.topic_fragment
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutResId(),container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        manageToolbar()
        manageViews()
    }


    private fun manageToolbar() {
        topic_fragment_toolbar?.let { t ->
            toolbar = t
            (activity as AppCompatActivity).setSupportActionBar(toolbar)

            YoctuUtils.changeToolbarColor(toolbar,activity!!.resources.getColor(R.color.colorPrimaryNoActionBar))
            toolbar_standard_title?.let {
                toolbar_standard_title.text = (activity as AppCompatActivity).getString(R.string.topic_view_title)
            }

            toolbar_standard_back_nav?.let {
                toolbar_standard_back_nav.visibility = View.GONE
                callbackNav?.let { toolbar_standard_back_nav.setOnClickListener { _ -> callbackNav!!.goBack() } }
            }
        }
    }

    private fun manageViews() {

    }

    override fun onDetach() {
        super.onDetach()
    }
}