package com.rickyandrean.herbapedia.ui.detail

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.rickyandrean.herbapedia.R
import com.rickyandrean.herbapedia.databinding.ActivityDetailBinding
import com.rickyandrean.herbapedia.model.PlantsItem
import com.rickyandrean.herbapedia.storage.Global
import com.rickyandrean.herbapedia.ui.maps.LocationDetailFragment


class DetailActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel: DetailViewModel by viewModels()
    private var marker: Marker? = null
    private var location: LatLng? = null

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()

        detailViewModel.loadPlant(Global.PLANT_ID)
        detailViewModel.plant.observe(this) {
            updateScreen(it.plants[0])
            val mapFragment = supportFragmentManager.findFragmentById(R.id.map_detail) as SupportMapFragment
            mapFragment.getMapAsync(this)
        }
        detailViewModel.addStatus.observe(this){
            if (it) {
                val intent = Intent(this, DetailActivity::class.java)
                detailViewModel.loadPlant(Global.PLANT_ID)
                startActivity(intent)
                finish()
            }
        }

        binding.fabAddPin.setOnClickListener {
            val input = EditText(this)
            input.inputType = InputType.TYPE_CLASS_TEXT

            AlertDialog.Builder(this).apply {
                setTitle(detailViewModel.plant.value?.plants?.get(0)?.name)
                setView(input)
                setPositiveButton("OK") { _, _ ->
                    detailViewModel.addLocation(location!!, input.text.toString())
                }
                setNegativeButton("Cancel") {dialog, _ ->
                    dialog.cancel()
                }
                create()
                show()
            }
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
        binding.fabAddPin.imageTintList = ColorStateList.valueOf(Color.rgb(255, 255, 255))
    }

    private fun updateScreen(plant: PlantsItem) {
        with(binding.bottomSheetPlant) {
            tvBsPlantName.text = plant.name
            tvBsPlantLatin.text = plant.latinName
            Glide.with(this@DetailActivity)
                .load(plant.image)
                .transform(CenterInside(), RoundedCorners(16))
                .into(ivBsPlantImage)

            tvBsPlantDescription.text = plant.description

            var nutrition = "-"
            if (plant.nutritions.isNotEmpty()) {
                nutrition = "${plant.nutritions[0].name} \n"

                if (plant.nutritions.size > 1) {
                    nutrition += plant.nutritions[1].name
                }
            }
            tvBsPlantNutrientContent.text = nutrition

            var cure = "-"
            if (plant.benefits.isNotEmpty()) {
                cure = "${plant.benefits[0].name} \n"

                if (plant.benefits.size > 1) {
                    cure += plant.benefits[1].name
                }
            }
            tvBsPlantCureContent.text = cure

            tvBsPlantProcessContent.text = plant.consumption
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        getMyLocation()

        with(mMap.uiSettings) {
            isCompassEnabled = true
            isMapToolbarEnabled = true
        }

        if (detailViewModel.plant.value != null) {
            val plant = detailViewModel.plant.value!!.plants[0]

            plant.locations?.forEachIndexed { index, plt ->
                if (plt?.lat != null && plt.lon != null) {
                    val coordinate = LatLng(plt.lat.toDouble(), plt.lon.toDouble())
                    mMap.addMarker(
                        MarkerOptions()
                            .position(coordinate)
                            //.title(detailViewModel.plant.value.plants[0].name)
                            .alpha(0.7F)
                            //.snippet(story.description)
                    )?.tag = index

                    if (index == plant.locations.size - 1) {
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 15f))
                    }
                }
            }
        }

        mMap.setOnMapClickListener {
            binding.fabAddPin.isEnabled = true
            binding.fabAddPin.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.sacramento))

            marker?.remove()
            marker = mMap.addMarker(
                MarkerOptions()
                    .position(it)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
            )
            location = it
        }

        mMap.setOnMarkerClickListener(this)
    }

    override fun onMarkerClick(m: Marker): Boolean {
        if (m.tag == null) {
            binding.fabAddPin.isEnabled = false
            binding.fabAddPin.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.light_gray3))

            marker?.remove()

            return true
        }

        val index = m.tag.toString().toInt()

        val args = Bundle()
        args.putString(LocationDetailFragment.TYPE, "detail")
        args.putString(LocationDetailFragment.ID, detailViewModel.plant.value!!.plants[0].id.toString())
        args.putString(LocationDetailFragment.NAME, detailViewModel.plant.value!!.plants[0].name)
        args.putString(LocationDetailFragment.DESCRIPTION, detailViewModel.plant.value!!.plants[0].locations?.get(index)?.description)

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
}