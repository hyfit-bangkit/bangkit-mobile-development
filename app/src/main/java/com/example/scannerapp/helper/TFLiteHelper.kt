package com.example.scannerapp.helper

import android.content.Context
import android.graphics.Bitmap
import okhttp3.OkHttpClient
import okhttp3.Request
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel

class TFLiteHelper(private val context: Context, private val modelPath: String) {
    companion object {
        const val IMAGE_SIZE = 224
        const val NUM_CLASSES = 1000
    }
    private var interpreter: Interpreter? = null
    private val labels = mutableListOf<String>()

    init{
        downloadModel()
        loadLabels()
    }

    private fun downloadModel() {
        val modelUrl = "https://storage.googleapis.com/model-ml-hyfit/my_model_lite_with_metadata.tflite"
        val modelFile = File(context.filesDir, "my_model_lite_with_metadata.tflite")

        Thread {
            try {
                val client = OkHttpClient()
                val request = Request.Builder().url(modelUrl).build()
                client.newCall(request).execute().use { response ->
                    if (response.isSuccessful) {
                        response.body?.byteStream()?.let { inputStream ->
                            FileOutputStream(modelFile).use { outputStream ->
                                inputStream.copyTo(outputStream)
                            }
                        }
                    }
                }

                val tfliteModel = FileInputStream(modelFile).channel
                    .map(FileChannel.MapMode.READ_ONLY, 0 , modelFile.length())
                interpreter = Interpreter(tfliteModel)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }

    private fun loadLabels() {
        try {
            context.assets.open("label.txt").bufferedReader().useLines { lines ->
                labels.addAll(lines)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

   fun prepareInputData(image: Bitmap): ByteBuffer {
       val inputData = ByteBuffer.allocateDirect(4 * IMAGE_SIZE * IMAGE_SIZE * 3)
       inputData.order(ByteOrder.nativeOrder())

       val intValues = IntArray(IMAGE_SIZE * IMAGE_SIZE)
       image.getPixels(intValues, 0, IMAGE_SIZE, 0, 0, IMAGE_SIZE, IMAGE_SIZE)

       for (pixel in intValues) {
           val r = (pixel shr 16 and 0xFF) / 255.0f
           val g = (pixel shr 8 and 0xFF) / 255.0f
           val b = (pixel and 0xFF) / 255.0f
           inputData.putFloat(r)
           inputData.putFloat(g)
           inputData.putFloat(b)
       }
       return inputData
   }

    fun runInference(inputData: ByteBuffer): String {
        val outputData = Array(1) { FloatArray(NUM_CLASSES) }
        interpreter?.run(inputData, outputData)

        val predictedIndex = outputData[0].indices.maxByOrNull { outputData[0][it] } ?: -1
        return if (predictedIndex != -1) labels.getOrNull(predictedIndex) ?: "Unknown" else "Unknown"
    }

    fun close() {
        interpreter?.close()
    }
}