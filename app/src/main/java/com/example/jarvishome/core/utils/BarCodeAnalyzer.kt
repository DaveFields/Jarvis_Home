package com.example.jarvishome.core.utils

import android.util.Log
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import javax.inject.Inject

@ExperimentalGetImage class BarCodeAnalyzer @Inject constructor(private val barcodeDetectionListener: BarcodeDetectionListener): ImageAnalysis.Analyzer {
    private var scanner: BarcodeScanner? = null
    override fun analyze(image: ImageProxy) {
        val img = image.image
        try {
            if (img != null) {
                val inputImage = InputImage.fromMediaImage(img, image.imageInfo.rotationDegrees)

                // Process image searching for barcodes
                val options = BarcodeScannerOptions.Builder()
                    .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
                    .build()

                val scanner = BarcodeScanning.getClient(options)

                scanner.process(inputImage)
                    .addOnSuccessListener { barcodes ->
                        for (barcode in barcodes) {
                            Log.d("scan", barcode.displayValue.toString())
                            barcodeDetectionListener.onBarcodeDetected( barcode.displayValue.toString())
                        }
                        image.close()
                    }
                    .addOnFailureListener { exception ->
                        Log.e("BarcodeAnalyzer", "Error al procesar códigos de barras: ${exception.message}")
                        barcodeDetectionListener.onBarcodeDetectionError(exception)
                        image.close()
                    }
                    .addOnCompleteListener {
                        // Cerrar la imagen después de procesarla
                        image.close()
                    }
            } else {
                // Cerrar la imagen si es nula
                image.close()
            }
        } catch (exception: Exception) {
            Log.e("BarcodeAnalyzer", "Error al procesar la imagen: ${exception.message}")
            barcodeDetectionListener.onBarcodeDetectionError(exception)
            // Cerrar la imagen en caso de error
            image.close()
        }
    }
}