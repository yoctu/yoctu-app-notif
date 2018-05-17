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
import android.widget.Toast
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.with
import com.yoctu.notif.android.yoctuappnotif.R
import com.yoctu.notif.android.yoctuappnotif.YoctuApplication
import com.yoctu.notif.android.yoctuappnotif.ui.adapters.YoctuAdapter
import com.yoctu.notif.android.yoctuappnotif.utils.YoctuUtils
import com.yoctu.notif.android.yoctulibrary.repository.manager.ManagerSharedPreferences
import kotlinx.android.synthetic.main.default_toolbar.*
import kotlinx.android.synthetic.main.fragment_add_topic_url_fragment.*

/**
 * Created by gael on 16.05.18.
 */

class AddTopicURLFragment:
        Fragment(),
        AddTopicURLContract.View {

    companion object {

        fun newInstance() = AddTopicURLFragment()
    }
    private var addPresenter : AddTopicURLContract.Presenter? = null
    private lateinit var toolbar : Toolbar
    private lateinit var recyclerView : RecyclerView
    private lateinit var adapter : YoctuAdapter
    private var currentTopicURL: String? = null
    private var managerSharedPreferences: ManagerSharedPreferences? = null

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

        activity?.let { act ->
            addPresenter = YoctuApplication.kodein.with(act).instance()
            addPresenter?.let { presenter -> presenter.takeView(this) }
            checkTopicURL(act)
        }
        manageToolbar()
        manageClickEvents()
        //manageView()
    }

    private fun checkTopicURL(context: Context) {
        managerSharedPreferences = YoctuApplication.kodein.with(context).instance()
        managerSharedPreferences?.let { shared ->
            currentTopicURL = shared.getTopicURL()

            if (currentTopicURL == null) {
                add_topic_url_linear_layout_horizontal?.let { container ->
                    container.visibility = View.GONE
                }
                add_topic_url_button?.let { button -> button.text = activity?.let { it.resources.getString(R.string.add_topic_url_current_url_save_new_url_btn) } }
            } else {
                add_topic_url_linear_layout_horizontal?.let { container ->
                    container.visibility = View.VISIBLE
                }
                add_topic_url_button?.let { button -> button.text = activity?.let { it.resources.getString(R.string.add_topic_url_current_url_replace_url_btn) } }
            }
        }
    }


    private fun manageToolbar() {
        add_topic_url_fragment_toolbar?.let { t ->
            toolbar = t
            (activity as AppCompatActivity).setSupportActionBar(toolbar)

            YoctuUtils.changeToolbarColor(toolbar,activity!!.resources.getColor(R.color.colorPrimaryNoActionBar))
            toolbar_standard_title?.let {
                toolbar_standard_title.text = (activity as AppCompatActivity).getString(R.string.add_topic_url_view_title)
            }

            toolbar_standard_back_nav?.let {
                toolbar_standard_back_nav.visibility = View.GONE
                //callbackNav?.let { toolbar_standard_back_nav.setOnClickListener { _ -> callbackNav!!.goBack() } }
            }
        }
    }

    private fun manageClickEvents() {
        //save/replace url
        add_topic_url_button?.let { btn ->
            btn?.setOnClickListener { _ ->
                addPresenter?.let { presenter ->
                    add_topic_url_fragment_edit_text?.let { url ->
                        presenter.saveTopicURL(url.text.toString())
                    }
                }
            }
        }
    }

    private fun manageView() {
        //simulate
        Thread.sleep(3000)
        add_topic_url_fragment_edit_text?.let { v ->
            addPresenter?.let { presenter ->
                presenter.saveTopicURL("http://notification.test.flash-global.net/api/channels")
            }
        }
    }

    override fun showErrorMessage(message: String) {
        activity?.let { act ->
            Toast.makeText(act,message,Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDetach() {
        super.onDetach()
    }
}