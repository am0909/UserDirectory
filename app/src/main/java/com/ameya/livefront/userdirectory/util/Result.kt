package com.ameya.livefront.userdirectory.util

/**
 * Interface that is used by the repository to indicate successful, error and loading states.
 */
sealed interface Result<out T> {
    data class Success<T>(val data: T) : Result<T>
    data class Error(val exception: Throwable) : Result<Nothing>
    data object Loading : Result<Nothing>
}