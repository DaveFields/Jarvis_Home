package com.example.jarvishome.ui.base

import android.app.Activity
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible

class BaseProgressBar(activity : Activity?, colorResID : Int = android.R.color.white) {
    private var progressBar: ProgressBar?= null
    private var childrenView: RelativeLayout?= null
    private var rootView: ViewGroup?= null

    init{
        activity?.let { wrapActivity ->
            rootView = wrapActivity.findViewById<View>(android.R.id.content).rootView as ViewGroup
            progressBar = ProgressBar(wrapActivity, null, android.R.attr.progressBarStyle).apply {
                indeterminateTintList = ContextCompat.getColorStateList(wrapActivity, colorResID)
                isIndeterminate = true
                visibility = View.GONE
            }
            val params = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT)
            childrenView = RelativeLayout(wrapActivity)
            childrenView?.gravity = Gravity.CENTER
            childrenView?.addView(progressBar)
            rootView?.addView(childrenView,params)
        }
    }

    fun setVisible (visible : Boolean)
    {
        progressBar?.isVisible = visible
    }

    fun removeView(){
        if(progressBar != null && childrenView != null && rootView != null){
            childrenView?.removeView(progressBar)
            rootView?.removeView(childrenView)
            progressBar = null
            childrenView = null
            rootView = null
        }
    }
}