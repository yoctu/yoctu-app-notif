package com.yoctu.notif.android.yoctuappnotif.ui.login

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.firebase.ui.auth.AuthUI
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.with
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId
import com.yoctu.notif.android.yoctuappnotif.R
import com.yoctu.notif.android.yoctuappnotif.YoctuApplication
import com.yoctu.notif.android.yoctuappnotif.callback.CallbackBroadcast
import com.yoctu.notif.android.yoctuappnotif.callback.CallbackNavBack
import com.yoctu.notif.android.yoctuappnotif.managers.ManageGoogleSignin
import com.yoctu.notif.android.yoctuappnotif.ui.adapters.YoctuAdapter
import com.yoctu.notif.android.yoctuappnotif.utils.BroadcastUtils
import com.yoctu.notif.android.yoctuappnotif.utils.IntentUtils
import com.yoctu.notif.android.yoctuappnotif.utils.NotificationUtils
import com.yoctu.notif.android.yoctuappnotif.utils.YoctuUtils
import com.yoctu.notif.android.yoctulibrary.models.User
import com.yoctu.notif.android.yoctulibrary.models.ViewType
import kotlinx.android.synthetic.main.default_toolbar.*
import kotlinx.android.synthetic.main.fragment_login.*

/**
 * Created on 26.03.18.
 */

class LoginFragment :
        Fragment(),
        LoginContract.View,
        CallbackBroadcast {

    private var loginPresenter: LoginContract.Presenter? = null
    private var dialog: Dialog? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: YoctuAdapter
    private var managerGoogleSignIn: ManageGoogleSignin? = null
    private var deviceId: String? = null
    private lateinit var toolbar: Toolbar
    //indicates if user taps on sign out
    private var isSignout: Boolean = false
    //when user clicks on sign in button
    private var mustRedirectToNoti = false

    companion object {
        fun newInstance(signout: Boolean): LoginFragment {
            var args = Bundle()
            args.putBoolean(LoginActivity.KEY_SIGN_OUT, signout)
            var frg = LoginFragment()
            frg.arguments = args
            return frg
        }

        fun newInstance() = LoginFragment()
    }

    private var callbackNav: CallbackNavBack? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        val activity = context as AppCompatActivity
        callbackNav = activity as CallbackNavBack
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        retainInstance = true

        if (arguments != null && arguments!!.containsKey(LoginActivity.KEY_SIGN_OUT)) {
            isSignout = arguments!!.getBoolean(LoginActivity.KEY_SIGN_OUT)
            arguments!!.remove(LoginActivity.KEY_SIGN_OUT)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_login, container, false)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.onViewCreated(view!!, savedInstanceState)

        manageToolbar()

        loginPresenter = YoctuApplication.kodein.with(activity).instance()
        loginPresenter!!.takeView(this)

        manageView()
        manageConnectivity()
    }


    override fun onStart() {
        super.onStart()

        manageBroadcast()
    }

    override fun onResume() {
        super.onResume()
    }

    /**
     * Manage toolbar : tilte and back navigation
     */
    private fun manageToolbar() {
        toolbar_login_fragment?.let {
            toolbar = toolbar_login_fragment
            (activity as AppCompatActivity).setSupportActionBar(toolbar)

            YoctuUtils.changeToolbarColor(toolbar, activity!!.resources.getColor(R.color.colorPrimaryNoActionBar))

            toolbar_standard_back_nav?.let {
                toolbar_standard_back_nav.visibility = View.GONE
                toolbar_standard_back_nav.setOnClickListener { _ ->
                    callbackNav?.let { callbackNav!!.goBack() }
                }
            }

        }
    }


    /**
     * This function allows to register to local broadcast receiver
     * and register the fragment as call back
     */
    private fun manageBroadcast() {
        LocalBroadcastManager.getInstance(activity!!)
                .registerReceiver(BroadcastUtils.mNotificationReceiver,
                        IntentFilter(YoctuUtils.INTENT_FILTER_FCM))

        BroadcastUtils.registerLoginFragment(this)
        BroadcastUtils.registerNotifLoginFragment(this)
    }


    /**
     * check connectivity before ask a request to presenter
     * check if device_id exists to get channels (case: not he first time)
     */
    private fun manageConnectivity() {
        //get channels
        if (YoctuUtils.checkConnectivity(activity!!)) {
            if (deviceId != null) {
                loginPresenter?.let {
                    //loginPresenter!!.askChannels()
                    googleSignIn()
                }
            }
        } else {
            login_fragment_main_linear_layout?.let {
                YoctuUtils.displaySnackBar(login_fragment_main_linear_layout, activity!!.getString(R.string.app_txt_connection_disable))
                if (dialog == null) {
                    dialog = YoctuUtils.dialogConnectivity(activity!!)
                    manageDialog()
                }

                dialog!!.show()
            }
        }
    }

    override fun getMessage(message: String) {
        val obj = YoctuUtils.getNotificationFromJson(message)
        Log.d(YoctuUtils.TAG_DEBUG, " FCM message(login) ".plus(obj.title).plus(" ").plus(obj.body))
        activity?.let {
            NotificationUtils.createNotification(activity!!, obj.title, obj.body)
            loginPresenter?.let {
                loginPresenter!!.saveMessage(obj)
            }
        }
    }

    /**
     * Get token from FCM
     * case : - first time (user not existed)
     *        - update (user exits)
     *
     * @param token
     */
    override fun getToken(token: String) {
        deviceId = token

        loginPresenter?.let {
            var user = loginPresenter!!.getUser()
            if (user != null) { //update
                user.firebaseToken = token
                loginPresenter!!.saveUserInLocal(user)
                loginPresenter!!.sendDeviceId()
            } else { //first time
                loginPresenter!!.askChannels()
            }
        }
    }

    /**
     * Manage views, initialise @ManageGoogleSignin and get token from firebase
     */
    private fun manageView() {
        managerGoogleSignIn = YoctuApplication.kodein.with(activity).instance()
        deviceId = FirebaseInstanceId.getInstance().token
        manageViews()
        manageRecyclerView()
    }

    /**
     * Manage events on views into the fragment : register button
     * manage button google sign in
     */
    private fun manageViews() {
        login_fragment_register?.let {
            login_fragment_register.setOnClickListener { _ ->
                adapter.let {
                    // register channels
                    if (adapter.getChosenChannels().size == 0)
                        YoctuUtils.displaySnackBar(login_fragment_register, activity!!.getString(R.string.login_fragment_there_are_not_chosen))
                    else
                        loginPresenter?.let { loginPresenter!!.saveChannels(adapter.getChosenChannels()) }
                }
            }
        }

        login_sign_in_button?.let {
            login_sign_in_button.setOnClickListener { _ ->
                isSignout = false
                mustRedirectToNoti = true
                googleSignIn()
            }
        }

    }

    private fun manageRecyclerView() {
        recyclerView = login_fragment_recycler_view
        recyclerView?.let {
            adapter = YoctuAdapter(activity!!)
            var layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity!!)
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
            buttonEnable.setOnClickListener { _ ->
                YoctuUtils.closeDialog(dialog!!)
                IntentUtils.openWifiSettings(activity!!)
            }
            buttonCancel?.let {
                buttonCancel.setOnClickListener { _ -> YoctuUtils.closeDialog(dialog!!) }
            }
        }
    }


    /**
     * populate the list here
     * show or hide button and progress bar
     */
    override fun getChannels(list: ArrayList<ViewType>) {

        recyclerView?.let {
            if (list.size == 0) {
                login_fragment_empty_list?.let { login_fragment_empty_list.visibility = View.VISIBLE }
                login_fragment_register?.let { login_fragment_register.visibility = View.GONE }
            } else {
                login_fragment_empty_list?.let { login_fragment_empty_list.visibility = View.GONE }
                adapter?.let { adapter.addItems(list) }
                login_fragment_register?.let { login_fragment_register.visibility = View.VISIBLE }
            }
        }
    }

    override fun hideProgressBar() {
        login_fragment_progress_bar?.let { login_fragment_progress_bar!!.visibility = View.GONE }
    }

    /**
     * Check if user is not in shared preferences
     * we launch getLocalGoogleEmailAddress sign in and show google text + hide progress bar text
     * check if there is a sign out
     *
     * called after get channels and by google sign in button
     */
    override fun googleSignIn() {
        login_fragment_text_loading?.let { login_fragment_text_loading.visibility = View.GONE }
        login_fragment_sign_in_btn?.let { login_fragment_sign_in_btn.visibility = View.GONE }
        if (isSignout) {
            login_fragment_progress_bar?.let { login_fragment_progress_bar.visibility = View.GONE }
            login_fragment_sign_in_btn?.let { login_fragment_sign_in_btn.visibility = View.VISIBLE }
        } else {
            loginPresenter?.let {
                val currentUser = loginPresenter!!.getUser()
                if (currentUser == null) {
                    /*login_fragment_text_google_sign_in?.let {
                        login_fragment_text_google_sign_in.visibility = View.VISIBLE
                    }*/
                    login_fragment_progress_bar?.let { login_fragment_progress_bar.visibility = View.GONE }
                    launchGoogleSignIn()
                } else { //show channels
                    //loginPresenter!!.showChannels()
                }
            }
        }
    }

    /**
     * This function allows launch intent for google sign in
     */
    private fun launchGoogleSignIn() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(managerGoogleSignIn!!.providers)
                        .build(),
                YoctuUtils.CODE_GOOGLE_SIGN_IN)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == YoctuUtils.CODE_GOOGLE_SIGN_IN && resultCode == Activity.RESULT_OK) {
            val googleUser = FirebaseAuth.getInstance().currentUser
            var user = User()
            user.email = googleUser!!.email!!
            user.firebaseToken = deviceId!!
            Log.d(YoctuUtils.TAG_DEBUG, " ** user created : ".plus(user))
            loginPresenter?.let {
                login_fragment_text_google_sign_in?.let {
                    login_fragment_text_google_sign_in.visibility = View.GONE
                }
                loginPresenter!!.saveUserInLocal(user)
                loginPresenter!!.sendDeviceId()
                if (mustRedirectToNoti) {
                    mustRedirectToNoti = false
                    loginPresenter!!.gotoNotifications()
                    login_sign_in_button?.let { login_sign_in_button.visibility = View.VISIBLE }
                } else {
                    loginPresenter!!.showChannels()
                }
            }

        }

        if (resultCode == Activity.RESULT_OK && requestCode == YoctuUtils.CODE_GET_ACCOUNTS) {
            googleSignIn()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}