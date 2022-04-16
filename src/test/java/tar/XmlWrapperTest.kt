package tar

import org.junit.Assert.assertEquals
import org.junit.Test
import tar.TarWrapper.XmlWrapper
import tar.TarWrapper.XmlWrapper.Flag
import java.util.regex.Pattern
import kotlin.test.assertTrue

class XmlWrapperTest {

    @Test
    fun xmlWrapTest() {
        val name = "1.txt"
        val someText = "33333"
        val wrapper = XmlWrapper()
        assertEquals("\n<$name>\n$someText\n</$name>\n", wrapper.wrap(name, someText))
    }

    @Test
    fun xmlUnwrapTest() {
        var wrapper = XmlWrapper()

        val dataName = "1.txt"
        val nameChildDataOne = "2.txt"
        val nameChildDataTwo = "3.txt"

        val someText = "33333"
        val childDataOne = wrapper.wrap(nameChildDataOne, someText)
        val childDataTwo = wrapper.wrap(nameChildDataTwo, someText)
        val data = "$childDataOne$childDataTwo"
        val text = wrapper.wrap(dataName, data)

        var result = wrapper.unWrap(text)
        assertEquals(1, result.size)
        assertEquals(Pair(dataName, data), Pair(result[0].first, result[0].second))

        wrapper = XmlWrapper(Flag.ALL)

        result = wrapper.unWrap(text)
        assertEquals(2, result.size)
        assertEquals(Pair(nameChildDataOne, someText), Pair(result[0].first, result[0].second))
        assertEquals(Pair(nameChildDataTwo, someText), Pair(result[1].first, result[1].second))
    }

    @Test
    fun simpleTest() {
        val pattern: Pattern = Pattern.compile("<(.+\\.txt)>([.\\w\\D]*)</(\\1)>")

        val nameData = "1.txt"
        val text = "<$nameData>1 \nsd 112d ds  xxcc*0/ \rs1_</$nameData>"
        val matcher = pattern.matcher(text)

        assertTrue(matcher.find())
    }
}