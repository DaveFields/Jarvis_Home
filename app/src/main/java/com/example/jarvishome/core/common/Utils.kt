package com.example.jarvishome.core.common

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraManager
import android.location.LocationManager
import androidx.core.content.ContextCompat

class Utils {

    companion object {

        fun isCameraPermissionGranted(context: Context): Boolean {
            return ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        }
    }
}