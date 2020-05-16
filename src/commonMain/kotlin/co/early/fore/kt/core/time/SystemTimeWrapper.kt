package co.early.fore.kt.core.time

/**
 * This enables us to set a mock the system time for testing.
 */
expect class SystemTimeWrapper {
    fun currentTimeMillis(): Long
}