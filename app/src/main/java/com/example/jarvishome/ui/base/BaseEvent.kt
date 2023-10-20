package com.example.jarvishome.ui.base

sealed class BaseEvent {
    data class ShowLoading(val visibility : Boolean): BaseEvent()
    data class ShowMessage(val message : Int): BaseEvent()
    data class ShowToast(val message : String): BaseEvent()
}