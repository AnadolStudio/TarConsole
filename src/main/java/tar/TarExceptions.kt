package tar

sealed class TarException(message: String) : Exception(message)

class TarMergeFilesNotChooseException : TarException("Merge files not choose")

class TarInvalidateOutputFileException : TarException("Invalidate output file exception") // TODO if !txt

class TarInvalidateMergeFilesException : TarException("Invalidate merge files exception")

//class TarIOException():
