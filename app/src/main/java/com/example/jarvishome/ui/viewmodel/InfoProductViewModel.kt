package com.example.jarvishome.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.jarvishome.domain.model.Product
import com.example.jarvishome.domain.model.SelectedImages
import com.example.jarvishome.ui.base.BaseViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class InfoProductViewModel : BaseViewModel(){
    sealed class Event {
        object GoToScan: Event()
        object InitActions: Event()
        object SetupView: Event()
        data class SetupBars(val product: Product): Event()
        data class SetupScore(val score: Int): Event()
        data class ShowImage(val url: SelectedImages): Event()
    }

    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    private var productCount = 0
    private val _productCountLiveData = MutableLiveData<Int>()
    val productCountLiveData: LiveData<Int>
        get() = _productCountLiveData

    var product : Product = Product()
        get() = field
        set(value) {
            field= value
        }

    fun initFlow() {
        doAction(Event.InitActions)
        doAction(Event.SetupView)
    }
    fun showProduct(){
        doAction(Event.SetupBars(product))
        product.ecoscore_score.takeIf { it != -1 }?.let {
            doAction(Event.SetupScore(it))
        }
        doAction(Event.ShowImage(product.images))


    }
    fun clickPlusProduct(){
        _productCountLiveData.value = ++productCount
    }
    fun clickLessProduct(){
        if (productCount > 0) _productCountLiveData.value = --productCount
    }
    private fun doAction(event:Event){
        viewModelScope.launch {
            eventChannel.send(event)
        }
    }
}