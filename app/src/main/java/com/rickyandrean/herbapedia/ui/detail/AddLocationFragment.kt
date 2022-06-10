package com.rickyandrean.herbapedia.ui.detail

import android.R
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.model.LatLng
import com.rickyandrean.herbapedia.databinding.FragmentAddLocationBinding
import com.rickyandrean.herbapedia.ui.maps.LocationDetailFragment

class AddLocationFragment : DialogFragment() {
    private var _binding: FragmentAddLocationBinding? = null
    private val binding get() = _binding!!
    private val addLocationViewModel: AddLocationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvAddLocationName.text = arguments?.getString(NAME)

        addLocationViewModel.addStatus.observe(this){
            if (it) {
                val intent = Intent(requireActivity(), DetailActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }
        }

        binding.btnAddLocationSubmit.setOnClickListener {
            val lat = arguments?.getString(LAT)!!.toDouble()
            val lon = arguments?.getString(LON)!!.toDouble()
            addLocationViewModel.addLocation(LatLng(lat, lon), binding.tiAddLocationDescription.editText?.text.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val NAME = "name"
        const val LAT = "lat"
        const val LON = "lon"
    }
}