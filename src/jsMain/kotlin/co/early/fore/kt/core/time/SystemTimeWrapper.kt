package co.early.fore.kt.core.time

import kotlin.js.Date

/**
 * This enables us to set a mock the system time for testing.
 */
actual class SystemTimeWrapper {
    actual fun currentTimeMillis(): Long {
        return Date().getTime().toLong()
    }
}