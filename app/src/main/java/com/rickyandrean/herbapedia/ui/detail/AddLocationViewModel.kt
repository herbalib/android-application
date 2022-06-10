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
import com.rickyandrean.herbapedia.storage.Global
import com.rickyandrean.herbapedia.ui.main.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddLocationViewModel: ViewModel() {
    private val _addStatus = MutableLiveData<Boolean>()
    val addStatus: LiveData<Boolean> = _addStatus

    init {
        _addStatus.value = false
    }

    fun addLocation(location: LatLng, description: String){
        val addLocationRequest = AddLocationRequest(location.latitude, location.longitude, Global.PLANT_ID, description)

        val client = ApiConfig.getApiService().addPlantLocation("application/json", "Bearer ${MainActivity.token}", addLocationRequest)
        client.enqueue(object: Callback<AddLocationResponse> {
            override fun onResponse(call: Call<AddLocationResponse>, response: Response<AddLocationResponse>) {

                if(response.isSuccessful) {
                    if (response.body()!!.error == "") {
                        Log.d(TAG, response.body()!!.success)
                        _addStatus.value = true
                    } else {
                        Log.d(TAG, response.body()!!.error)
                    }
                } else {
                    Log.d(TAG, "Error occured!")
                }
            }

            override fun onFailure(call: Call<AddLocationResponse>, t: Throwable) {
                Log.e(TAG, "Error occured!")
            }
        })
    }

    companion object {
        private const val TAG = "AddLocationViewModel"
    }
}