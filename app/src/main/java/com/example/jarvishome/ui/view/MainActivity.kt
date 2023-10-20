package com.example.jarvishome.ui.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.jarvishome.databinding.ActivityMainBinding
import com.example.jarvishome.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@AndroidEntryPoint
class MainActivity: BaseActivity() {

    private lateinit var binding : ActivityMainBinding
    private var LOG_TAG ="MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}