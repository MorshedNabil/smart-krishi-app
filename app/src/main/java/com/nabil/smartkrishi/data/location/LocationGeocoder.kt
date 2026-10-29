package com.nabil.smartkrishi.data.location

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.util.Locale
import kotlin.coroutines.resume

/**
 * Helper class to convert latitude and longitude coordinates into a human-readable location string.
 */
class LocationGeocoder(
    private val context: Context
) {

    /**
     * Converts latitude and longitude into a user-friendly location name (e.g. "Panchagarh, Rangpur").
     * Handles API 33+ (Android 13+) async GeocodeListener as well as legacy synchronous Geocoder.
     */
    suspend fun getReadableLocation(latitude: Double, longitude: Double): String? {
        if (!Geocoder.isPresent()) return null

        return withContext(Dispatchers.IO) {
            val geocoder = Geocoder(context, Locale.getDefault())

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                suspendCancellableCoroutine { continuation ->
                    try {
                        geocoder.getFromLocation(
                            latitude,
                            longitude,
                            1,
                            object : Geocoder.GeocodeListener {
                                override fun onGeocode(addresses: MutableList<Address>) {
                                    val address = addresses.firstOrNull()
                                    if (continuation.isActive) {
                                        continuation.resume(address?.let { formatAddress(it) })
                                    }
                                }

                                override fun onError(errorMessage: String?) {
                                    if (continuation.isActive) {
                                        continuation.resume(null)
                                    }
                                }
                            }
                        )
                    } catch (e: Exception) {
                        if (continuation.isActive) {
                            continuation.resume(null)
                        }
                    }
                }
            } else {
                try {
                    @Suppress("DEPRECATION")
                    val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                    val address = addresses?.firstOrNull()
                    address?.let { formatAddress(it) }
                } catch (e: Exception) {
                    null
                }
            }
        }
    }

    /**
     * Formats an [Address] object into a clean readable location string.
     */
    private fun formatAddress(address: Address): String {
        val locality = address.locality ?: address.subAdminArea ?: address.adminArea
        val subLocality = address.subLocality ?: address.thoroughfare
        val adminArea = address.adminArea ?: address.countryName

        return when {
            !subLocality.isNullOrBlank() && subLocality != locality -> {
                subLocality
            }
            !adminArea.isNullOrBlank() && locality != adminArea -> {
                adminArea
            }
            !locality.isNullOrBlank() -> {
                locality
            }
            else -> {
                address.getAddressLine(0) ?: "Unknown Location"
            }
        }
    }
}
