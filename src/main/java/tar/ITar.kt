package tar

import java.io.IOException

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
}