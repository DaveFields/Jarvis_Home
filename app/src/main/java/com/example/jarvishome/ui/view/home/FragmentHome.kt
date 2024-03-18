package com.example.jarvishome.ui.view.home

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.pm.PackageManager
import android.hardware.camera2.CameraAccessException
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.camera.core.CameraInfoUnavailableException
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import com.example.jarvishome.R
import com.example.jarvishome.core.common.CAMERA_PERMISSIONS
import com.example.jarvishome.core.common.PermissionsHelper.isPermissionsCamera
import com.example.jarvishome.core.common.PermissionsHelper.launchPermissionSettings
import com.example.jarvishome.core.common.PermissionsHelper.shouldShowRequestPermissionRationale
import com.example.jarvishome.core.common.events.ProductContentEvent
import com.example.jarvishome.core.common.extensions.observe
import com.example.jarvishome.core.utils.BarCodeAnalyzer
import com.example.jarvishome.core.utils.BarcodeDetectionListener
import com.example.jarvishome.databinding.FragmentHomeBinding
import com.example.jarvishome.domain.model.Product
import com.example.jarvishome.ui.base.BaseFragment
import com.example.jarvishome.ui.view.InfoProduct.FragmentInfoProduct
import com.example.jarvishome.ui.viewmodel.HomeViewModel
import com.example.jarvishome.ui.viewmodel.HomeViewModel.Event.*
import com.example.jarvishome.ui.viewmodel.MainActivityViewModel
import com.google.common.util.concurrent.ListenableFuture
import com.squareup.moshi.Moshi
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@ExperimentalGetImage @AndroidEntryPoint
class FragmentHome: BaseFragment(), BarcodeDetectionListener {

    private var LOG_TAG ="FragmentHome"

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!! // This property is only valid between onCreateView and onDestroyView.

    private val infoProductFragment: FragmentInfoProduct by lazy { FragmentInfoProduct() }

    private val viewModelHome: HomeViewModel by viewModels()
    private val mviewModelHome: MainActivityViewModel by viewModels()


    private lateinit var cameraProvider: ProcessCameraProvider
    //lateinit var cameraProviderFuture : ListenableFuture<ProcessCameraProvider>
    private lateinit var cameraExecutor: ExecutorService

    private val cameraProviderFuture: ListenableFuture<ProcessCameraProvider> by lazy {
        ProcessCameraProvider.getInstance(requireContext())
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            it.checkIfPermissionsGranted(viewModelHome::didGrantedCameraPermissions)
        }

    private val REQUEST_CAMERA_PERMISSION = 1

    override fun onDetach() {
        super.onDetach()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(viewModelHome)
        viewModelHome.initFlow(view.context)
        viewModelHome.eventsFlow.observe(viewLifecycleOwner,::updateUi)

        //initializeCameraProviderAndExecutor(requireContext())
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }
    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        EventBus.getDefault().unregister(this)
        super.onStop()
    }
    //Events
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onProductContentEvent(event: ProductContentEvent) {
        if (event.result == RESULT_OK) {
            val moshi = Moshi.Builder().build()
            val jsonAdapter = moshi.adapter(Product::class.java)
            val productJson = jsonAdapter.toJson(event.resultValue)
            val bundle = bundleOf("product" to productJson)
            val fragment = FragmentInfoProduct().apply {
                arguments = bundle
            }
            fragment.show(childFragmentManager, infoProductFragment.tag)
        }
    }
    fun updateUi(event: HomeViewModel.Event) {
        Log.d(LOG_TAG, "updateUi: $event")
        with(binding) {
            when (event) {
                is SetupView -> {
                    searchBarcode.setOnClickListener { viewModelHome.SearchClick() }
                }
                is RequestCameraPermissions -> activity?.let { activity ->
                    when {
                        // Si ya tiene permisos de cámara
                        isPermissionsCamera(activity) -> {
                            Log.d(LOG_TAG, "tiene permisos")
                            viewModelHome.didGrantedCameraPermissions(true)
                        }
                        // Si se debe mostrar una explicación sobre por qué se necesita el permiso
                        shouldShowRequestPermissionRationale(activity, CAMERA_PERMISSIONS) -> {
                            alertOkAction(R.string.common_permission_no_accepted) {
                                launchPermissionSettings(activity)
                            }
                        }
                        // Si necesita solicitar los permisos
                        else -> {
                            requestPermissionLauncher.launch(CAMERA_PERMISSIONS)
                        }
                    }
                }
                is OpenCamera -> {
                    if (!isPermissionsCamera(requireActivity())) {
                        Log.d("OpenCamera", "entro if")
                        requestCameraPermissions()
                    } else {
                        Log.d("OpenCamera", "entro else")
                        initializeCameraProviderAndExecutor()
                        binding.cameraView.visibility = View.VISIBLE
                    }
                }
                is ShowProductSendData -> {
                    EventBus.getDefault().post(ProductContentEvent(RESULT_OK, event.product))
                }
                is ShowLoading ->{
                    Log.d(LOG_TAG, "ShowLoading")
                    mviewModelHome.showLoading()
                }
                else -> {}
            }
        }
    }
    private fun initializeCameraProviderAndExecutor() {
        try {
        // Asegúrate de no inicializarlos nuevamente si ya lo están
            Log.d("initializeCameraProviderAndExecutor", (!::cameraProvider.isInitialized).toString())
            Log.d("initializeCameraProviderAndExecutor", (!::cameraExecutor.isInitialized).toString())
            if (!::cameraProvider.isInitialized || !::cameraExecutor.isInitialized) {
                cameraProviderFuture.addListener({
                    cameraProvider = cameraProviderFuture.get()
                    cameraExecutor = Executors.newSingleThreadExecutor()

                    bindCamera(cameraProvider)
                }, ContextCompat.getMainExecutor(requireContext()))
            } else {
                Log.d("initializeCameraProviderAndExecutor", "Camera provider and executor already initialized.")
            }
        } catch (exception: Exception) {
            Log.e("initializeCameraProviderAndExecutor", "Error initializing camera provider and executor: ${exception.message}", exception)
            // Manejar la excepción según sea necesario
        }
    }
    private fun alertOkAction(resIdMessage: Int, action: () -> Unit) {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.app_name)
            .setMessage(resIdMessage)
            .setPositiveButton(R.string.common_accept) { _, _ ->
                action.invoke()
            }
            .setNegativeButton(R.string.common_cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun Map<String, Boolean>.checkIfPermissionsGranted(action: (Boolean) -> Unit) {
        val allPermissionsGranted = this.all { it.value }
        action.invoke(allPermissionsGranted)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CAMERA_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permiso concedido. Puedes iniciar la cámara o lo que necesites hacer.
                    viewModelHome.didGrantedCameraPermissions(true)
                } else {
                    // Permiso denegado. Informa al usuario la necesidad del permiso.
                    Toast.makeText(requireContext(), "Se necesita el permiso de cámara para continuar", Toast.LENGTH_SHORT).show()
                    ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
                }
            }
            else -> { }
        }
    }

    private fun requestCameraPermissions() {
        requestPermissionLauncher.launch(CAMERA_PERMISSIONS)
    }
    private fun bindCamera(cameraProvider: ProcessCameraProvider) {
        binding.cameraView.visibility = View.VISIBLE
        bindPreview(cameraProvider)
        bindImageAnalysis(cameraProvider)
    }
    private fun bindPreview(cameraProvider: ProcessCameraProvider) {
        Log.d(LOG_TAG, "bindPreview")

        val preview = createPreview()
        val cameraSelector = createCameraSelector()

        preview.setSurfaceProvider(binding.cameraView.surfaceProvider)

        try {
                val camera = cameraProvider.bindToLifecycle(
                this as LifecycleOwner,
                cameraSelector,
                preview
            )
            Log.d(LOG_TAG, "Cámara vinculada correctamente a lifecycle")
            // Realizar cualquier operación adicional con la cámara si es necesario
        } catch (cameraInfoUnavailableException: CameraInfoUnavailableException) {
            Log.e(LOG_TAG, "Error al obtener información de la cámara", cameraInfoUnavailableException)
        } catch (cameraAccessException: CameraAccessException) {
            Log.e(LOG_TAG, "Error al acceder a la cámara", cameraAccessException)
        } catch (exception: Exception) {
            Log.e(LOG_TAG, "Error desconocido al enlazar la cámara al ciclo de vida", exception)
        }
    }

    private fun bindImageAnalysis(cameraProvider: ProcessCameraProvider) {
        Log.d(LOG_TAG, "bindImageAnalysis")

        val imageAnalysis = createImageAnalysis()
        val cameraSelector = createCameraSelector()

        try {
            cameraProvider.bindToLifecycle(
                this as LifecycleOwner,
                cameraSelector,
                imageAnalysis
            )
        } catch (exception: Exception) {
            Log.e(LOG_TAG, "Error al enlazar el análisis de imagen al ciclo de vida", exception)
            // Manejar la excepción según sea necesario
        }
    }

    private fun createImageAnalysis(): ImageAnalysis {
        return ImageAnalysis.Builder()
            .build()
            .also {
                it.setAnalyzer(cameraExecutor, BarCodeAnalyzer(this))
            }
    }
    private fun createPreview(): Preview {
        return Preview.Builder()
            .build()
    }

    private fun closedCamera(){
        try {
        Log.d(LOG_TAG, "closedCamera")
        // Liberar recursos al destruir la actividad o fragmento
        cameraProvider.unbindAll()
        cameraExecutor.shutdown()
        binding.cameraView.visibility = View.GONE
        Log.d("closedCamera", (!::cameraProvider.isInitialized).toString())
        Log.d("closedCamera", (!::cameraExecutor.isInitialized).toString())
        } catch (exception: Exception) {
            Log.e("closedCamera", "Error closing camera: ${exception.message}", exception)
            // Manejar la excepción según sea necesario
        }
    }

    private fun createCameraSelector(): CameraSelector {
        return CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onBarcodeDetected(barcodeValue: String) {
        Log.d("BarcodeDetection", "Código de barras detectado: $barcodeValue")
        closedCamera()
        viewModelHome.searchByCode(barcodeValue)
    }

    override fun onBarcodeDetectionError(error: Exception) {
        Log.e("BarcodeDetection", "Error en la detección de códigos de barras: ${error.message}")
        closedCamera()
    }
}


