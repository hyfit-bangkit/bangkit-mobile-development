package com.example.scannerapp

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

class CameraActivity : AppCompatActivity() {

    private lateinit var previewView: PreviewView
    private lateinit var ivCameraRotate: ImageView
    private lateinit var ivCapture: FrameLayout
    private lateinit var ivFlash: ImageView
    private lateinit var flLoading: FrameLayout

    private var cameraProvider: ProcessCameraProvider? = null
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private var imageCapture: ImageCapture? = null
    private var flashEnabled = false

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
            cameraProviderFuture.addListener({
                cameraProvider = cameraProviderFuture.get()
                bindCameraUseCases()
            }, ContextCompat.getMainExecutor(this))
        } else {
            Toast.makeText(this, "Camera permission is required", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        previewView = findViewById(R.id.previewView)
        ivCameraRotate = findViewById(R.id.ivCameraRotate)
        ivCapture = findViewById(R.id.ivCapture)
        ivFlash = findViewById(R.id.ivFlash)
        flLoading = findViewById(R.id.fl_loading)

        when {
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
                cameraProviderFuture.addListener({
                    cameraProvider = cameraProviderFuture.get()
                    bindCameraUseCases()
                }, ContextCompat.getMainExecutor(this))
            }

            else -> {
                requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
            }
        }

        ivCameraRotate.setOnClickListener {
            toggleCameraSelector()
        }

        ivCapture.setOnClickListener {
            capturePhotoAndSendToApi()
        }

        ivFlash.setOnClickListener {
            toggleFlash()
        }
    }

    private fun bindCameraUseCases() {
        val preview = Preview.Builder().build().also {
            it.surfaceProvider = previewView.surfaceProvider
        }

        imageCapture = ImageCapture.Builder().build()

        cameraProvider?.unbindAll()
        cameraProvider?.bindToLifecycle(
            this as LifecycleOwner,
            cameraSelector,
            preview,
            imageCapture
        )
    }

    private fun toggleCameraSelector() {
        cameraSelector = if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
            CameraSelector.DEFAULT_FRONT_CAMERA
        } else {
            CameraSelector.DEFAULT_BACK_CAMERA
        }
        cameraProvider?.unbindAll()
        bindCameraUseCases()
    }

    private fun capturePhotoAndSendToApi() {
        val imageCapture = imageCapture ?: return

        val photoFile = File(
            externalMediaDirs.firstOrNull(),
            SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis()) + ".jpg"
        )

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    sendImageToApi(photoFile)
                }

                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(
                        applicationContext,
                        "Error taking photo: ${exception.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )
    }

    private fun sendImageToApi(photoFile: File) {
        val requestBody = RequestBody.create("image/*".toMediaTypeOrNull(), photoFile)
        val multipartBody = MultipartBody.Part.createFormData("image", photoFile.name, requestBody)

        flLoading.isVisible = true

        RetrofitClient.instance.predict(multipartBody).enqueue(object : Callback<PredictResponse> {
            override fun onResponse(call: Call<PredictResponse>, response: Response<PredictResponse>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result != null) {
                        navigateToResultActivity(photoFile.absolutePath, result)
                    }
                } else {
                    Toast.makeText(applicationContext, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
                flLoading.isVisible = false
            }

            override fun onFailure(call: Call<PredictResponse>, t: Throwable) {
                Toast.makeText(applicationContext, "Failed: ${t.message}", Toast.LENGTH_SHORT).show()
                flLoading.isVisible = false
            }
        })
    }

    private fun toggleFlash() {
        val flashMode = if (flashEnabled) {
            ImageCapture.FLASH_MODE_OFF
        } else {
            ImageCapture.FLASH_MODE_ON
        }

        imageCapture?.flashMode = flashMode
        flashEnabled = !flashEnabled

        ivFlash.setImageResource(
            if (flashEnabled) R.drawable.ic_flash_on else R.drawable.ic_flash
        )
    }

    private fun navigateToResultActivity(imagePath: String, result: PredictResponse) {
        val intent = Intent(this, ResultActivity::class.java).apply {
            putExtra("image_path", imagePath)
            putExtra("confidence_score", result.confidence_score)
            putExtra("predicted_class", result.predicted_class)
        }
        startActivity(intent)
    }

    companion object {
        const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
    }
}