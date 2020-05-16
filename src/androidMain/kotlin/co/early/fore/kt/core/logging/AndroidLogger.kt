package co.early.fore.kt.core.logging

import android.util.Log

class AndroidLogger(private val tagPrefix: String? = null) : Logger {

    override fun e(tag: String, message: String) {
        Log.e(addTagPrefixIfPresent(tag), message)
    }

    override fun w(tag: String, message: String) {
        Log.w(addTagPrefixIfPresent(tag), message)
    }

    override fun i(tag: String, message: String) {
        Log.i(addTagPrefixIfPresent(tag), message)
    }

    override fun d(tag: String, message: String) {
        Log.d(addTagPrefixIfPresent(tag), message)
    }

    override fun v(tag: String, message: String) {
        Log.v(addTagPrefixIfPresent(tag), message)
    }

    override fun e(tag: String, message: String, throwable: Throwable) {
        Log.e(addTagPrefixIfPresent(tag), message, throwable)
    }

    override fun w(tag: String, message: String, throwable: Throwable) {
        Log.w(addTagPrefixIfPresent(tag), message, throwable)
    }

    override fun i(tag: String, message: String, throwable: Throwable) {
        Log.i(addTagPrefixIfPresent(tag), message, throwable)
    }

    override fun d(tag: String, message: String, throwable: Throwable) {
        Log.d(addTagPrefixIfPresent(tag), message, throwable)
    }

    override fun v(tag: String, message: String, throwable: Throwable) {
        Log.e(addTagPrefixIfPresent(tag), message, throwable)
    }

    private fun addTagPrefixIfPresent(message: String): String {
        return if (tagPrefix == null) message else tagPrefix + message
    }
}