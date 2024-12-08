package com.example.scannerapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Button to Camera Scanner Feature (ubah menjadi ImageButton)
        val btnToScanner: ImageButton = findViewById<ImageButton>(R.id.btnToScanner) // Sesuaikan tipe menjadi ImageButton
        btnToScanner.setOnClickListener {
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
}
