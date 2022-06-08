package com.rickyandrean.herbapedia.ui.main.ui.home

import android.content.Intent
import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.rickyandrean.herbapedia.R
import com.rickyandrean.herbapedia.databinding.FragmentHomeBinding
import com.rickyandrean.herbapedia.model.PlantsItem
import com.rickyandrean.herbapedia.storage.Global
import com.rickyandrean.herbapedia.ui.detail.DetailActivity
import com.rickyandrean.herbapedia.ui.main.MainActivity
import com.rickyandrean.herbapedia.ui.scan.ScanActivity
import java.util.Random

class HomeFragment : Fragment(), View.OnFocusChangeListener {
    private val homeViewModel: HomeViewModel by viewModels()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textInputSearch.onFocusChangeListener = this
        binding.cvHomeScan.setOnClickListener {
            val intent = Intent(requireActivity(), ScanActivity::class.java)
            startActivity(intent)
        }

        homeViewModel.plant.observe(viewLifecycleOwner) { result ->
            if (!homeViewModel.finish.value!!) {
                val random = Random().nextInt(result.plants.size - 1)
                val plant = result.plants[random]

                homeViewModel.finish.value = true
                homeViewModel.plantSelected.value = plant
            }
        }

        homeViewModel.plantSelected.observe(viewLifecycleOwner) {
            updateScreen(it)
        }
    }

    private fun updateScreen(plant: PlantsItem) {
        binding.homePlant.tvPlantName.text = plant.name
        binding.homePlant.tvPlantLatin.text = plant.latinName
        Glide.with(this)
            .load(plant.image)
            .transform(CenterInside(), RoundedCorners(8))
            .into(binding.homePlant.ivItemPlantImage)

        var nutrition = "-"
        if (plant.nutritions.isNotEmpty()) {
            nutrition = "${plant.nutritions[0].name} \n"

            if (plant.nutritions.size > 1) {
                nutrition += plant.nutritions[1].name
            }
        }
        binding.homePlant.tvNutrientContent.text = nutrition

        var cure = "-"
        if (plant.benefits.isNotEmpty()) {
            cure = "${plant.benefits[0].name} \n"

            if (plant.benefits.size > 1) {
                cure += plant.benefits[1].name
            }
        }
        binding.homePlant.tvCureContent.text = cure

        binding.homePlant.btnReadMore.setOnClickListener {
            val intent = Intent(requireActivity(), DetailActivity::class.java)
            // intent.putExtra(DetailActivity.ID, plant.id)
            Global.PLANT_ID = plant.id
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onFocusChange(v: View?, p1: Boolean) {
        if (v?.id == R.id.text_input_search) {
            binding.textInputSearch.onFocusChangeListener = null

            val navView: BottomNavigationView = requireActivity().findViewById(R.id.nav_view) as BottomNavigationView
            navView.menu.getItem(1).isChecked = true

            MainActivity.stack.add(1)
            MainActivity.searchAnimation = true

            val extras = FragmentNavigatorExtras(binding.cvHome to "plants", binding.cvHomeSearch to "plants_search", binding.cvHomeScan to "plants_scan")
            findNavController().navigate(
                R.id.action_navigation_home_to_navigation_plants,
                null,
                null,
                extras
            )
        }
    }
}