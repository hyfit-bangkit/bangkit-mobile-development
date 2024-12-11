package com.example.scannerapp

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.scannerapp.databinding.ActivityResultBinding
import com.example.scannerapp.helper.TFLiteHelper
import java.nio.ByteBuffer
import java.nio.ByteOrder

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private lateinit var tfLiteHelper: TFLiteHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityResultBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //initialize TFLiteHelper
        tfLiteHelper = TFLiteHelper(this, "my_model_lite_with_metadata.tflite")

        val bitmap = intent.getParcelableExtra<Bitmap>("bitmap")
        if (bitmap != null) {
            // Set gambar ke ImageView
            binding.ivImageFood.setImageBitmap(bitmap)

            // Lakukan inference
            val inputData = tfLiteHelper.prepareInputData(bitmap)
            val predictedLabel = tfLiteHelper.runInference(inputData)

            // Tampilkan hasil ke TextView
            binding.tvNameFood.text = predictedLabel
        } else {
            Toast.makeText(this, "No image data provided!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tfLiteHelper.close()
    }
}