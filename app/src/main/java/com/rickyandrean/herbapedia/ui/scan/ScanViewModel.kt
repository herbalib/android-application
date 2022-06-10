package com.rickyandrean.herbapedia.ui.scan

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rickyandrean.herbapedia.helper.reduceFileImage
import com.rickyandrean.herbapedia.model.PredictPlantResponse
import com.rickyandrean.herbapedia.network.ApiConfig
import com.rickyandrean.herbapedia.storage.Global
import com.rickyandrean.herbapedia.ui.main.MainActivity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ScanViewModel: ViewModel() {
    private val _loading = MutableLiveData<Boolean>()
    private val _error = MutableLiveData<String>()
    private val _success = MutableLiveData<Boolean>()

    val loading: LiveData<Boolean> = _loading
    val error: LiveData<String> = _error
    val success: LiveData<Boolean> = _success
    val getFile = MutableLiveData<File>()

    init {
        _error.value = ""
        _success.value = false
    }

    fun uploadImage() {
        _loading.value = true

        val file = reduceFileImage(getFile.value!!)
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData("image", file.name, requestImageFile)

        val service = ApiConfig.getApiService().predictPlant("Bearer ${MainActivity.token}", imageMultipart)
        service.enqueue(object : Callback<PredictPlantResponse> {
            override fun onResponse(call: Call<PredictPlantResponse>, response: Response<PredictPlantResponse>) {
                _loading.value = false

                if (response.isSuccessful) {
                    val responseBody = response.body()!!

                    if (responseBody.error == "") {
                        Global.PLANT_ID = responseBody.plantId
                        _success.value = true
                    } else {
                        Log.d(TAG, responseBody.error)
                        _error.value = responseBody.error
                    }
                } else {
                    Log.d(TAG, "Failed to upload image")
                    _error.value = "Failed to upload image"
                }
            }

            override fun onFailure(call: Call<PredictPlantResponse>, t: Throwable) {
                _loading.value = false
                Log.d(TAG, "Failed to upload image")
                _error.value = "Failed to upload image"
            }
        })
    }

    companion object {
        const val TAG = "ScanViewModel"
    }
}