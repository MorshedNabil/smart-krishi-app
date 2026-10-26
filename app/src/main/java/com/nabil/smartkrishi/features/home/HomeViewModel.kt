package com.nabil.smartkrishi.features.home

import android.app.Application
import androidx.compose.runtime.Applier
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nabil.smartkrishi.data.local.AppDatabase
import com.nabil.smartkrishi.data.repository.HomeRepository
import com.nabil.smartkrishi.data.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * HomeViewModel handles state logic for Home Screen and triggers Retrofit API calls via HomeRepository.
 */
class HomeViewModel(
    application: Application,
    private val repository: HomeRepository = HomeRepository(),
    private val weatherRepository: WeatherRepository = WeatherRepository(weatherDao = AppDatabase.getInstance(application).weatherDao())
) : AndroidViewModel(application) {
    // Far date state
    private val _dateState = MutableStateFlow(DateUiState(date = getCurrentFormattedDate()))
    val dateState: StateFlow<DateUiState> = _dateState.asStateFlow()

    // Home UI State
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    // Weather UI State
    private val _weatherState = MutableStateFlow<WeatherState>(WeatherState())
    val weatherState: StateFlow<WeatherState> = _weatherState.asStateFlow()

    init {
        loadHomeData()
        observeAndFetchWeatherData()
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

    fun observeAndFetchWeatherData() {
        // To fetch fresh weather data from backend
        viewModelScope.launch {
            weatherRepository.refreshWeather("default")
        }
        // To get cached weather data from Room DB
        viewModelScope.launch {
            weatherRepository.getWeatherStream("default").collect { weather ->
                _weatherState.value = WeatherState(weather = weather)
            }
        }
    }
}

private fun getCurrentFormattedDate(): String {
    return try {
        LocalDate.now().format(DateTimeFormatter.ofPattern("d MMMM, yyyy", Locale.ENGLISH))
    } catch (e: Exception) {
        "Date load failed"
    }
}
