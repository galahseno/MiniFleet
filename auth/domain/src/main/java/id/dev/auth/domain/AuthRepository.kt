package id.dev.auth.domain

import id.dev.auth.domain.model.LoginCredentials
import id.dev.core.domain.util.DataError
import id.dev.core.domain.util.Result

interface AuthRepository {
    suspend fun login(credentials: LoginCredentials): Result<Boolean, DataError.Local>
}