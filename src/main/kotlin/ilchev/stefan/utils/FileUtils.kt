package ilchev.stefan.utils

import ilchev.stefan.indentfilter.Converter
import java.io.File
import java.io.OutputStream

val thisJar by lazy {
	val anyClass = Converter::class.java
	File(anyClass.protectionDomain
			.codeSource
			.location
			.toURI())
}

val thisWorkspace by lazy {
	val result = File(thisJar.parent, thisJar.nameWithoutExtension)
	result.mkdir()
	result
}

fun File.readTextLines() = if (isFile) {
	val mimeType = findMimeType(canonicalPath)
	if (mimeType.startsWith("text/")) readLines() else null
} else {
	null
}

fun File.copyTo(output: OutputStream) = inputStream().use {
	it.copyTo(output)
}
