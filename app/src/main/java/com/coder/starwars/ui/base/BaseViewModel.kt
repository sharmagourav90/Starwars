package com.coder.starwars.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.coder.starwars.util.extensions.hide
import io.reactivex.disposables.CompositeDisposable

/**
 * Base class for all the ViewModels contain common logic
 */
abstract class BaseViewModel : ViewModel() {

    val disposable = CompositeDisposable()

    // Mutable livedata for loading state, expose LiveData instead of Mutable
    protected val _loading: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val loading: LiveData<Boolean> by lazy { _loading }

    // Mutable livedata for error cases, expose LiveData instead of Mutable
    val _error: MutableLiveData<Throwable> by lazy { MutableLiveData<Throwable>() }
    val error: LiveData<Throwable> by lazy { _error }

    // Handle Error cases
    protected fun handleError(error: Throwable) {
        _loading.hide()
        _error.postValue(error)
    }

    // Error has been handled , so passing null to error livedata
    fun errorHandled() {
        _error.value = null
    }

    // On ViewModel destruction
    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}