package ilchev.stefan.utils

import java.io.File

fun start(command: String,
		directory: File = File(".")): Process {
	val regex = Regex("""\s(?=(?:[^"]*["][^"]*["])*[^"]*$)""")
	val parts = command.split(regex)
			.toTypedArray()
	return ProcessBuilder(*parts)
			.directory(directory)
			.redirectOutput(ProcessBuilder.Redirect.PIPE)
			.redirectError(ProcessBuilder.Redirect.PIPE)
			.start()
}

fun execute(command: String,
		directory: File = File(".")): String {
	val process = start(command, directory)
	process.waitFor()
	val result = process.inputStream
			.bufferedReader()
			.use { it.readText() }
	return if (process.exitValue() == 0) result else throw Exception(result)
}

fun findMimeType(path: String) = execute("""file -b --mime-type "$path"""")
