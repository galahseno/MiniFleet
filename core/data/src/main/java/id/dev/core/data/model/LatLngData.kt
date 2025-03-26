package id.dev.core.data.model

import kotlinx.serialization.Serializable

@Serializable
data class LatLngData(
    val lat: Double,
    val lon: Double
)
