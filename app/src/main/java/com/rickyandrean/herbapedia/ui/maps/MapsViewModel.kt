package com.rickyandrean.herbapedia.ui.maps

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rickyandrean.herbapedia.model.PlantsLocationResponse
import com.rickyandrean.herbapedia.network.ApiConfig
import com.rickyandrean.herbapedia.ui.main.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsViewModel: ViewModel() {
    private val _plantLocation = MutableLiveData<PlantsLocationResponse>()

    val plantLocation: LiveData<PlantsLocationResponse> = _plantLocation

    init {
        loadLocation()
    }

    private fun loadLocation() {
        val client = ApiConfig.getApiService().getPlantsLocation(MainActivity.lat, MainActivity.lon, "Bearer ${MainActivity.token}")
        client.enqueue(object: Callback<PlantsLocationResponse> {
            override fun onResponse(call: Call<PlantsLocationResponse>, response: Response<PlantsLocationResponse>) {
                val responseBody = response.body()!!

                if (response.isSuccessful) {
                    if (responseBody.error == "") {
                        _plantLocation.value = responseBody
                    } else {
                        Log.d(TAG, responseBody.error)
                    }
                } else {
                    Log.d(TAG, "Error occurred!")
                }
            }

            override fun onFailure(call: Call<PlantsLocationResponse>, t: Throwable) {
                Log.e(TAG, "Error occurred!")
            }
        })
    }

    companion object {
        const val TAG = "MapsViewModel"
    }
}