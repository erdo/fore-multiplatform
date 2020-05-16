package co.early.fore.kt.core.utils.text

import co.early.fore.kt.core.utils.text.TextPadder.Pad.LEFT

object TextPadder {
    @ExperimentalStdlibApi
    fun padText(
        textOriginal: String,
        desiredLength: Int,
        pad: Pad,
        paddingCharacter: Char
    ): String {
        val paddingCharactersRequired = desiredLength - textOriginal.length
        if (paddingCharactersRequired < 0) {
            throw IllegalArgumentException("textOriginal is already longer than the desiredLength, textOriginal.length():" + textOriginal.length + " desiredLength:" + desiredLength)
        }
        val sb = StringBuilder()
        for (ii in 0 until paddingCharactersRequired) {
            sb.append(paddingCharacter)
        }
        if (pad == LEFT) {
            sb.append(textOriginal)
        } else {
            //TODO insert is experimental at the moment
            sb.insert(0, textOriginal)
        }
        return sb.toString()
    }

    enum class Pad {
        LEFT, RIGHT
    }
}