package com.test.appsfactorytask.core.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.test.appsfactorytask.R
import com.test.appsfactorytask.common.util.setGone
import com.test.appsfactorytask.common.util.setVisible
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

abstract class BaseFragment(@LayoutRes layoutId: Int) : Fragment(layoutId) {

    @Inject
    protected lateinit var viewModelFactory: ViewModelProvider.Factory

    protected val actionBar: ActionBar?
        get() = (requireActivity() as? AppCompatActivity)?.supportActionBar

    private var pbView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pbView = view.findViewById(R.id.pbView)
    }

    protected fun hideProgressbar() = pbView?.setGone()

    protected fun showProgressbar() = pbView?.setVisible()

    protected fun showError(error: String) {
        pbView?.setGone()
        Toast.makeText(requireContext(), R.string.error, Toast.LENGTH_SHORT).show()
    }
}

