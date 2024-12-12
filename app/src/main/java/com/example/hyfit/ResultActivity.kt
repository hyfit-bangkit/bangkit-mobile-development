package com.example.hyfit

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ResultActivity : AppCompatActivity() {

    private lateinit var recipeImage: ImageView
    private lateinit var recipeTitle: TextView
    private lateinit var recipeAuthor: TextView
    private lateinit var recipeDescription: TextView
    private lateinit var ingredientsLabel: TextView
    private lateinit var totalItems: TextView
    private lateinit var confirmMealButton: Button
    private lateinit var rvNutrition: RecyclerView
    private lateinit var ingredientsRecyclerView: RecyclerView
    private lateinit var nutritionalAdapter: NutritionalAdapter
    private lateinit var ingredientsAdapter: IngredientsAdapter
    private lateinit var ivBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        recipeImage = findViewById(R.id.recipeImage)
        recipeTitle = findViewById(R.id.recipeTitle)
        recipeAuthor = findViewById(R.id.recipeAuthor)
        recipeDescription = findViewById(R.id.recipeDescription)
        ingredientsLabel = findViewById(R.id.ingredientsLabel)
        totalItems = findViewById(R.id.totalItems)
        confirmMealButton = findViewById(R.id.confirmMealButton)
        rvNutrition = findViewById(R.id.rvNutrition)
        ingredientsRecyclerView = findViewById(R.id.ingredientsRecyclerView)
        ivBack = findViewById(R.id.ivBack)

        val imagePath = intent.getStringExtra("image_path") ?: ""
        val predictedClass = intent.getStringExtra("predicted_class") ?: ""

        val bitmap = BitmapFactory.decodeFile(imagePath)

        ivBack.setOnClickListener {
            finish()
        }

        Glide.with(this@ResultActivity)
            .load(BitmapUtil.rotateImageIfRequired(bitmap, imagePath))
            .into(recipeImage)

        Log.d("<RESULT>", predictedClass)

        recipeTitle.text = predictedClass

        rvNutrition.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        nutritionalAdapter = NutritionalAdapter(getNutritionData())
        rvNutrition.adapter = nutritionalAdapter

        ingredientsRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        ingredientsAdapter = IngredientsAdapter(getIngredientsData())
        ingredientsRecyclerView.adapter = ingredientsAdapter

        recipeAuthor.text = "by James Ruth"
        recipeDescription.text = "Pancakes are some people's favorite breakfast, who doesn't like pancakes?..."
        totalItems.text = "6 items"

        confirmMealButton.setOnClickListener {
            confirmMeal()
        }

        recipeImage.setImageResource(R.drawable.ic_launcher_background)
    }

    private fun confirmMeal() {
        // Add logic to confirm the meal here if necessary
    }

    private fun getNutritionData(): List<NutritionItem> {
        return listOf(
            NutritionItem("180kCal", R.drawable.ic_launcher_foreground),
            NutritionItem("200kCal", R.drawable.ic_launcher_foreground),
            NutritionItem("250kCal", R.drawable.ic_launcher_foreground)
        )
    }

    private fun getIngredientsData(): List<Ingredient> {
        return listOf(
            Ingredient("Wheat Flour", "100gr", R.drawable.ic_launcher_foreground),
            Ingredient("Eggs", "2 pcs", R.drawable.ic_launcher_foreground),
            Ingredient("Sugar", "50gr", R.drawable.ic_launcher_foreground)
        )
    }
}
