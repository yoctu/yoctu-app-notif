package com.yoctu.notif.android.yoctuappnotif.ui.login

import android.app.Activity
import android.app.Dialog
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
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.with
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.iid.FirebaseInstanceId
import com.yoctu.notif.android.yoctuappnotif.R
import com.yoctu.notif.android.yoctuappnotif.YoctuApplication
import com.yoctu.notif.android.yoctuappnotif.callback.CallbackBroadcast
import com.yoctu.notif.android.yoctuappnotif.managers.ManageGoogleSignin
import com.yoctu.notif.android.yoctuappnotif.ui.adapters.YoctuAdapter
import com.yoctu.notif.android.yoctuappnotif.utils.BroadcastUtils
import com.yoctu.notif.android.yoctuappnotif.utils.IntentUtils
import com.yoctu.notif.android.yoctuappnotif.utils.YoctuUtils
import com.yoctu.notif.android.yoctulibrary.models.User
import com.yoctu.notif.android.yoctulibrary.models.ViewType
import kotlinx.android.synthetic.main.dialog_connectivity.*
import kotlinx.android.synthetic.main.fragment_login.*

/**
 * Created on 26.03.18.
 */

class LoginFragment :
        Fragment(),
        LoginContract.View,
        CallbackBroadcast {

    private var loginPresenter : LoginContract.Presenter? = null
    private var dialog : Dialog? = null
    private lateinit var recyclerView : RecyclerView
    private lateinit var adapter : YoctuAdapter
    private var managerGoogleSignIn : ManageGoogleSignin? = null
    private var deviceId : String? = null
    private lateinit var toolbar : Toolbar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater!!.inflate(R.layout.fragment_login,container,false)
        return v
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        manageToolbar()
        loginPresenter = YoctuApplication.kodein.with(activity).instance()
        manageView()
    }

    override fun onStart() {
        super.onStart()

        manageBroadcast()
        manageConnectivity()
    }

    /**
     * Uses to binds the view with its presenter !
     */
    override fun onResume() {
        super.onResume()

        //binds the view
        loginPresenter!!.takeView(this)
    }

    private fun manageToolbar() {
        toolbar_login_fragment?.let {
            toolbar = toolbar_login_fragment
            (activity as AppCompatActivity).setSupportActionBar(toolbar)

            YoctuUtils.changeToolbarColor(toolbar,activity!!.resources.getColor(R.color.colorPrimaryNoActionBar))
        }
    }


    /**
     * This function allows to register to local broadcast receiver
     * and register the activity as call back
     */
    private fun manageBroadcast() {
        LocalBroadcastManager.getInstance(activity)
                .registerReceiver(BroadcastUtils.mNotificationReceiver,
                        IntentFilter(YoctuUtils.INTENT_FILTER_FCM))

        BroadcastUtils.registerLoginFragment(this)
    }


    /**
     * check connectivity before ask a request to presenter
     * check if device_id exists to get channels (case: not he first time)
     */
    private fun manageConnectivity() {
        //get channels
        if (YoctuUtils.checkConnectivity(activity!!)) {
            //loginPresenter?.let { loginPresenter!!.askChannels() }
            if (deviceId != null) {
                loginPresenter?.let { loginPresenter!!.askChannels() }
            }
        } else {
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

    override fun getMessage(message: String) {}

    /**
     * Get token from FCM
     * case : - first time (user not existed)
     *        - update (user exits)
     */
    override fun getToken(token: String) {
        deviceId = token

        loginPresenter?.let {
            var user = loginPresenter!!.getUser()
            if (user != null) { //update
                user.firebaseToken = token
                //TODO save usr local + server
                loginPresenter!!.saveUserInLocal(user)
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
     * Manage events on views into the fragment
     */
    private fun manageViews() {
        login_fragment_register?.let {
            login_fragment_register.setOnClickListener { _ ->
                adapter.let {// register channels
                    if(adapter.getChosenChannels().size == 0)
                        YoctuUtils.displaySnackBar(login_fragment_register,activity!!.getString(R.string.login_fragment_there_are_not_chosen))
                    else
                        loginPresenter?.let { loginPresenter!!.saveChannels(adapter.getChosenChannels()) }
                }
            }
        }
    }

    private fun manageRecyclerView() {
        recyclerView = login_fragment_recycler_view
        recyclerView?.let {
            adapter = YoctuAdapter(activity)
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
    override fun getChannels(list : ArrayList<ViewType>) {
        //login_fragment_text_loading?.let { login_fragment_text_loading.visibility = View.GONE }

        recyclerView?.let {
            if(list.size == 0) {
                login_fragment_empty_list.visibility = View.VISIBLE
                login_fragment_register?.let { login_fragment_register.visibility = View.GONE }
            } else {
                login_fragment_empty_list.visibility = View.GONE
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
     * we launch Google sign in and show google text + hide progress bar text
     */
    override fun googleSignIn() {
        login_fragment_text_loading?.let { login_fragment_text_loading.visibility = View.GONE }
        loginPresenter?.let {
            val currentUser = loginPresenter!!.getUser()
            if (currentUser == null) {
                login_fragment_text_google_sign_in?.let {
                    login_fragment_text_google_sign_in.visibility = View.VISIBLE
                }
                launchGoogleSignIn()
            } else { //show channels
                loginPresenter!!.showChannels()
            }
        }
    }

    /**
     * This function allows launch intent for google sign in
     */
    private fun launchGoogleSignIn() {
        startActivityForResult(managerGoogleSignIn!!.getGoogleSignInIntent(),YoctuUtils.CODE_GOOGLE_SIGN_IN)
    }

    /**
     * This function allows to get account from Google + hide google text
     */
    private fun handleGoogleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            val user = User()
            user.email = account.email!!
            user.firebaseToken = deviceId!!
            Log.d(YoctuUtils.TAG_DEBUG, "user from google ".plus(user))
            loginPresenter?.let {
                login_fragment_text_google_sign_in?.let {
                    login_fragment_text_google_sign_in.visibility = View.GONE
                }
                //TODO save usr local + server
                loginPresenter!!.saveUserInLocal(user)
                loginPresenter!!.showChannels()
            }
        } catch (e: ApiException) {
            e.printStackTrace()
            Log.e(YoctuUtils.TAG_ERROR, e.message)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == YoctuUtils.CODE_GOOGLE_SIGN_IN && resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleGoogleSignInResult(task)
        }
    }

    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(activity)
                .unregisterReceiver(BroadcastUtils.mNotificationReceiver)

    }

    override fun onDestroy() {
        super.onDestroy()
    }
}