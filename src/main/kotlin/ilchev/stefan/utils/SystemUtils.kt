package ilchev.stefan.utils

import java.io.File

val lineSeparator: String = System.lineSeparator()

val terminal = if (System.getProperty("os.name").startsWith("Windows")) "cmd /c" else "bash"

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
	val exitValue = process.waitFor()
	val result = process.inputStream
			.bufferedReader()
			.use { it.readText() }
	if (exitValue != 0) {
		throw Exception("exitValue = $exitValue$lineSeparator$result")
	}
	return result
}

fun executeTerminal(command: String) = execute("$terminal $command")

fun findMimeType(path: String) = executeTerminal("""file -b --mime-type "$path"""")
