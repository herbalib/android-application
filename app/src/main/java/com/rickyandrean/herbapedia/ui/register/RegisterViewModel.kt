package com.rickyandrean.herbapedia.ui.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rickyandrean.herbapedia.model.RegisterRequest
import com.rickyandrean.herbapedia.model.RegisterResponse
import com.rickyandrean.herbapedia.network.ApiConfig
import com.rickyandrean.herbapedia.ui.login.LoginViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel: ViewModel() {
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    init {
        _loading.value = false
    }

    fun register(name: String, email: String, password: String) {
        _loading.value = true

        val client = ApiConfig.getApiService().register("application/json", RegisterRequest(name, email, password))
        client.enqueue(object: Callback<RegisterResponse>{
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                _loading.value = false

                if(response.isSuccessful) {
                    if (response.body()!!.error == "") {
                        Log.d(TAG, response.body()!!.success)
                    } else {
                        Log.d(TAG, response.body()!!.error)
                    }
                } else {
                    Log.e(TAG, "Error occured!")
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _loading.value = false
                Log.e(TAG, "Error occured!")
            }
        })
    }

    companion object {
        private const val TAG = "RegisterViewModel"
    }
}