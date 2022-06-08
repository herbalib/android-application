package com.rickyandrean.herbapedia.ui.maps

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.rickyandrean.herbapedia.R
import com.rickyandrean.herbapedia.databinding.FragmentLocationDetailBinding
import com.rickyandrean.herbapedia.storage.Global
import com.rickyandrean.herbapedia.ui.detail.DetailActivity

class LocationDetailFragment : DialogFragment() {
    private var _binding: FragmentLocationDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLocationDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (MapsActivity.PLANT != null) {
            binding.tvMapPlantName.text = MapsActivity.PLANT!!.name
            binding.tvMapPlantDescription.text = MapsActivity.PLANT!!.description
            binding.btnMapSeeDetail.setOnClickListener {
                val intent = Intent(requireActivity(), DetailActivity::class.java)
                Global.PLANT_ID = MapsActivity.PLANT!!.plantId
                startActivity(intent)
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}