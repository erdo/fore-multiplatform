package co.early.fore.kt.core.utils.text


/**
 * Basic class which only deals with Left to Right text and will work for Monospaced fonts only
 */
@ExperimentalStdlibApi
object BasicTextWrapper {
    private const val LINE_BREAK = "\n"
    const val SPACE = " "
    const val EMPTY = ""

    /**
     * Wrap fullText so that it will fit in charactersAvailable,
     * over however many lines it takes
     *
     *
     * Attempts to wrap on spaces between words if possible, if not will break mid-word
     *
     *
     * Honours the line break '\n'
     *
     *
     * Does not honour large spaces (made up of multiple space characters in a row) - all spaces between
     * words will return as single spaced
     *
     * @param fullText text that needs to be wrapped
     * @param charactersAvailable has to be larger than 0
     * @return lines of text, such that each line of text fits inside the charactersAvailable, any line
     * breaks in the original string will be converted to a empty string item in the list
     */
    fun wrapMonospaceText(fullText: String, charactersAvailable: Int): List<String> {
        if (charactersAvailable <= 0) {
            throw IllegalArgumentException("charactersAvailable needs to be larger than 0, charactersAvailable:$charactersAvailable")
        }

        //separate any line breaks out
        val linesWithExtractedLineBreaks = mutableListOf<String>()
        expandLineBreaks(linesWithExtractedLineBreaks, fullText)
        val finalLines = mutableListOf<String>()
        for (line in linesWithExtractedLineBreaks) {
            if (EMPTY == line) {
                finalLines.add(line)
            } else {
                val trimmedLine = line.trim { it <= ' ' }
                if (EMPTY == trimmedLine) {
                    finalLines.add(SPACE)
                } else {

                    //take the line with no line breaks in it and then wrap that
                    val words = line.split(SPACE)

                    extractWrappedLines(
                        finalLines,
                        words.toMutableList(),
                        charactersAvailable
                    )
                }
            }
        }
        return finalLines
    }

    /**
     * Takes a string of text, replacing any '\n' line breaks with new lines which are added to the output list
     *
     * @param lines
     * @param lineWhichMayIncludeLineBreaks
     */
    private fun expandLineBreaks(
        lines: MutableList<String>,
        lineWhichMayIncludeLineBreaks: String
    ) {
        val positionOfLineBreak =
            lineWhichMayIncludeLineBreaks.indexOf(LINE_BREAK)
        if (positionOfLineBreak < 0) { //no line breaks present
            lines.add(lines.size, lineWhichMayIncludeLineBreaks)
        } else { //has a line break in the word
            lines.add(lines.size, lineWhichMayIncludeLineBreaks.substring(0, positionOfLineBreak))
            expandLineBreaks(
                lines,
                lineWhichMayIncludeLineBreaks.substring(positionOfLineBreak + 1, lineWhichMayIncludeLineBreaks.length)
            )
        }
    }

    /**
     * Recursive method, takes a list of words and concatenates them in order and with spaces in between each word, up to a maximum line width of widthAvailable,
     * further lines are added to linesSoFar until all the wordsStillToWrap have been processed.
     *
     * @param linesSoFar
     * @param wordsStillToWrap
     */
    private fun extractWrappedLines(
        linesSoFar: MutableList<String>,
        wordsStillToWrap: MutableList<String>,
        charactersAvailable: Int
    ) {
        if (wordsStillToWrap.size > 0) {

            //start by assuming all the words still to wrap will wrap into one line
            val nextLineAttempt =
                flattenWordsAddingSpaces(wordsStillToWrap)
            var charactersThatFit =
                if (nextLineAttempt.length < charactersAvailable) nextLineAttempt.length else charactersAvailable
            if (charactersThatFit == nextLineAttempt.length) { //we're done
                linesSoFar.add(nextLineAttempt)
                wordsStillToWrap.clear()
            } else { //just get the next line, then pass the remaining words to the method again recursively
                val wordsForNextLine = mutableListOf<String>()
                var charactersUsed =
                    -1 //to take into account that there is no space before the first word in a line
                while (wordsStillToWrap.size > 0 && charactersUsed + 1 + wordsStillToWrap[0].length <= charactersThatFit) {
                    wordsForNextLine.add(wordsStillToWrap[0])
                    charactersUsed += 1 + wordsStillToWrap[0].length
                    wordsStillToWrap.removeAt(0)
                }
                if (charactersUsed < 1) { //the next word must be very large so we need to take what we can off the front of it
                    val massiveWord = wordsStillToWrap[0]
                    if (charactersThatFit == 0) { //just in case
                        charactersThatFit = 1
                    }
                    wordsForNextLine.add(massiveWord.substring(0, charactersThatFit))
                    wordsStillToWrap.removeAt(0)
                    if (massiveWord.length > charactersThatFit) {
                        wordsStillToWrap.add(0, massiveWord.substring(charactersThatFit, massiveWord.length))
                    }
                }
                linesSoFar.add(flattenWordsAddingSpaces(wordsForNextLine))
                extractWrappedLines(
                    linesSoFar,
                    wordsStillToWrap,
                    charactersAvailable
                )
            }
        }
    }

    private fun flattenWordsAddingSpaces(words: List<String>): String {
        val stringBuilder = StringBuilder()
        for (word in words) {
            stringBuilder.append(word)
            stringBuilder.append(" ")
        }
        if (words.isNotEmpty()) {
            //TODO currently experimental API
            stringBuilder.deleteAt(stringBuilder.length - 1)
        }
        return stringBuilder.toString()
    }
}