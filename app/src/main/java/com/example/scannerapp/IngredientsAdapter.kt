package com.example.scannerapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class IngredientsAdapter(private val ingredientList: List<Ingredient>) :
    RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_ingredients, parent, false)
        return IngredientsViewHolder(view)
    }

    override fun onBindViewHolder(holder: IngredientsViewHolder, position: Int) {
        val ingredient = ingredientList[position]
        holder.tvTitle.text = ingredient.title
        holder.tvGram.text = ingredient.gram
        holder.ivPhoto.setImageResource(ingredient.imageResId)
    }

    override fun getItemCount(): Int {
        return ingredientList.size
    }

    class IngredientsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivPhoto: ImageView = itemView.findViewById(R.id.ivPhoto)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvGram: TextView = itemView.findViewById(R.id.tvGram)
    }
}
