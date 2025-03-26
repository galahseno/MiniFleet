package id.dev.minifleet

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.dev.core.domain.UserPreferences
import id.dev.minifleet.navigation.MainState
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainViewModel(
    userPreferences: UserPreferences
) : ViewModel() {
    var state by mutableStateOf(MainState())
        private set

    init {
        state = state.copy(isCheckingAuth = true)
        userPreferences
            .isLoggedIn()
            .distinctUntilChanged()
            .onEach { isLogin ->
                state = state.copy(
                    isCheckingAuth = false, isLoggedIn = isLogin
                )
            }.launchIn(viewModelScope)
    }
}