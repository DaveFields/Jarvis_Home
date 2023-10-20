package com.example.jarvishome.ui.base

import android.content.Context
import android.content.ContextWrapper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.jarvishome.R
import com.example.jarvishome.core.common.extensions.observe
import kotlinx.coroutines.flow.Flow
import com.example.jarvishome.ui.base.BaseEvent.*


open class BaseActivity: AppCompatActivity() {

    private lateinit var eventsFlow : Flow<BaseEvent>
    private lateinit var baseProgressBar: BaseProgressBar

   /* override fun attachBaseContext(newBase: Context){
        //val localeUpdatedContext: ContextWrapper = updateLocale(newBase)
        super.attachBaseContext(localeUpdatedContext)
    }*/
    override fun onDestroy() {
        if (::baseProgressBar.isInitialized) baseProgressBar.removeView()
        super.onDestroy()
    }
    fun init(baseViewModel: BaseViewModel) {
        this.eventsFlow = baseViewModel.baseEventsFlow
        this.eventsFlow.observe(this, ::observeEvent)
        this.baseProgressBar = BaseProgressBar(this, R.color.black)
    }
    private fun observeEvent(event: BaseEvent) {
        when (event) {
            is BaseEvent.ShowMessage -> Toast.makeText(this, event.message, Toast.LENGTH_LONG).show()
            is ShowLoading -> baseProgressBar.setVisible(event.visibility)
            //is GoToLoginAfterUnauthorized -> startActivity(MainAuthActivity.newIntent(this))
            //is GoToLogin -> startActivity(MainAuthActivity.newIntent(this))
            else -> {}
        }
    }

}