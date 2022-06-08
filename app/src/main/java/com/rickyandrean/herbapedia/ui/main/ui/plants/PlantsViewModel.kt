package com.rickyandrean.herbapedia.ui.main.ui.plants

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rickyandrean.herbapedia.model.PlantResponse
import com.rickyandrean.herbapedia.model.PlantsItem
import com.rickyandrean.herbapedia.network.ApiConfig
import com.rickyandrean.herbapedia.ui.main.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlantsViewModel : ViewModel() {
    private val _loading = MutableLiveData<Boolean>()
    private val _plant = MutableLiveData<PlantResponse>()

    val loading: LiveData<Boolean> = _loading
    val plant: LiveData<PlantResponse> = _plant
    val finish: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    init {
        finish.value = false
        loadPlant()
    }

    private fun loadPlant(){
        val client = ApiConfig.getApiService().plants("application/json", "Bearer ${MainActivity.token}")
        client.enqueue(object: Callback<PlantResponse> {
            override fun onResponse(call: Call<PlantResponse>, response: Response<PlantResponse>) {
                val responseBody = response.body()!!

                if (response.isSuccessful) {
                    if (responseBody.error == "") {
                        _plant.value = responseBody
                    } else {
                        Log.d(TAG, responseBody.error)
                    }
                } else {
                    Log.d(TAG, "Error occurred!")
                }
            }

            override fun onFailure(call: Call<PlantResponse>, t: Throwable) {
                Log.e(TAG, "Error occurred!")
            }
        })
    }

    fun searchPlant(keyword: String) {
        val client = ApiConfig.getApiService().searchPlant(keyword,"application/json","Bearer ${MainActivity.token}")
        client.enqueue(object: Callback<PlantResponse> {
            override fun onResponse(call: Call<PlantResponse>, response: Response<PlantResponse>) {
                val responseBody = response.body()!!

                if (response.isSuccessful) {
                    if (responseBody.error == "") {
                        _plant.value = responseBody
                    } else {
                        Log.d(TAG, responseBody.error)
                    }
                } else {
                    Log.d(TAG, "Error occurred!")
                }
            }

            override fun onFailure(call: Call<PlantResponse>, t: Throwable) {
                Log.e(TAG, "Error occurred!")
            }
        })
    }

    companion object {
        private const val TAG = "PlantsViewModel"
    }
}