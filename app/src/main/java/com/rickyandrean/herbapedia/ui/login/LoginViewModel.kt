package com.rickyandrean.herbapedia.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rickyandrean.herbapedia.model.Authentication
import com.rickyandrean.herbapedia.model.LoginRequest
import com.rickyandrean.herbapedia.model.LoginResponse
import com.rickyandrean.herbapedia.network.ApiConfig
import com.rickyandrean.herbapedia.storage.AuthenticationPreference
import com.rickyandrean.herbapedia.ui.main.MainActivity
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val preference: AuthenticationPreference): ViewModel() {
    private val _loading = MutableLiveData<Boolean>()
    private val _loginAccess = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading
    val loginAccess: LiveData<Boolean> = _loginAccess

    init {
        _loading.value = false
        _loginAccess.value = false
    }

    fun login(email: String, password: String) {
        _loading.value = true

        val client = ApiConfig.getApiService().login("application/json", LoginRequest(email, password))
        client.enqueue(object: Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                _loading.value = false

                val responseBody = response.body()!!

                if (response.isSuccessful) {
                    if (responseBody.error == "") {
                        Log.d(TAG, responseBody.success)

                        viewModelScope.launch {
                            preference.login(
                                Authentication(
                                    responseBody.name.toString(),
                                    responseBody.accessToken.toString()
                                )
                            )
                        }

                        MainActivity.token = responseBody.accessToken.toString()
                        _loginAccess.value = true
                    } else {
                        Log.d(TAG, responseBody.error)
                    }
                } else {
                    Log.d(TAG, "Error occured!")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _loading.value = false
                Log.e(TAG, "Error occured!")
            }
        })
    }

    companion object {
        private const val TAG = "LoginViewModel"
    }
}