package co.early.fore.kt.core.threading

//import platform.Foundation.Thread

/**
 * This enables us to set a mock the system time for testing.
 */
actual fun isOnUiThread() : Boolean {
    return true; //Thread.isMainThread
}