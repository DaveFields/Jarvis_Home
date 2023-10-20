package com.example.jarvishome.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jarvishome.R
import com.example.jarvishome.domain.base.FailureError
import com.example.jarvishome.domain.base.Resource
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.receiveAsFlow


abstract class BaseViewModel : ViewModel() {
    private val baseEventChannel = Channel<BaseEvent>(Channel.BUFFERED)
    val baseEventsFlow = baseEventChannel.receiveAsFlow()
    /**
     * handleError send a BaseEvent from BaseViewModel to BaseActivity or BaseFragment
     */
    fun handleError(failure: Resource.Failure) {
       /* when(failure.type) {
            FailureError.Timeout -> showMessage(R.string.common_error_timeout)
            FailureError.Network -> showMessage(R.string.common_error_network)
            FailureError.NotFound -> showMessage(R.string.common_error_generic)
            FailureError.Generic -> showMessage(R.string.common_error_generic)
            else -> showMessage(R.string.common_error_generic)
        }*/
    }

    fun showLoading(visibility: Boolean) = doEvent(BaseEvent.ShowLoading(visibility))

    fun showMessage(resId: Int) = doEvent(BaseEvent.ShowMessage(resId))

    private fun doEvent(event: BaseEvent) {
        viewModelScope.launch {
            baseEventChannel.send(event)
        }
    }
}