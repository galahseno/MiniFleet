package id.dev.core.domain.model

data class TripLog(
    val timestamp: Long,
    val location: LatLngDomain,
    val speed: Int,
    val engineStatus: String,
    val doorStatus: String
)