package tar

import org.apache.commons.io.Charsets
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

interface ITar {

    /**
     * Получает на вход пути к файлам, которые сливает
     */
    @Throws(IOException::class)
    fun mergeFiles(fileNames: Array<String>, outputFileName: String)

    /**
     * Получает на вход путь к файлу, который может разбить на составляющее
     */
    @Throws(IOException::class)
    fun separateFile(path: String)


    class TarBase(private val encoding: Charset = Charsets.UTF_8, private val wrapper: TarWrapper) : ITar {
        // Deprecated code это плохо, но под капотом либа его же и использует.
        // Поэтому причину диприкейта я не пойму? ибо альтернативы для диприкейта нет

        override fun mergeFiles(fileNames: Array<String>, outputFileName: String) {
            val builder = StringBuilder()
            fileNames.forEach { name ->
                val text = FileUtils.readFileToString(File(name), encoding)
                val wrapText = wrapper.wrap(name, text)
                builder.append(wrapText)
            }
            FileUtils.writeStringToFile(File(outputFileName), builder.toString(), encoding)
        }

        override fun separateFile(path: String) {
            val text = FileUtils.readFileToString(File(path), encoding)
            val textArray = wrapper.unWrap(text)
            textArray.forEach {
                val file = File(it.first)
                println(file)
                FileUtils.writeStringToFile(file, it.second, encoding)
            }
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            if (encoding != (other as TarBase).encoding) return false
            return true
        }

        override fun hashCode(): Int {
            return encoding.hashCode()
        }

        override fun toString(): String {
            return "[${this.javaClass.name}]"
        }
    }
}