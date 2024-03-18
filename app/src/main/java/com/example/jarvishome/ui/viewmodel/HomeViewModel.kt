package com.example.jarvishome.ui.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.jarvishome.R
import com.example.jarvishome.core.common.Utils
import com.example.jarvishome.ui.base.BaseViewModel
import com.example.jarvishome.domain.base.Resource
import com.example.jarvishome.domain.model.Product
import com.example.jarvishome.domain.usecases.GetProductUsesCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getProductUsesCase:GetProductUsesCase):BaseViewModel()
{
    sealed class Event {
        object SetupView: Event()
        object GoToMainActivity: Event()
        object RequestCameraPermissions: Event()
        object OpenCamera: Event()
        object CloseCamera: Event()
        data class ShowProductSendData(val product: Product): Event()
        data class ShowMessage(val resId: Int): Event()
        data class ShowToast(val message: String): Event()
        data class ShowLoading(val visibility: Boolean): Event()
    }
    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    private lateinit var context: Context
    var permissionAccepted = false

    //region ViewModel Output
    private fun doAction(event: Event) {
        viewModelScope.launch {
            eventChannel.send(event)
        }
    }
    //region ViewModel Input
    fun initFlow(context: Context) {
        this.context = context
        doAction(Event.SetupView)
    }

    fun didGrantedCameraPermissions(granted: Boolean) {
        if (granted) {
            permissionAccepted = granted
            checkIfPermissionIsAccepted()
        } else {
            doAction(Event.RequestCameraPermissions)
        }
    }

    private fun checkIfPermissionIsAccepted() {
        if (permissionAccepted && Utils.isCameraPermissionGranted(context)) {
            doAction(Event.OpenCamera)
            //searchByCode("3017624010701")
        }
    }

    fun SearchClick() {
        doAction(Event.RequestCameraPermissions)
    }

    fun searchByCode(productId: String){
        Log.d("ProductViewModel", "searchByCode")
        viewModelScope.launch {
            doAction(Event.ShowLoading(true))
            when (val result = getProductUsesCase.invoke(productId)) {
                is Resource.Success -> {
                    doAction(Event.ShowProductSendData(result.value))
                } is Resource.Failure -> {
                    doAction(Event.ShowMessage(R.string.common_error_network))
                }
            }
            doAction(Event.ShowLoading(false))
        }
    }
}