package com.rickyandrean.herbapedia.ui.maps

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.rickyandrean.herbapedia.R
import com.rickyandrean.herbapedia.databinding.ActivityMapsBinding
import com.rickyandrean.herbapedia.model.PlantsLocationsItem
import com.rickyandrean.herbapedia.storage.Global
import com.rickyandrean.herbapedia.ui.detail.DetailActivity

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

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
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

        PLANT = mapsViewModel.plantLocation.value!!.locations[index]
        LocationDetailFragment().show(supportFragmentManager, LocationDetailFragment::class.java.simpleName)

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
        var PLANT: PlantsLocationsItem? = null
    }
}