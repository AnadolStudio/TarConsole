package tar

sealed class TarException(message: String) : Exception(message)

class TarMergeFilesNotChooseException : TarException("Merge files not choose")

class TarInvalidateOutputFileException : TarException("Invalidate output file exception")

class TarInvalidateMergeFilesException : TarException("Invalidate merge files exception")
