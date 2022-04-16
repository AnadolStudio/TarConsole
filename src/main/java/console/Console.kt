package console

import org.kohsuke.args4j.Argument
import org.kohsuke.args4j.CmdLineException
import org.kohsuke.args4j.CmdLineParser
import org.kohsuke.args4j.Option
import org.kohsuke.args4j.spi.StringArrayOptionHandler
import tar.*


class Console {
    @Argument(metaVar = "mergeNames", handler = StringArrayOptionHandler::class, usage = "File names to merge")
    private var mergeNames: Array<String>? = null

    @Option(name = "-u", metaVar = "separateName", usage = "Tarred file to separate")
    private var separateName: String? = null

    @Option(name = "-out", metaVar = "outputName", usage = "Output file")
    private var outputName: String? = null

    @Throws(CmdLineException::class)
    fun commandReader(args: Array<String?>) {
        val parser = CmdLineParser(this)
        parser.parseArgument(args.toMutableList())
        val tar = ITar.TarBase(wrapper = TarWrapper.XmlWrapper())
        try {
            if (separateName != null) {

                mergeNames?.let { throw TarInvalidateMergeFilesException() }
                outputName?.let { throw TarInvalidateOutputFileException() }

                tar.separateFile(separateName!!)
            } else {
                mergeNames?.apply { if (isEmpty()) throw TarMergeFilesNotChooseException() }
                    ?: throw TarInvalidateMergeFilesException()
                outputName ?: throw TarInvalidateOutputFileException()

                tar.mergeFiles(mergeNames!!, outputName!!)
            }

        } catch (ex: TarException) {
            System.err.println(ex.message)
        }
    }
}