package com.nabil.smartkrishi.features.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nabil.smartkrishi.data.local.AppDatabase
import com.nabil.smartkrishi.data.location.LocationTracker
import com.nabil.smartkrishi.data.repository.HomeRepository
import com.nabil.smartkrishi.data.repository.WeatherRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.nabil.smartkrishi.R
import com.nabil.smartkrishi.domain.model.Weather
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * HomeViewModel handles state logic for Home Screen and triggers Retrofit API calls via HomeRepository.
 */
class HomeViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository by lazy { HomeRepository() }
    private val weatherRepository by lazy {
        WeatherRepository(
            weatherDao = AppDatabase.getInstance(application).weatherDao()
        )
    }

    // ============== Header Date state ==============
    private val _dateState = MutableStateFlow(DateUiState(date = getCurrentFormattedDate()))
    val dateState: StateFlow<DateUiState> = _dateState.asStateFlow()

    // =============== Home UI State ================
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    // =========== Weather UI State ============
    private val _weatherState = MutableStateFlow<WeatherUIState>(WeatherUIState())
    val weatherState: StateFlow<WeatherUIState> = _weatherState.asStateFlow()

    // Active fetch job handle to avoid duplicate concurrent API calls
    private var fetchWeatherJob: Job? = null

    init {
        loadHomeData()
        observeWeatherData()
    }

    // ============================ MarketPrice Section =============================
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

    // ============================= Weather Section =============================
    
    /**
     * Continuously observes cached weather data from Room DB and updates weather state
     * along with night time icon state determined by the phone's current time
     * and sunrise/sunset values from the weather domain model.
     */
    fun observeWeatherData() {
        viewModelScope.launch {
            weatherRepository.getWeatherStream().collect { weatherItem ->
                val isNight = isNightTime(weatherItem)
                val iconRes = if (isNight) R.drawable.night_ic else R.drawable.morning_ic
                _weatherState.value = WeatherUIState(
                    weather = weatherItem,
                    weatherIconRes = iconRes
                )
            }
        }
    }

    /**
     * Determines whether it is night time using the [Weather.isDay] property from the domain model.
     * Falls back to device time (6:00 PM - 6:00 AM) if weather data is null.
     */
    private fun isNightTime(weather: Weather?): Boolean {
        if (weather != null) {
            return !weather.isDay
        }

        // Fallback default: 6 PM (18:00) to 6 AM (06:00) based on device time
        return try {
            val currentHour = LocalTime.now().hour
            currentHour < 6 || currentHour >= 18
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Uses LocationTracker to get current GPS coordinates from device, resolves the human-readable location
     * via Geocoder, and updates Room DB with fresh weather.
     * Prevents duplicate concurrent API calls if a fetch is already in progress.
     */
    fun fetchWeatherFromGps() {
        // Guard against duplicate concurrent calls
        if (fetchWeatherJob?.isActive == true) return

        fetchWeatherJob = viewModelScope.launch {
            val locationTracker = LocationTracker(getApplication())
            val location = locationTracker.getCurrentLocation()
            if (location != null) {
                // Use Geocoder to resolve readable place name (e.g. "Panchagarh, Rangpur")
                val locationName = locationTracker.getReadableLocation(
                    latitude = location.latitude,
                    longitude = location.longitude
                ) ?: "GPS Location"

                weatherRepository.refreshWeatherForCoordinates(
                    lat = location.latitude,
                    lon = location.longitude,
                    locationKey = locationName
                )
            } else {
                // Fallback to default coordinates if GPS unavailable or permissions denied
                weatherRepository.refreshWeather()
            }
        }
    }
}

// =============== Helper function to get current date of device ===============
private fun getCurrentFormattedDate(): String {
    return try {
        LocalDate.now().format(DateTimeFormatter.ofPattern("d MMMM, yyyy", Locale.ENGLISH))
    } catch (e: Exception) {
        "Date load failed"
    }
}
