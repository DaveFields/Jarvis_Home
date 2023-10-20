package com.example.jarvishome.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jarvishome.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(): BaseViewModel() {

    sealed class Event {

        object GoToHome: Event()
        object Loading: Event()

        data class ShowMessage(val message: String) : Event()
        data class ShowToast(val message: String) : Event()
    }

    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    private fun doAction(event:Event){
        viewModelScope.launch {
            eventChannel.send(event)
        }
    }
}