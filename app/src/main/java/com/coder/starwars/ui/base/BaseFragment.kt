package com.coder.starwars.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.annotation.CallSuper
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.coder.starwars.R
import com.coder.starwars.util.NoConnectivityException
import dagger.android.support.DaggerFragment

/**
 * BaseFragment for all the fragments in the App
 */
abstract class BaseFragment : DaggerFragment() {

    // To be override by childs
    abstract val layout: Int
    abstract val viewModelClass: Class<out BaseViewModel>
    abstract fun provideViewModelFactory(): ViewModelProvider.Factory

    // View Model Factory
    private val viewModelFactory: ViewModelProvider.Factory by lazy { provideViewModelFactory() }

    // Base View Model Lazy Initialization
    protected val baseViewModel: BaseViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(viewModelClass)
    }

    // Parent Activity for fragments
    protected lateinit var parentActivity: AppCompatActivity

    // Common logic for oCreate of all the fragments
    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    // Common logic for onCreateView of all the fragments
    @CallSuper
    override
    fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        parentActivity = activity as AppCompatActivity
        return inflater.inflate(layout, container, false)
    }

    // Common logic for onCreateView of all the fragments
    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLoadingState()
        observeExceptions()
    }

    // Observer for loading state
    private fun observeLoadingState() {
        baseViewModel.loading.observe(this, Observer { loading ->
            when (loading) {
                true -> showLoading()
                else -> hideLoading()
            }
        })
    }

    // Observer for Exceptions
    @CallSuper
    private fun observeExceptions() {
        baseViewModel.error.observe(this, Observer { error ->
            if (error == null) return@Observer
            baseViewModel.errorHandled()
            when (error) {
                is NoConnectivityException -> showNetworkError()
                else -> showToast("Application Error")
            }
        })
    }

    // To be overridden by childs for showing and hiding logic
    abstract fun hideLoading()

    abstract fun showLoading()

    // Show toast method with message
    protected open fun showToast(message: String?) {
        if (message == null) return
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    // Show Toast with msgId
    protected open fun showToast(@StringRes msgId: Int) {
        showToast(getString(msgId))
    }

    // Show Network Error message
    protected open fun showNetworkError() {
        showToast(R.string.no_connection)
    }

    // Show toolbar with title
    protected fun setUpToolbar(toolbar: Toolbar, title: String? = null) {
        parentActivity.setSupportActionBar(toolbar)
        parentActivity.supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            if (!title.isNullOrBlank()) setTitle(title)
        }
    }

    // Menu Item click handling
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            android.R.id.home -> parentActivity.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    // Pop up last transaction
    protected open fun popBack() = parentActivity.supportFragmentManager.popBackStack()
}