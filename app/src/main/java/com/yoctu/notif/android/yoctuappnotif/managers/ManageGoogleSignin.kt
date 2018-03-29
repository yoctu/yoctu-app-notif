package com.yoctu.notif.android.yoctuappnotif.managers

import android.app.Activity
import android.content.Context
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.yoctu.notif.android.yoctuappnotif.R

/**
 * Created on 27.03.18.
 */

class ManageGoogleSignin(context : Context) {

    private var mAuth: FirebaseAuth
    private var gso : GoogleSignInOptions
    private var mContext : Context
    private var mGoogleSignInClient : GoogleSignInClient
    var mGoogleApiClient : GoogleApiClient? = null
    init {
        mContext = context
        mAuth = FirebaseAuth.getInstance()
        gso = GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(mContext.getString(R.string.my_default_web_client_id))//.requestIdToken(mContext.getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(mContext,gso)

        mGoogleApiClient = GoogleApiClient
                .Builder(mContext)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build()
        mGoogleApiClient!!.connect()

    }

    var providers = arrayListOf<AuthUI.IdpConfig>(AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build())

    fun getCurrentUser() = mAuth.currentUser

    fun getGoogleSignInIntent() = mGoogleSignInClient.signInIntent

    fun getInstanceGoogleApiClient(activity: Activity)  {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = GoogleApiClient
                    .Builder(mContext)
                    .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                    .build()
        }

        if (!mGoogleApiClient!!.isConnected) {
            mGoogleApiClient!!.connect()
        }
    }


}