package co.early.fore.kt.core.time


/**
 * This enables us to set a mock the system time for testing.
 */
actual class SystemTimeWrapper {
    actual fun currentTimeMillis(): Long {
       return 1000; //TODO return Int64(Date().timeIntervalSince1970 * 1000)
    }
}