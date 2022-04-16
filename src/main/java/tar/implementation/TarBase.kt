package tar.implementation

import org.apache.commons.io.Charsets.UTF_8
import org.apache.commons.io.FileUtils
import tar.ITar
import tar.TarWrapper
import java.io.File
import java.nio.charset.Charset


class TarBase(private val encoding: Charset = UTF_8, private val wrapper: TarWrapper) : ITar {
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

        other as TarBase

        if (encoding != other.encoding) return false

        return true
    }

    override fun hashCode(): Int {
        return encoding.hashCode()
    }

    override fun toString(): String {
        return "[${this.javaClass.name}]"
    }
}