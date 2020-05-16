package co.early.fore.kt.core.logging

class IosLogger : Logger {

    private var longestTagLength = 0

    override fun e(tag: String, message: String) {
        println("(E) " + padTagWithSpace(tag) + "|" + message)
    }

    override fun w(tag: String, message: String) {
        println("(W) " + padTagWithSpace(tag) + "|" + message)
    }

    override fun i(tag: String, message: String) {
        println("(I) " + padTagWithSpace(tag) + "|" + message)
    }

    override fun d(tag: String, message: String) {
        println("(D) " + padTagWithSpace(tag) + "|" + message)
    }

    override fun v(tag: String, message: String) {
        println("(V) " + padTagWithSpace(tag) + "|" + message)
    }

    override fun e(tag: String, message: String, throwable: Throwable) {
        e(tag, message)
        println(throwable)
    }

    override fun w(tag: String, message: String, throwable: Throwable) {
        w(tag, message)
        println(throwable)
    }

    override fun i(tag: String, message: String, throwable: Throwable) {
        i(tag, message)
        println(throwable)
    }

    override fun d(tag: String, message: String, throwable: Throwable) {
        d(tag, message)
        println(throwable)
    }

    override fun v(tag: String, message: String, throwable: Throwable) {
        v(tag, message)
        println(throwable)
    }

    private fun padTagWithSpace(tag: String): String? {
        val paddingResult = padTagWithSpace(tag, longestTagLength)
        longestTagLength = paddingResult.second
        return paddingResult.first
    }
}