package com.example.jarvishome.ui.base

import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.jarvishome.ui.base.BaseEvent.*
import com.example.jarvishome.R
import com.example.jarvishome.core.common.extensions.observe
import kotlinx.coroutines.flow.Flow

open class BaseFragment : Fragment() {

    private lateinit var eventsFlow: Flow<BaseEvent>
    private lateinit var baseProgressBar: BaseProgressBar


    override fun onDestroyView() {
        if(::baseProgressBar.isInitialized) baseProgressBar.removeView()
        super.onDestroyView()
    }

    fun init(baseViewModel: BaseViewModel) {
        this.eventsFlow = baseViewModel.baseEventsFlow
        this.eventsFlow.observe(viewLifecycleOwner, ::observeEvent)
        this.baseProgressBar = BaseProgressBar(activity, R.color.black)
    }

    private fun observeEvent(event: BaseEvent) {
        when(event) {
            is ShowMessage -> context?.let { context ->
                Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
            }
            is ShowLoading -> {
                Log.d("BaseFragment", "ShowLoading")
                baseProgressBar.setVisible(event.visibility)
            }
            //is GoToLoginAfterUnauthorized -> activity?.let { activity-> startActivity(MainAuthActivity.newIntent(activity))}
            //is GoToLogin -> activity?.let { activity-> startActivity(MainAuthActivity.newIntent(activity))}
            is ShowToast -> TODO()
            else -> {}
        }
    }
}