package ilchev.stefan.indentfilter

import difflib.DiffUtils
import ilchev.stefan.utils.copyTo
import ilchev.stefan.utils.copyToStorage
import ilchev.stefan.utils.printLines
import ilchev.stefan.utils.readTextLines
import java.io.File
import java.io.InputStream
import java.io.PrintStream

fun smudge(lines: List<String>,
		operator: (String) -> String): List<String> {
	val result = lines.toMutableList()
	result.replaceAll(operator)
	return result
}

fun clean(lines: List<String>,
		head: List<String>,
		smudgeOperator: (String) -> String,
		cleanOperator: (String) -> String): List<String> {
	val smudged = smudge(head, smudgeOperator)
	val diff = DiffUtils.diff(smudged, lines)
	for (delta in diff.deltas) {
		val original = delta.original
		val originalPosition = original.position
		val originalLines = original.lines
		for (i in originalLines.indices) {
			originalLines[i] = head[originalPosition + i]
		}
		delta.revised.lines.replaceAll(cleanOperator)
	}
	return diff.applyTo(head)
}

fun gitSmudge(repo: String,
		path: String,
		type: String,
		size: Int,
		input: InputStream,
		output: PrintStream) {
	val file = input.copyToStorage(repo, "smudge", path)
	val lines = file.readTextLines()
	if (lines == null) {
		file.copyTo(output)
		file.delete()
		return
	}
	val operator = Converter(size)
			.getOperator(type)
	val smudged = smudge(lines, operator)
	output.printLines(smudged)
}

fun gitClean(repo: String,
		path: String,
		smudgeType: String,
		smudgeSize: Int,
		cleanType: String,
		cleanSize: Int,
		input: InputStream,
		output: PrintStream) {
	val file = input.copyToStorage(repo, "clean", path)
	val lines = file.readTextLines()
	if (lines == null) {
		file.copyTo(output)
		file.delete()
		return
	}
	file.delete()
	val headFile = File("$repo/smudge/$path")
	val head = headFile.readTextLines() ?: emptyList()
	val smudgeOperator = Converter(smudgeSize)
			.getOperator(smudgeType)
	val cleanOperator = Converter(cleanSize)
			.getOperator(cleanType)
	val cleaned = clean(lines, head, smudgeOperator, cleanOperator)
	output.printLines(cleaned)
}
