package tar

import org.junit.Assert.assertEquals
import org.junit.Test
import tar.TarWrapper.XmlWrapper
import tar.TarWrapper.XmlWrapper.Companion.SEPARATOR
import tar.TarWrapper.XmlWrapper.Flag
import kotlin.test.assertTrue

class XmlWrapperTest {

    @Test
    fun wrapTest() {
        val name = "1.txt"
        wrapSimpleTest(name, "someText", "path/$name")
        wrapSimpleTest(name, "", "path/$name")
    }

    private fun wrapSimpleTest(name: String, someText: String, path: String) {
        val wrapper = XmlWrapper()
        assertEquals("<$name>$SEPARATOR$someText$SEPARATOR</$name>", wrapper.wrap(path, someText))
        assertEquals("<$name>$SEPARATOR$someText$SEPARATOR</$name>", wrapper.wrap(name, someText))
    }

    @Test
    fun unwrapWithFlagTest() {

        val wrapper = XmlWrapper(Flag.LAZY)

        val someText = "someText"

        val nameChildDataOne = "2.txt"
        val childDataOne = wrapper.wrap(nameChildDataOne, someText)

        val nameChildDataTwo = "3.txt"
        val childDataTwo = wrapper.wrap(nameChildDataTwo, someText)

        val dataName = "1.txt"
        val data = "$childDataOne$childDataTwo"
        val wrapText = wrapper.wrap(dataName, data)

        var result = wrapper.unWrap(wrapText)
        assertEquals(1, result.size)
        assertEquals(data, result[dataName])

        wrapper.unwrapFlag = Flag.ALL
        result = wrapper.unWrap(wrapText)
        assertEquals(2, result.size)
        assertEquals(someText, result[nameChildDataOne])
        assertEquals(someText, result[nameChildDataTwo])
    }

    @Test
    fun unwrapEmptyTest() {
        val wrapper = XmlWrapper(Flag.LAZY)
        val name = "1.txt"

        val wrapText = wrapper.wrap(name, "")
        val result = wrapper.unWrap(wrapText)

        assertEquals("", result[name])
    }

    @Test
    fun regexTest() {
        val pattern = XmlWrapper.pattern
        var nameData = "1>.txt"
        var text = "<$nameData>${SEPARATOR}1\nsd >112d ds <dsdf> xxcc*0/ \rs1_$SEPARATOR</$nameData>"
        var matcher = pattern.matcher(text)

        assertTrue(matcher.find())

        nameData = "<1.txt></1.txt>.txt"
        text = "<$nameData>${SEPARATOR}1\nsd >112d ds <dsdf> xxcc*0/ \rs1_${SEPARATOR}</$nameData>"
        matcher = pattern.matcher(text)

        assertTrue(matcher.find())
    }
}