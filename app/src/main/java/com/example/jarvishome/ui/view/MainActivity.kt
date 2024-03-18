package com.example.jarvishome.ui.view

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import com.example.jarvishome.core.common.extensions.observe
import com.example.jarvishome.databinding.ActivityMainBinding
import com.example.jarvishome.ui.base.BaseActivity
import com.example.jarvishome.ui.base.BaseEvent
import com.example.jarvishome.ui.viewmodel.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.example.jarvishome.ui.viewmodel.MainActivityViewModel.Event.GoToHome
import com.example.jarvishome.ui.viewmodel.MainActivityViewModel.Event.Loading
import com.example.jarvishome.ui.viewmodel.MainActivityViewModel.Event.ShowMessage
import com.example.jarvishome.ui.viewmodel.MainActivityViewModel.Event.ShowToast

@AndroidEntryPoint
class MainActivity: BaseActivity() {

    private lateinit var binding : ActivityMainBinding
    private var LOG_TAG ="MainActivity"
    private val mainViewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init(mainViewModel)
        mainViewModel.eventsFlow.observe(this, ::updateUi)
    }
    fun updateUi(event: MainActivityViewModel.Event) {
        Log.d(LOG_TAG, "updateUi: $event")
        when (event) {
            is GoToHome -> TODO()
            is Loading -> {
                Log.d(LOG_TAG, "Loading")
                mainViewModel.showLoading(true)
                //showLoadingAnimation()
            }
            is ShowMessage -> TODO()
            is ShowToast -> TODO()
            else -> {}
        }
    }
    private fun showLoadingAnimation() {
        Log.d("showLoadingAnimation","enter")
        if (binding.loading.isAnimating) {
            binding.loading.pauseAnimation()
            binding.loading.visibility= View.GONE
        } else {
            binding.loading.playAnimation()
            binding.loading.visibility= View.VISIBLE
        }
    }
}