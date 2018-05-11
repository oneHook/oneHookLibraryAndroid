package com.onehookinc.onehooklibraryandroid.sample.samples.demo.kotlin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.onehookinc.onehooklibraryandroid.R
import com.onehookinc.onehooklibraryandroid.sample.common.BaseFragment

class KotlinTryFragment: BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sample_view_misc, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.something()
    }
}

fun KotlinTryFragment.something() {

}