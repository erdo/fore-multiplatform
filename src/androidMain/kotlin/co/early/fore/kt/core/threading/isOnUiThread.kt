package co.early.fore.kt.core.threading

import android.os.Looper

/**
 * This enables us to set a mock the system time for testing.
 */
actual fun isOnUiThread(): Boolean {
    return Looper.myLooper() == Looper.getMainLooper()
}