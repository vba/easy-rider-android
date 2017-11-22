package com.renault.easyrider.instantapps.newmodule

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class NewModuleDashboardFragment : Fragment() {

    companion object {
        private val TAG = NewModuleDashboardFragment::javaClass.name
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_new_module_dashboard, container, false);
        return view
    }
}