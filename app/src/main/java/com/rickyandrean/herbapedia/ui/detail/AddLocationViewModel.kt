package com.rickyandrean.herbapedia.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.rickyandrean.herbapedia.model.AddLocationRequest
import com.rickyandrean.herbapedia.model.AddLocationResponse
import com.rickyandrean.herbapedia.network.ApiConfig
import com.rickyandrean.herbapedia.storage.Global
import com.rickyandrean.herbapedia.ui.main.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddLocationViewModel: ViewModel() {
    private val _addStatus = MutableLiveData<Boolean>()
    private val _error = MutableLiveData<String>()
    val addStatus: LiveData<Boolean> = _addStatus
    val error: LiveData<String> = _error

    init {
        _addStatus.value = false
        _error.value = ""
    }

    fun addLocation(location: LatLng, description: String){
        val addLocationRequest = AddLocationRequest(location.latitude, location.longitude, Global.PLANT_ID, description)

        val client = ApiConfig.getApiService().addPlantLocation("application/json", "Bearer ${MainActivity.token}", addLocationRequest)
        client.enqueue(object: Callback<AddLocationResponse> {
            override fun onResponse(call: Call<AddLocationResponse>, response: Response<AddLocationResponse>) {
                if(response.isSuccessful) {
                    if (response.body()!!.error == "") {
                        _addStatus.value = true
                    } else {
                        _error.value = response.body()!!.error
                    }
                } else {
                    _error.value = response.body()!!.error
                }
            }

            override fun onFailure(call: Call<AddLocationResponse>, t: Throwable) {
                _error.value = "Failed to add location"
            }
        })
    }
}