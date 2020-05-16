package co.early.fore.kt.core.threading

/**
 * This enables us to set a mock the system time for testing.
 */
actual fun isOnUiThread(): Boolean {
    return true
}