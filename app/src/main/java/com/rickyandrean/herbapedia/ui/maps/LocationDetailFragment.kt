package com.rickyandrean.herbapedia.ui.maps

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
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

        if (arguments != null) {
            binding.tvMapPlantName.text = arguments?.getString(NAME)
            binding.tvMapPlantDescription.text = arguments?.getString(DESCRIPTION)

            if (arguments?.getString(TYPE) == "map") {
                binding.btnMapSeeDetail.setOnClickListener {
                    val intent = Intent(requireActivity(), DetailActivity::class.java)
                    Global.PLANT_ID = arguments?.getString(ID)!!.toInt()
                    startActivity(intent)
                    dismiss()
                }
            } else {
                binding.btnMapSeeDetail.text = resources.getString(R.string.okay)
                binding.btnMapSeeDetail.setOnClickListener {
                    dismiss()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TYPE = "type"
        const val ID = "id"
        const val NAME = "name"
        const val DESCRIPTION = "description"
    }
}