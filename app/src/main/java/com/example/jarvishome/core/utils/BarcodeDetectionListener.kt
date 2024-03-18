package com.example.jarvishome.core.utils

interface BarcodeDetectionListener {
    fun onBarcodeDetected(barcodeValue: String)
    fun onBarcodeDetectionError(error: Exception)
}