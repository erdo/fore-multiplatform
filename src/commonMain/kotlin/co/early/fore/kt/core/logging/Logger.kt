package co.early.fore.kt.core.logging

import co.early.fore.kt.core.utils.text.TextPadder
import kotlin.math.max

interface Logger {
    fun e(tag: String, message: String)
    fun w(tag: String, message: String)
    fun i(tag: String, message: String)
    fun d(tag: String, message: String)
    fun v(tag: String, message: String)
    fun e(tag: String, message: String, throwable: Throwable)
    fun w(tag: String, message: String, throwable: Throwable)
    fun i(tag: String, message: String, throwable: Throwable)
    fun d(tag: String, message: String, throwable: Throwable)
    fun v(tag: String, message: String, throwable: Throwable)
}

fun padTagWithSpace(tag: String, previousLongestTagLength: Int): Pair<String, Int> {
    val longestTagLength = max(previousLongestTagLength, tag.length + 1)
    return if (longestTagLength != tag.length) {
        TextPadder.padText(tag, longestTagLength, TextPadder.Pad.RIGHT, ' ') to longestTagLength
    } else {
        tag to longestTagLength
    }
}