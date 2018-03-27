package com.yoctu.notif.android.yoctuappnotif.ui.login

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.salomonbrys.kodein.instance
import com.yoctu.notif.android.yoctuappnotif.R
import com.yoctu.notif.android.yoctuappnotif.YoctuApplication
import com.yoctu.notif.android.yoctuappnotif.ui.adapters.YoctuAdapter
import com.yoctu.notif.android.yoctuappnotif.utils.IntentUtils
import com.yoctu.notif.android.yoctuappnotif.utils.YoctuUtils
import com.yoctu.notif.android.yoctulibrary.models.Channel
import com.yoctu.notif.android.yoctulibrary.models.ViewType
import kotlinx.android.synthetic.main.dialog_connectivity.*
import kotlinx.android.synthetic.main.fragment_login.*

/**
 * Created on 26.03.18.
 */

class LoginFragment :
        Fragment(),
        LoginContract.View {

    private val loginPresenter : LoginContract.Presenter = YoctuApplication.kodein.instance()
    private var dialog : Dialog? = null
    private lateinit var recyclerView : RecyclerView
    private lateinit var adapter : YoctuAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater!!.inflate(R.layout.fragment_login,container,false)
        return v
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        manageView()
    }

    override fun onStart() {
        super.onStart()

        manageConnectivity()
    }

    private fun manageView() {
        manageViews()
        manageRecyclerView()
    }

    /**
     * Manage events on views into the fragment
     */
    private fun manageViews() {
        login_fragment_register?.let {
            login_fragment_register.setOnClickListener { _ ->
                adapter.let {
                    if(adapter.getChosenChannels().size == 0)
                        YoctuUtils.displaySnackBar(login_fragment_register,activity!!.getString(R.string.login_fragment_there_are_not_chosen))
                    else
                        loginPresenter?.let { loginPresenter.saveChannels(adapter.getChosenChannels()) }
                }
            }
        }
    }

    private fun manageRecyclerView() {
        recyclerView = login_fragment_recycler_view
        recyclerView?.let {
            adapter = YoctuAdapter(activity!!)
            var layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(activity!!)
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = adapter
        }
    }

    /**
     * Manage events on views into the dialog
     */
    private fun manageDialog() {
        val buttonEnable = dialog!!.findViewById<TextView>(R.id.dialog_connectivity_enable)
        val buttonCancel = dialog!!.findViewById<TextView>(R.id.dialog_connectivity_cancel)
        buttonEnable?.let {
            buttonEnable.setOnClickListener { v ->
                YoctuUtils.closeDialog(dialog!!)
                IntentUtils.openWifiSettings(activity!!)
            }
            buttonCancel?.let {
                buttonCancel.setOnClickListener { v -> YoctuUtils.closeDialog(dialog!!) }
            }
        }
    }

    /**
     * check connectivity before ask a request to presenter
     */
    private fun manageConnectivity() {
        //get channels
        if (YoctuUtils.checkConnectivity(activity!!))
            loginPresenter?.let { loginPresenter!!.askChannels() }
        else {
            login_fragment_main_linear_layout?.let {
                YoctuUtils.displaySnackBar(login_fragment_main_linear_layout,activity!!.getString(R.string.app_txt_connection_disable))
                if(dialog == null) {
                    dialog = YoctuUtils.dialogConnectivity(activity!!)
                    manageDialog()
                }


                dialog!!.show()
            }
        }
    }


    /**
     * populate the list here
     */
    override fun getChannels(list : ArrayList<ViewType>) {
        recyclerView?.let {
            if(list.size == 0)
                login_fragment_empty_list.visibility = View.VISIBLE
            else {
                login_fragment_empty_list.visibility = View.GONE
                adapter?.let { adapter.addItems(list) }
            }
        }
    }

    override fun hideProgressBar() {
        login_fragment_progress_bar?.let { login_fragment_progress_bar!!.visibility = View.GONE }
    }

    /**
     * Uses to binds the view with its presenter !
     */
    override fun onResume() {
        super.onResume()

        //binds the view
        loginPresenter.takeView(this)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}