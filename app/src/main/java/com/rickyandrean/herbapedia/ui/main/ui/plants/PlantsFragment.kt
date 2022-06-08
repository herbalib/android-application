package com.rickyandrean.herbapedia.ui.main.ui.plants

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.rickyandrean.herbapedia.adapter.PlantAdapter
import com.rickyandrean.herbapedia.databinding.FragmentPlantsBinding
import com.rickyandrean.herbapedia.model.PlantsItem
import com.rickyandrean.herbapedia.ui.main.MainActivity
import com.rickyandrean.herbapedia.ui.scan.ScanActivity

class PlantsFragment : Fragment() {
    private val plantsViewModel: PlantsViewModel by viewModels()
    private var _binding: FragmentPlantsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlantsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val animation = TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
        sharedElementEnterTransition = animation
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (MainActivity.searchAnimation) {
            binding.tiSearchPlant.requestFocus()
            val imm: InputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.tiSearchPlant, InputMethodManager.SHOW_IMPLICIT)
            MainActivity.searchAnimation = false
        }

        plantsViewModel.plant.observe(viewLifecycleOwner) {
            if (!plantsViewModel.finish.value!!) {
                plantsViewModel.finish.value = true
                updateScreen(it.plants)
            }
        }

        binding.tiSearch.setEndIconOnClickListener {
            val search = binding.tiSearch.editText?.text.toString()
            plantsViewModel.finish.value = false
            plantsViewModel.searchPlant(search)
        }

        binding.cvPlantsScan.setOnClickListener {
            val intent = Intent(requireActivity(), ScanActivity::class.java)
            startActivity(intent)
        }
    }

    private fun updateScreen(plants: List<PlantsItem>) {
        binding.rvPlants.setHasFixedSize(true)
        binding.rvPlants.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPlants.adapter = PlantAdapter(plants)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}