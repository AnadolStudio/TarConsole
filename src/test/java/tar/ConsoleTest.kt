package tar

import console.Main
import org.apache.commons.io.Charsets
import org.apache.commons.io.FileUtils
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File

class ConsoleTest {
    companion object {
        const val path = "C:\\Users\\tereh\\Desktop\\My_Projects\\Java\\TarConsole\\src\\main\\resources\\"
    }

    @Test
    fun consoleWrapTest() {
        val sample1 = "${path}SimpleSample\\Sample1.txt"
        val sample2 = "${path}SimpleSample\\Sample2.txt"
        val sample3 = "${path}Sample3.txt"
        val outFile = "${path}OutText.txt"

        val command = arrayOf(sample1, sample2, "-out", outFile)
        Main.main(command)
        val outFileText = FileUtils.readFileToString(File(outFile), Charsets.UTF_8)
        val sample3Text = FileUtils.readFileToString(File(sample3), Charsets.UTF_8)
        assertEquals(outFileText.replace("\r", ""), sample3Text.replace("\r", ""))
        //Кажется, FileUtils - добавляет \r в начало каждой строки, визуально различий в файлах нет, но тесты не проходят
        // для этого делаю replace
    }

    @Test
    fun consoleUnwrapTest() {

        val sample1 = "${path}SimpleSample\\Sample1.txt"
        val sample2 = "${path}SimpleSample\\Sample2.txt"
        val outFile = "${path}OutText.txt"

        val command = arrayOf(sample1, sample2, "-out", outFile)
        Main.main(command)
//        val sample1Text = FileUtils.readFileToString(File(sample1), Charsets.UTF_8)
//        val sample2Text = FileUtils.readFileToString(File(sample2), Charsets.UTF_8)
//        assertEquals()
    }
}