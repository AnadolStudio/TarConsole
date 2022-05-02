package tar

import console.Console
import console.Main
import org.apache.commons.io.Charsets
import org.apache.commons.io.FileUtils
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.jupiter.api.assertThrows
import java.io.File

class TarTest {

    @Test
    fun tarTest() {

        // WRAP
        val cl = this.javaClass.classLoader
        val sample1 = cl.getResource("SimpleSample/Sample1.txt")
        val sample2 = cl.getResource("SimpleSample/Sample2.txt")
        val sample3 = cl.getResource("Sample3.txt")
        val outFile = "src/main/resources/OutText.txt"

        var command = arrayOf<String>(sample1.path, sample2.path, "-out", outFile)
        Main.main(command)
        val outFileText = FileUtils.readFileToString(File(outFile), Charsets.UTF_8)
        val sample3Text = FileUtils.readFileToString(File(sample3.toURI()), Charsets.UTF_8)
        assertEquals(outFileText, sample3Text)

        //Unwrap
        val inputFile = cl.getResource("OutText.txt")
        command = arrayOf("-u", inputFile.path)
        Main.main(command)

        val expectedFile1 = File(cl.getResource("SimpleSample/Sample1.txt").toURI())
        val expectedFile2 = File(cl.getResource("SimpleSample/Sample2.txt").toURI())

        val expectedText1 = FileUtils.readFileToString(expectedFile1, Charsets.UTF_8)
        val expectedText2 = FileUtils.readFileToString(expectedFile2, Charsets.UTF_8)

        val resultText1 = FileUtils.readFileToString(File("Sample1.txt"), Charsets.UTF_8)
        val resultText2 = FileUtils.readFileToString(File("Sample2.txt"), Charsets.UTF_8)

        assertEquals(expectedText1, resultText1)
        assertEquals(expectedText2, resultText2)
    }

    @Test
    fun exceptionsTest() {
        val console = Console()

        //separate
        var command = arrayOf("", "-u", "outFile")
        assertThrows<TarInvalidateMergeFilesException> {
            console.validateCommand(command)
        }

        console.clear()
        command = arrayOf("-u", "separateFile","-out", "outFile")
        assertThrows<TarInvalidateOutputFileException> {
            console.validateCommand(command)
        }

        //merge
        console.clear()
        command = arrayOf("path1", "path2", "outFile")

        assertThrows<TarInvalidateOutputFileException> {
            console.validateCommand(command)
        }

        console.clear()
        command = arrayOf("-out", "outFile")

        assertThrows<TarInvalidateMergeFilesException> {
            console.validateCommand(command)
        }

        console.clear()
        command = arrayOf("", "-out", "outFile")

        assertThrows<TarMergeFilesNotChooseException> {
            console.validateCommand(command)
        }

    }

}