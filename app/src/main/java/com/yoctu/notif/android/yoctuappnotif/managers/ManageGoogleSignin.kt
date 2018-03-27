package com.yoctu.notif.android.yoctuappnotif.managers

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.yoctu.notif.android.yoctuappnotif.R

/**
 * Created on 27.03.18.
 */

class ManageGoogleSignin(context : Context) {

    private var mAuth: FirebaseAuth
    private var gso : GoogleSignInOptions
    private var mContext : Context
    private var mGoogleSignInClient : GoogleSignInClient
    init {
        mContext = context
        mAuth = FirebaseAuth.getInstance()
        gso = GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(mContext.getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(mContext,gso)
    }

    fun getCurrentUser() = mAuth.currentUser

    fun getGoogleSignInIntent() = mGoogleSignInClient.signInIntent

    fun googleSignOut() {
        FirebaseAuth.getInstance().signOut()
    }
}