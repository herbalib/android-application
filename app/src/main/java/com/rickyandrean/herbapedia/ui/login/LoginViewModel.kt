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
    private val _error = MutableLiveData<String>()
    private val _loginAccess = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading
    val error: LiveData<String> = _error
    val loginAccess: LiveData<Boolean> = _loginAccess

    init {
        _loading.value = false
        _loginAccess.value = false
        _error.value = ""
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
                        viewModelScope.launch {
                            preference.login(
                                Authentication(
                                    responseBody.name.toString(),
                                    responseBody.accessToken.toString()
                                )
                            )
                        }

                        MainActivity.token = responseBody.accessToken.toString()
                        MainActivity.name = responseBody.name.toString()
                        _loginAccess.value = true
                    } else {
                        _error.value = responseBody.error
                    }
                } else {
                    _error.value = responseBody.error
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _loading.value = false
                _error.value = "Login Failed"
            }
        })
    }
}