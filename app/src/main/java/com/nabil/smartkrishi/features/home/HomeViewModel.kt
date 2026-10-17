package com.nabil.smartkrishi.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nabil.smartkrishi.data.repository.HomeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * HomeViewModel handles state logic for Home Screen and triggers Retrofit API calls via HomeRepository.
 */
class HomeViewModel(
    private val repository: HomeRepository = HomeRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadHomeData()
    }

    fun loadHomeData() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            val result = repository.getMarketPrices() // the coroutine call to the API
            result.onSuccess { priceData ->
                _uiState.value = HomeUiState.Success(priceData)
            }.onFailure { exception ->
                _uiState.value = HomeUiState.Error(exception.message ?: "Failed to load market prices")
            }
        }
    }
}
