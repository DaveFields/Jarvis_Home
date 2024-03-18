package com.example.jarvishome.ui.view.InfoProduct

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.jarvishome.databinding.ComponentBarDataBinding

class InfoBarComponent (private val binding: ComponentBarDataBinding) {

    fun updateData(percent: Float, title: String, info: String, colorRes: Int, colorResInfo: Int) {

        binding.tvTittle.text = title
        binding.tvInfo.text = info
        binding.percentBar.text = "${(percent * 100).toInt()} %"
        binding.percentBar.setTextColor(ContextCompat.getColor(binding.root.context, colorResInfo))

        val params = binding.pbPercent.layoutParams as ConstraintLayout.LayoutParams
        params.matchConstraintPercentHeight = percent
        binding.pbPercent.setCardBackgroundColor(ContextCompat.getColor(binding.root.context, colorRes))
    }

    fun setVisible(visible: Boolean) {
        binding.root.visibility = if(visible) View.VISIBLE else View.GONE
    }

}