package console

import org.kohsuke.args4j.CmdLineException

class Main {

    companion object{

        @JvmStatic
        fun main(args: Array<String>) {
            val console = Console()

            try {
                console.commandReader(args)
            } catch (ex: CmdLineException) {
                System.err.println(ex.message)
            }
        }

    }

}