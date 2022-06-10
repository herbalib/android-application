package com.rickyandrean.herbapedia.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.rickyandrean.herbapedia.model.AddLocationRequest
import com.rickyandrean.herbapedia.model.AddLocationResponse
import com.rickyandrean.herbapedia.model.PlantResponse
import com.rickyandrean.herbapedia.network.ApiConfig
import com.rickyandrean.herbapedia.ui.main.MainActivity
import com.rickyandrean.herbapedia.ui.register.RegisterViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel: ViewModel() {
    private val _loading = MutableLiveData<Boolean>()
    private val _plant = MutableLiveData<PlantResponse>()

    val loading: LiveData<Boolean> = _loading
    val plant: LiveData<PlantResponse> = _plant

    fun loadPlant(id: Int) {
        val client = ApiConfig.getApiService().detailPlant(id.toString(), MainActivity.lat, MainActivity.lon, "application/json", "Bearer ${MainActivity.token}")
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
        private const val TAG = "DetailViewModel"
    }
}