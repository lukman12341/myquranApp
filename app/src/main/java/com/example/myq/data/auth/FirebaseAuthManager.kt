package com.example.myq.data.auth

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseUser

object FirebaseAuthManager {
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    fun getGoogleSignInClient(context: Context): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("961660361183-5cu66cmqlj4h961inieqaqrha5ivvdg1.apps.googleusercontent.com") // Ganti dengan Web Client ID Firebase Anda
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(context, gso)
    }

    suspend fun signInWithGoogle(account: GoogleSignInAccount): Result<FirebaseUser?> {
        return try {
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            val authResult = auth.signInWithCredential(credential).await()
            Result.success(authResult.user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getCurrentUser(): FirebaseUser? = auth.currentUser

    fun signOut(context: Context) {
        auth.signOut()
        getGoogleSignInClient(context).signOut()
    }
}