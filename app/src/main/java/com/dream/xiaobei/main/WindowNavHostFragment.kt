package com.dream.xiaobei.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import com.tcl.tclzjpro.main.WindowFrameLayout


class WindowNavHostFragment : NavHostFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val frameLayout = WindowFrameLayout(inflater.context)
        frameLayout.id = id
        return frameLayout
    }
}