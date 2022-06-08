package com.rickyandrean.herbapedia.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.rickyandrean.herbapedia.databinding.ItemPlantBinding
import com.rickyandrean.herbapedia.model.PlantsItem
import com.rickyandrean.herbapedia.storage.Global
import com.rickyandrean.herbapedia.ui.detail.DetailActivity

class PlantAdapter(private val listPlant: List<PlantsItem>): RecyclerView.Adapter<PlantAdapter.PlantViewHolder>() {
    class PlantViewHolder(var binding: ItemPlantBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(plant: PlantsItem) {
            binding.tvPlantName.text = plant.name
            binding.tvPlantLatin.text = plant.latinName
            Glide.with(itemView.context)
                .load(plant.image)
                .transform(CenterInside(), RoundedCorners(8))
                .into(binding.ivItemPlantImage)

            var nutrition = "-"
            if (plant.nutritions.isNotEmpty()) {
                nutrition = "${plant.nutritions[0].name} \n"

                if (plant.nutritions.size > 1) {
                    nutrition += plant.nutritions[1].name
                }
            }
            binding.tvNutrientContent.text = nutrition

            var cure = "-"
            if (plant.benefits.isNotEmpty()) {
                cure = "${plant.benefits[0].name} \n"

                if (plant.benefits.size > 1) {
                    cure += plant.benefits[1].name
                }
            }
            binding.tvCureContent.text = cure

            binding.btnReadMore.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                Global.PLANT_ID = plant.id
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantViewHolder {
        val binding = ItemPlantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlantViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlantViewHolder, position: Int) {
        holder.bind(listPlant[position])
    }

    override fun getItemCount(): Int = listPlant.size
}