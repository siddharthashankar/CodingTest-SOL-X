package com.sid.assignment.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sid.assignment.data.Repository
import com.sid.assignment.model.CatFactsListResponse
import com.sid.assignment.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor
    (
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    private val _catFactsResponse: MutableLiveData<NetworkResult<List<CatFactsListResponse>>> = MutableLiveData()
    val catFactResponse: LiveData<NetworkResult<List<CatFactsListResponse>>> = _catFactsResponse

    fun fetchCatFactsResponse() = viewModelScope.launch {
        repository.getCatFacts().collect() { values ->
            _catFactsResponse.value = values
        }
    }

}