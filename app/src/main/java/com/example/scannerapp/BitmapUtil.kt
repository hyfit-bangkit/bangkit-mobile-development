package com.example.scannerapp

import android.graphics.Bitmap
import androidx.exifinterface.media.ExifInterface

object BitmapUtil {

    fun rotateImageIfRequired(bitmap: Bitmap, photoPath: String): Bitmap {
        val exif = ExifInterface(photoPath)
        val orientation =
            exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

        var rotatedBitmap = bitmap
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> {
                rotatedBitmap = rotateBitmap(bitmap, 90f)
            }

            ExifInterface.ORIENTATION_ROTATE_180 -> {
                rotatedBitmap = rotateBitmap(bitmap, 180f)
            }

            ExifInterface.ORIENTATION_ROTATE_270 -> {
                rotatedBitmap = rotateBitmap(bitmap, 270f)
            }

            ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> {
                rotatedBitmap = flipBitmap(bitmap, true, false)
            }

            ExifInterface.ORIENTATION_FLIP_VERTICAL -> {
                rotatedBitmap = flipBitmap(bitmap, false, true)
            }
        }
        return rotatedBitmap
    }

    private fun rotateBitmap(bitmap: Bitmap, degree: Float): Bitmap {
        val matrix = android.graphics.Matrix()
        matrix.postRotate(degree)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    private fun flipBitmap(bitmap: Bitmap, flipHorizontal: Boolean, flipVertical: Boolean): Bitmap {
        val matrix = android.graphics.Matrix()
        matrix.postScale(if (flipHorizontal) -1f else 1f, if (flipVertical) -1f else 1f)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }
}