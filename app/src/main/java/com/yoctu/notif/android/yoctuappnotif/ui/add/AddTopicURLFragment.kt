package com.yoctu.notif.android.yoctuappnotif.ui.add

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
import com.yoctu.notif.android.yoctuappnotif.ui.adapters.YoctuAdapter
import com.yoctu.notif.android.yoctuappnotif.utils.YoctuUtils
import kotlinx.android.synthetic.main.default_toolbar.*
import kotlinx.android.synthetic.main.fragment_add_topic_url_fragment.*

/**
 * Created by gael on 16.05.18.
 */

class AddTopicURLFragment: Fragment() {

    companion object {

        fun newInstance() = AddTopicURLFragment()
    }
    private var addPresenter : AddTopicURLContract.Presenter? = null
    private lateinit var toolbar : Toolbar
    private lateinit var recyclerView : RecyclerView
    private lateinit var adapter : YoctuAdapter

    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun getLayoutResId() = R.layout.fragment_add_topic_url_fragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutResId(),container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addPresenter = YoctuApplication.kodein.with(activity).instance()
        manageToolbar()
    }

    private fun manageToolbar() {
        add_topic_url_fragment_toolbar?.let { t ->
            toolbar = t
            (activity as AppCompatActivity).setSupportActionBar(toolbar)

            YoctuUtils.changeToolbarColor(toolbar,activity!!.resources.getColor(R.color.colorPrimaryNoActionBar))
            toolbar_standard_title?.let {
                toolbar_standard_title.text = (activity as AppCompatActivity).getString(R.string.add_opic_url_view_title)
            }

            toolbar_standard_back_nav?.let {
                toolbar_standard_back_nav.visibility = View.GONE
                //callbackNav?.let { toolbar_standard_back_nav.setOnClickListener { _ -> callbackNav!!.goBack() } }
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
    }
}