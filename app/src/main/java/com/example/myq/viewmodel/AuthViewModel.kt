package com.example.myq.viewmodel

import android.app.PendingIntent
import android.content.Context
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myq.data.auth.FirebaseAuthManager
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel : ViewModel() {
    sealed class AuthState {
        object Unauthenticated : AuthState()
        object Authenticated : AuthState()
        object Loading : AuthState()
        data class Error(val message: String) : AuthState()
    }

    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
    val authState: StateFlow<AuthState> = _authState

    init {
        viewModelScope.launch {
            if (FirebaseAuthManager.getCurrentUser() != null) {
                _authState.value = AuthState.Authenticated
            } else {
                _authState.value = AuthState.Unauthenticated
            }
        }
    }


    fun startGoogleSignIn(context: Context, launcher: ActivityResultLauncher<IntentSenderRequest>) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val client = FirebaseAuthManager.getGoogleSignInClient(context)
                // Mendapatkan PendingIntent dari GoogleSignInClient
                val signInTask = client.signInIntent
                val pendingIntent = PendingIntent.getActivity(
                    context,
                    0,
                    signInTask,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                // Membuat IntentSenderRequest dengan PendingIntent
                launcher.launch(IntentSenderRequest.Builder(pendingIntent).build())
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Gagal memulai Google Sign-In: ${e.message}")
                Log.e("AuthViewModel", "Kesalahan memulai Google Sign-In", e)
            }
        }
    }

    fun handleGoogleSignInResult(task: Task<GoogleSignInAccount>, onError: (String?) -> Unit) {
        viewModelScope.launch {
            try {
                val account = task.getResult(ApiException::class.java)
                val result = FirebaseAuthManager.signInWithGoogle(account)
                if (result.isSuccess) {
                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value = AuthState.Error("Gagal masuk dengan Google")
                    onError("Gagal masuk dengan Google")
                }
            } catch (e: ApiException) {
                _authState.value = AuthState.Error("Gagal masuk dengan Google: ${e.message}")
                onError("Gagal masuk dengan Google: ${e.message}")
                Log.e("AuthViewModel", "Google Sign-In gagal", e)
            }
        }
    }

    fun signOut(context: Context) {
        viewModelScope.launch {
            FirebaseAuthManager.signOut(context)
            _authState.value = AuthState.Unauthenticated
        }
    }
}