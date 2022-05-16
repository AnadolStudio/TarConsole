package tar

import java.util.regex.Matcher
import java.util.regex.Pattern

interface TarWrapper {

    fun wrap(path: String, text: String): String

    /**
     * Возвращает массив пар, где первый объект - имя, второй - данные(текст)
     */
    fun unWrap(text: String): LinkedHashMap<String, String>

    class XmlWrapper(var unwrapFlag: Flag = Flag.LAZY) : TarWrapper {

        companion object{
            const val SEPARATOR = '\u0000'

            val pattern: Pattern = Pattern.compile(
                    "(<(.+\\.txt)>$SEPARATOR(.*)$SEPARATOR</(\\2)>)",
                    Pattern.DOTALL or Pattern.UNIX_LINES or Pattern.MULTILINE
            )
            /*val pattern: Pattern = Pattern.compile(
                    "(<(.+\\.txt)>(.*)</(\\2)>)",
                    Pattern.DOTALL or Pattern.UNIX_LINES or Pattern.MULTILINE
            )*/

        }

        /**
         * ALL Unwrap для всех вложенных блоков
         * LAZY Unwrap только для блоков верхнего уровня
         */
        enum class Flag { ALL, LAZY }

        override fun wrap(path: String, text: String): String = StringBuilder().apply {
            val name = path.substring(path.lastIndexOf('/') + 1)

            append("<$name>")
            append("$SEPARATOR$text$SEPARATOR")
            append("</$name>")
        }.toString()

        override fun unWrap(text: String): LinkedHashMap<String, String> {
            val result = linkedMapOf<String, String>()
            val matcher = matcher(text)

            while (matcher.find()) {
                var path = matcher.group(2)
                val data = deleteExtremeParagraphs(matcher.group(3))

                if (result[path] != null) {
                    path = "${System.currentTimeMillis()}$path"
                }

                result[path] = data

                if (unwrapFlag == Flag.ALL) {
                    val child = unWrap(data)

                    if (child.isNotEmpty()) {
                        result.remove(path)
                        result.putAll(child)
                    }
                }
            }

            return result
        }

        private fun matcher(text: String): Matcher = pattern.matcher(text)

        private fun deleteExtremeParagraphs(text: String): String {
            var result = text

            if (result.first() == SEPARATOR) {
                result = result.removeRange(0, 1)
            }

            if (result.last() == SEPARATOR) {
                result = result.removeRange(result.lastIndex, result.length)
            }

            return result
        }
    }
}