package com.example.jarvishome.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jarvishome.ui.base.BaseViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class HomeViewModel : BaseViewModel(){
    sealed class Event {
        object GoToScan: Event()
        object SetupViewNav: Event()
    }

    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    private fun doAction(event:Event){
        viewModelScope.launch {
            eventChannel.send(event)
        }
    }
}