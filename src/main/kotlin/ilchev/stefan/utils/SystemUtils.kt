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
		directory: File = File(".")) = start(command, directory)
		.apply { waitFor() }
		.inputStream
		.bufferedReader()
		.use { it.readText() }

fun findMimeType(path: String) = execute("""file -b --mime-type "$path"""")
