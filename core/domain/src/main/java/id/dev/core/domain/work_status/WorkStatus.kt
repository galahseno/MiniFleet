package id.dev.core.domain.work_status

sealed interface WorkStatus {
    data object Running : WorkStatus
    data object NotRunning : WorkStatus
}