package ilchev.stefan.utils

import java.io.File
import java.io.InputStream

fun InputStream.copyToStorage(repo: String,
		type: String,
		path: String): File {
	val file = File("$repo/$type/$path")
	file.parentFile
			?.mkdirs()
	file.outputStream().use {
		copyTo(it)
	}
	return file
}
