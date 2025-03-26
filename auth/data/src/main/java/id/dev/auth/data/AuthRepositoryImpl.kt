package id.dev.auth.data

import id.dev.auth.domain.AuthRepository
import id.dev.auth.domain.model.LoginCredentials
import id.dev.core.domain.UserPreferences
import id.dev.core.domain.util.DataError
import id.dev.core.domain.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class AuthRepositoryImpl(
    private val userPreferences: UserPreferences
) : AuthRepository {
    override suspend fun login(credentials: LoginCredentials): Result<Boolean, DataError.Local> {
        return withContext(Dispatchers.IO) {
            // Simulate network delay
            delay(2500)

            try {
                when {
                    credentials.username == "admin" && credentials.password == "admin123" -> {
                        userPreferences.setLoggedIn(true)
                        Result.Success(true)
                    }

                    credentials.username.isBlank() || credentials.password.isBlank() -> {
                        Result.Success(false)
                    }

                    else -> {
                        Result.Success(false)
                    }
                }
            } catch (e: Exception) {
                Result.Error(DataError.Local.ERROR_PROSES)
            }
        }
    }
}