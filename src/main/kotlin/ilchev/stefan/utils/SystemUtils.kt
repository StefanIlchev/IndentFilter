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

fun findMimeType(path: String): String {
	val process = start("""file -b --mime-type "$path"""")
	process.waitFor()
	return process.inputStream
			.bufferedReader()
			.readText()
}
