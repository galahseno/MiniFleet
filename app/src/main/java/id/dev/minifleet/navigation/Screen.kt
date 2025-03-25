package id.dev.minifleet.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    data object Auth {
        @Serializable
        data object Login
    }

    @Serializable
    data object Home {
        @Serializable
        data object Dashboard

        @Serializable
        data object Maps
    }
}
