package id.dev.core.data.model

import kotlinx.serialization.Serializable

@Serializable
data class VehicleData(
    val position: LatLngData,
    val engineOn: Boolean,
    val doorOpen: Boolean,
    val speedKmh: Int,
    val positionIndex: Int,
)