package com.nabil.smartkrishi.features.home

import com.nabil.smartkrishi.domain.model.MarketPrice

// UI State representing different network states for Home Screen
sealed interface HomeUiState {
    object Loading : HomeUiState
    data class Success(val marketPrice: MarketPrice) : HomeUiState
    data class Error(val message: String) : HomeUiState
}
