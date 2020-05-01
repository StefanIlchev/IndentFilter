package ilchev.stefan.utils

import java.io.File

val lineSeparator: String = System.lineSeparator()

val isWindows = System.getProperty("os.name").startsWith("Windows")

fun createProcessBuilder(command: Array<String>, directory: File = File(".")) = ProcessBuilder(*command).apply {
	directory(directory)
	redirectOutput(ProcessBuilder.Redirect.PIPE)
	redirectError(ProcessBuilder.Redirect.PIPE)
}

fun createProcessBuilder(command: String, directory: File = File(".")): ProcessBuilder {
	val regex = Regex("""\s(?=(?:[^"]*["][^"]*["])*[^"]*$)""")
	val parts = command.split(regex).toTypedArray()
	return createProcessBuilder(parts, directory)
}

fun ProcessBuilder.execute(): String = start().run {
	val exitValue = waitFor()
	val result = inputStream
			.bufferedReader()
			.use { it.readText() }
	if (exitValue == 0) result else throw Exception("exitValue = $exitValue$lineSeparator$result")
}

fun ProcessBuilder.execute(retriesCount: Int): String {
	var throwable: Throwable? = null
	for (attempt in 0..retriesCount) {
		try {
			return execute()
		} catch (t: Throwable) {
			throwable?.also { t.addSuppressed(it) }
			throwable = t
		}
	}
	throw throwable!!
}

fun executeTerminal(command: String, retriesCount: Int = 0) = if (isWindows) {
	createProcessBuilder("cmd /c $command")
} else {
	createProcessBuilder(arrayOf("bash", "-c", command))
}.execute(retriesCount)

fun findMimeType(path: String) = executeTerminal("""file -b --mime-type "$path"""")
