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

    @Option(name = "-all")
    private var allOpt: String? = null

    @Throws(CmdLineException::class,
            TarInvalidateMergeFilesException::class,
            TarInvalidateOutputFileException::class,
            TarMergeFilesNotChooseException::class)
    fun commandReader(args: Array<String>) {
        validateCommand(args)

        val flag = allOpt
                ?.let { TarWrapper.XmlWrapper.Flag.ALL }
                ?: TarWrapper.XmlWrapper.Flag.LAZY

        val tar = Tar.Base(wrapper = TarWrapper.XmlWrapper(flag))

        if (separateName != null) {
            tar.separateFile(separateName!!)
        } else {
            tar.mergeFiles(mergeNames!!, outputName!!)
        }

    }

    fun validateCommand(args: Array<String>) {
        clear()
        CmdLineParser(this).parseArgument(args.toMutableList())

        if (separateName != null) {

            mergeNames?.let { throw TarInvalidateMergeFilesException() }
            outputName?.let { throw TarInvalidateOutputFileException() }
        } else {
            mergeNames
                    ?.forEach { path ->
                        if (path.isEmpty()) throw TarMergeFilesNotChooseException()
                    }
                    ?: throw TarInvalidateMergeFilesException()

            outputName ?: throw TarInvalidateOutputFileException()
        }
    }

    fun clear() {
        mergeNames = null
        separateName = null
        outputName = null
        allOpt = null
    }

}