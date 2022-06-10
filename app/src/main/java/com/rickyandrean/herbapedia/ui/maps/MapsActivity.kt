package com.rickyandrean.herbapedia.ui.maps

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.rickyandrean.herbapedia.R
import com.rickyandrean.herbapedia.databinding.ActivityMapsBinding
import com.rickyandrean.herbapedia.model.PlantsLocationsItem

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val mapsViewModel: MapsViewModel by viewModels()

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }
        supportActionBar?.hide()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()

        mapsViewModel.plantLocation.observe(this) {
            if (it != null) {
                val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
                mapFragment.getMapAsync(this)
            }
        }
    }

    private fun setMapStyle() {
        try {
            val success = mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map))
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", exception)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        setMapStyle()
        getMyLocation()

        with(mMap.uiSettings) {
            isCompassEnabled = true
            isMapToolbarEnabled = true
        }

        if (mapsViewModel.plantLocation.value != null) {
            val locations = mapsViewModel.plantLocation.value!!.locations
            locations.forEachIndexed { index, lct ->
                val coordinate = LatLng(lct.lat, lct.lon)
                mMap.addMarker(
                    MarkerOptions()
                        .position(coordinate)
                        .alpha(0.7F)
                )?.tag = index

                if (index == locations.size - 1) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 15f))
                }
            }
        }

        mMap.setOnMarkerClickListener(this)
    }

    override fun onMarkerClick(m: Marker): Boolean {
        val index = m.tag.toString().toInt()

        val args = Bundle()
        args.putString(LocationDetailFragment.TYPE, "map")
        args.putString(LocationDetailFragment.ID, mapsViewModel.plantLocation.value!!.locations[index].plantId.toString())
        args.putString(LocationDetailFragment.NAME, mapsViewModel.plantLocation.value!!.locations[index].name)
        args.putString(LocationDetailFragment.DESCRIPTION, mapsViewModel.plantLocation.value!!.locations[index].description)

        LocationDetailFragment().apply {
            arguments = args
            show(supportFragmentManager, LocationDetailFragment::class.java.simpleName)
        }

        return true
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    companion object {
        const val TAG = "MapsActivity"
    }
}