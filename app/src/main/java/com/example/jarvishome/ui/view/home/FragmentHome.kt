package com.example.jarvishome.ui.view.home

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import com.example.jarvishome.R
import com.example.jarvishome.core.common.CAMERA_PERMISSIONS
import com.example.jarvishome.core.common.PermissionsHelper.hasPermissions
import com.example.jarvishome.core.common.PermissionsHelper.isPermissionsCamera
import com.example.jarvishome.core.common.PermissionsHelper.launchPermissionSettings
import com.example.jarvishome.core.common.PermissionsHelper.shouldShowRequestPermissionRationale
import com.example.jarvishome.core.common.events.ProductContentEvent
import com.example.jarvishome.core.common.extensions.observe
import com.example.jarvishome.databinding.FragmentHomeBinding
import com.example.jarvishome.ui.base.BaseFragment
import com.example.jarvishome.ui.viewmodel.HomeViewModel
import com.example.jarvishome.ui.viewmodel.HomeViewModel.Event.*
import com.example.jarvishome.ui.viewmodel.ProductViewModel
import com.example.jarvishome.ui.viewmodel.ProductViewModel.Event.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.common.util.concurrent.ListenableFuture

import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.concurrent.ExecutorService
@AndroidEntryPoint
class FragmentHome: BaseFragment() {

    private var LOG_TAG ="FragmentHome"

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!! // This property is only valid between onCreateView and onDestroyView.

    private val viewModelProduct: ProductViewModel by viewModels()
    private val viewModel: HomeViewModel by viewModels()

    private lateinit var bottomSheet:BottomSheetBehavior<View>

    lateinit var cameraProviderFuture : ListenableFuture<ProcessCameraProvider>
    private lateinit var cameraExecutor: ExecutorService

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            it.checkIfPermissionsGranted(viewModelProduct::didGrantedCameraPermissions)
        }

    private val REQUEST_CAMERA_PERMISSION = 1

    override fun onAttach(context: Context) {
        super.onAttach(context)

        cameraProviderFuture = ProcessCameraProvider.getInstance(context)

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModelProduct.initFlow(view.context)
        viewModelProduct.eventsFlow.observe(viewLifecycleOwner, ::updateUi)
        _binding?.searchBarcode?.setOnClickListener {
            //viewModelProduct.searchByCode("3017624010701")
            viewModelProduct.SearchClick()
            Log.d(LOG_TAG,"setOnClickListener")
            /*if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
            }
            else {
                scanBarcode()
                //bottomSheet.state= BottomSheetBehavior.STATE_EXPANDED;
            }*/

        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        bottomSheet= BottomSheetBehavior.from(binding.lyBottomSheet.fgBottomSheet)
        bottomSheet.state= BottomSheetBehavior.STATE_HIDDEN
        bottomSheet.addBottomSheetCallback(object: BottomSheetBehavior.BottomSheetCallback(){
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                //TODO("Not yet implemented")
                Log.d("State", newState.toString())
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {}
                    BottomSheetBehavior.STATE_EXPANDED -> {}
                    BottomSheetBehavior.STATE_COLLAPSED -> {}
                    BottomSheetBehavior.STATE_DRAGGING -> {}
                    BottomSheetBehavior.STATE_SETTLING -> {}
                }
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                //TODO("Not yet implemented")
                //Log.d("Slide", slideOffset.toString())
            }
        })
        return view
    }
    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)  // Registrarse para escuchar eventos
    }

    override fun onStop() {
        EventBus.getDefault().unregister(this)  // Des-registrarse
        super.onStop()
    }
    //Events
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onProductContentEvent(event: ProductContentEvent) {
        // Aquí manejas el evento
        if (event.result == RESULT_OK) {
            // Hacer algo con event.data
            Log.d("Event", event.resultValue.code.toString())
        }
    }
    private fun updateUi (model: HomeViewModel.Event) {
        Log.e("TAG", "updateUi: $model")
        with(binding) {
            when (model) {
                is SetupViewNav -> {}
                else -> {}
            }
        }
    }
    fun updateUi(event: ProductViewModel.Event) {
        Log.e("TAG", "updateUi: $event")
        with(binding) {
            when (event) {
                is SetupView -> {}
                is RequestCameraPermissions -> activity?.let { activity ->
                    when {
                        // Si ya tiene permisos de cámara
                        isPermissionsCamera(activity) -> {
                            viewModelProduct.didGrantedCameraPermissions(true)
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
                    scanBarcode()
                    binding.cameraView.visibility = View.VISIBLE
                }
                is ShowProductSendData -> {
                    EventBus.getDefault().post(ProductContentEvent(RESULT_OK, event.product))
                }
                else -> {}
            }
        }
    }
    private fun alertOkAction(resIdMessage: Int, action: () -> Unit) {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.app_name)
            .setMessage(resIdMessage)
            .setPositiveButton(R.string.common_accept) { dialog, _ ->
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
                    scanBarcode()
                } else {
                    // Permiso denegado. Informa al usuario la necesidad del permiso.
                    Toast.makeText(requireContext(), "Se necesita el permiso de cámara para continuar", Toast.LENGTH_SHORT).show()
                    ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
                }
            }
            else -> {

            }
        }
    }

    private fun scanBarcode() {
        Log.d(LOG_TAG,"scanBarcode")
        cameraSettings()
        viewModelProduct.searchByCode("3017624010701")
    }

    private fun cameraSettings() {
        Log.d(LOG_TAG,"cameraSettings")
        cameraProviderFuture.addListener(Runnable {
            val cameraProvider = cameraProviderFuture.get()
            bindPreview(cameraProvider)
        }, ContextCompat.getMainExecutor(requireContext()))


    }
    private fun bindPreview(cameraProvider : ProcessCameraProvider) {
        Log.d(LOG_TAG,"bindPreview")
        var preview : Preview = Preview.Builder()
            .build()

        var cameraSelector : CameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        preview.setSurfaceProvider(binding.cameraView.getSurfaceProvider())

        var camera = cameraProvider.bindToLifecycle(this as LifecycleOwner, cameraSelector, preview)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


