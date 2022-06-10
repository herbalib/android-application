package com.rickyandrean.herbapedia.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rickyandrean.herbapedia.model.RegisterRequest
import com.rickyandrean.herbapedia.model.RegisterResponse
import com.rickyandrean.herbapedia.network.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel: ViewModel() {
    private val _loading = MutableLiveData<Boolean>()
    private val _message = MutableLiveData<String>()

    val loading: LiveData<Boolean> = _loading
    val message: LiveData<String> = _message

    init {
        _loading.value = false
        _message.value = ""
    }

    fun register(name: String, email: String, password: String) {
        _loading.value = true

        val client = ApiConfig.getApiService().register("application/json", RegisterRequest(name, email, password))
        client.enqueue(object: Callback<RegisterResponse>{
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                _loading.value = false

                if(response.isSuccessful) {
                    if (response.body()!!.error == "") {
                        _message.value = response.body()!!.success
                    } else {
                        _message.value = response.body()!!.error
                    }
                } else {
                    _message.value = response.body()!!.error
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _loading.value = false
                _message.value = "Registration Failed"
            }
        })
    }
}