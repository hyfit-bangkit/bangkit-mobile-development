package com.example.hyfit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NutritionalAdapter(private val nutritionList: List<NutritionItem>) :
    RecyclerView.Adapter<NutritionalAdapter.NutritionalViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NutritionalViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_nutritions, parent, false)
        return NutritionalViewHolder(view)
    }

    override fun onBindViewHolder(holder: NutritionalViewHolder, position: Int) {
        val item = nutritionList[position]
        holder.tvNutrition.text = item.nutritionValue
        holder.ivNutrition.setImageResource(item.imageResId)
    }

    override fun getItemCount(): Int {
        return nutritionList.size
    }

    class NutritionalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivNutrition: ImageView = itemView.findViewById(R.id.ivNutrition)
        val tvNutrition: TextView = itemView.findViewById(R.id.tvNutrition)
    }
}
