package id.dev.core.domain.util

sealed interface DataError: Error {
    enum class Local: DataError {
        ERROR_PROSES,
    }
}