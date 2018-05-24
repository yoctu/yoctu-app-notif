package com.yoctu.notif.android.yoctuappnotif.ui.add

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.with
import com.yoctu.notif.android.yoctuappnotif.R
import com.yoctu.notif.android.yoctuappnotif.YoctuApplication
import com.yoctu.notif.android.yoctuappnotif.utils.CopyPastUtils
import com.yoctu.notif.android.yoctuappnotif.utils.KeyboardUtils
import com.yoctu.notif.android.yoctuappnotif.utils.YoctuUtils
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
    private var currentTopicURL: String? = null

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
        manageView()
    }

    private fun checkTopicURL(activity: Activity) {
        addPresenter?.let { presenter ->
            currentTopicURL = presenter.getTopicURL()
            if (currentTopicURL == null) {
                add_topic_url_linear_layout_horizontal?.let { container ->
                    container.visibility = View.GONE
                }
                add_topic_url_button?.let { button -> button.text = activity?.let { it.resources.getString(R.string.add_topic_url_current_url_save_new_url_btn) } }
            } else {
                add_topic_url_fragment_current_url?.let { v -> v.text = currentTopicURL }
                add_topic_url_linear_layout_horizontal?.let { container ->
                    container.visibility = View.VISIBLE
                }
                add_topic_url_button?.let { button -> button.text = activity?.let { it.resources.getString(R.string.add_topic_url_current_url_replace_url_btn) } }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        activity?.let { act ->
            checkTopicURL(act)
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
                        if (!url.toString().isEmpty()) {
                            add_topic_url_fragment_edit_text?.let { edit -> KeyboardUtils.hidesKeyboard(edit) }
                            presenter.saveTopicURL(url.text.toString())
                            url.setText("")
                        } else {
                            activity?.let { act ->
                                add_topic_url_fragment_edit_text?.let { edit -> YoctuUtils.displaySnackBar(edit,act.resources.getString(R.string.add_topic_url_empty_url)) }
                            }
                        }
                    }
                }
            }
        }

        //hide keyboard
        add_topic_url_fragment_principal_container?.let { v ->
            v.setOnTouchListener(object : View.OnTouchListener{
                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                    if (v !is EditText)
                        add_topic_url_fragment_edit_text?.let { edit -> KeyboardUtils.hidesKeyboard(edit) }
                    return false
                }
            })
        }

        //long press to copy text
        add_topic_url_fragment_current_url?.let { url ->
            url.setOnLongClickListener(object : View.OnLongClickListener {
                override fun onLongClick(v: View?): Boolean {
                    activity?.let { act ->
                        CopyPastUtils.copyText("current url",url.text.toString(),act)
                        YoctuUtils.displaySnackBar(url,act.resources.getString(R.string.add_topic_url_copy_current_url))
                    }
                    return true
                }
            })
        }
    }

    private fun manageView() {
    }

    override fun showErrorMessage(message: String) {
        activity?.let { act ->
            YoctuUtils.displaySnackBar(add_topic_url_button, message)
        }
    }

    override fun onDetach() {
        super.onDetach()
    }
}