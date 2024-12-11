package com.example.scannerapp

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.scannerapp.databinding.ActivityMainBinding
import com.example.scannerapp.helper.TFLiteHelper

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var tfLiteHelper: TFLiteHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        tfLiteHelper = TFLiteHelper(this, "my_model_lite_with_metadata.tflite")

        binding.btnToGallery.setOnClickListener {
            openGallery()

        }

        binding.btnToScanner.setOnClickListener {
            // Cek apakah activity ScannerActivity sudah didefinisikan
            try {
                val intent = Intent(this, ScannerActivity::class.java)
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(this, "Error opening Scanner: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

        // Button to navigate Home (no action needed since it's already home)
        val navHome = findViewById<Button>(R.id.navHome)
        navHome.setOnClickListener {
            // Placeholder: Jika sudah di homepage, bisa dihapus atau diubah untuk tujuan lain
            Toast.makeText(this, "You're already on the homepage", Toast.LENGTH_SHORT).show()
        }

        findViewById<ImageButton>(R.id.btnToScanner).setOnClickListener {
            val intent = Intent(this@MainActivity, ScannerActivity::class.java)
            startActivity(intent)
        }

        // Button to Camera Scanner in Bottom Navigation
        val navScanner = findViewById<Button>(R.id.navScanner)
        navScanner.setOnClickListener {
            // Cek apakah activity ScannerActivity sudah didefinisikan
            try {
                val intent = Intent(this, ScannerActivity::class.java)
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(this, "Error opening Scanner: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_CODE_GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_GALLERY && resultCode == Activity.RESULT_OK) {
            val imageUri: Uri? = data?.data
            if (imageUri != null) {
                try {
                    // Konversi URI ke Bitmap
                    val bitmap: Bitmap = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                        val source = ImageDecoder.createSource(contentResolver, imageUri)
                        ImageDecoder.decodeBitmap(source)
                    } else {
                        MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
                    }

                    // Proses gambar dengan model ML
                    val inputData = tfLiteHelper.prepareInputData(bitmap)
                    val predictedLabel = tfLiteHelper.runInference(inputData)

                    // Pindah ke ResultActivity dengan hasil prediksi
                    val intent = Intent(this, ResultActivity::class.java).apply {
                        putExtra("bitmap", bitmap)
                        putExtra("predictedLabel", predictedLabel)
                    }
                    startActivity(intent)

                } catch (e: Exception) {
                    Toast.makeText(this, "Error processing image: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    companion object {
        private const val REQUEST_CODE_GALLERY = 100
    }

    override fun onDestroy() {
        super.onDestroy()
        tfLiteHelper.close()
    }
}
