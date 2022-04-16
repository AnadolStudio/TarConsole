package tar

import java.util.regex.Pattern


interface TarWrapper {

    fun wrap(name: String, text: String): String

    /**
     * Возвращает массив пар, где первый объект - имя, второй - данные(текст)
     */
    fun unWrap(text: String): List<Pair<String, String>>

    class XmlWrapper(private val unwrapflag: Flag = Flag.LAZY) : TarWrapper {
        /**
         * ALL Unwrap для всех вложенных блоков
         * LAZY Unwrap только для блоков верхнего уровня
         */
        enum class Flag { ALL, LAZY }

        override fun wrap(name: String, text: String): String = StringBuilder().apply {
            append("\n<$name>")
            append("\n$text\n")
            append("</$name>\n")
        }.toString()

        override fun unWrap(text: String): List<Pair<String, String>> {
            val result = mutableListOf<Pair<String, String>>()

            val pattern: Pattern = Pattern.compile("(\n<(.+\\.txt)>([.\\w\\D]*)</(\\2)>\n)")
            //Хоть в документации и написано, что "." - любой символ, но \n - под это не попадает
            // [.\w\D] - больше похоже на костыль, но лечит данную проблему
            val matcher = pattern.matcher(text.replace("\r", ""))

            while (matcher.find()) {
                val name = matcher.group(2)
                val data = deleteExtremeParagraphs(matcher.group(3))

                result.add(Pair(name, data))
                if (unwrapflag == Flag.ALL) {
                    val child = unWrap(data)
                    if (child.isNotEmpty()) {
                        result.removeAt(result.lastIndex)
                        result.addAll(child)
                    }
                }
            }

            return result
        }

        private fun deleteExtremeParagraphs(text: String): String {
            var result = text
            if (result.first() == '\n') {
                result = result.removeRange(0, 1)
            }
            if (result.last() == '\n') {
                result = result.removeRange(result.lastIndex, result.length)
            }
            return result
        }
    }
}