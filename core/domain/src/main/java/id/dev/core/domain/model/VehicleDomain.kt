package id.dev.core.domain.model

data class VehicleDomain(
    val position: LatLngDomain,
    val engineOn: Boolean,
    val doorOpen: Boolean,
    val speedKmh: Int,
    val positionIndex: Int,
)
