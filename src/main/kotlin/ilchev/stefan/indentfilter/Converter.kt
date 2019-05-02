package ilchev.stefan.indentfilter

const val INDENT_SIZE_DEFAULT = 4

fun countIndents(line: String,
		indentSize: Int = INDENT_SIZE_DEFAULT
): Int {
	if (indentSize < 1) {
		return 0
	}
	var indentsCount = 0
	var count = 0
	for (c in line) {
		count = when (c) {
			' ' -> (count + 1) % indentSize
			'\t' -> 0
			else -> return if (count > 0) indentsCount + 1 else indentsCount
		}
		if (count < 1) {
			++indentsCount
		}
	}
	return 0
}

fun collapse(line: String,
		indentSize: Int = INDENT_SIZE_DEFAULT
): String {
	val indentsCount = countIndents(line, indentSize)
	val indent = "\t".repeat(indentsCount)
	return indent + line.trim()
}

fun expand(line: String,
		indentSize: Int = INDENT_SIZE_DEFAULT
): String {
	val indentsCount = countIndents(line, indentSize)
	val indent = " ".repeat(indentSize)
			.repeat(indentsCount)
	return indent + line.trim()
}

class Converter(private val indentSize: Int = INDENT_SIZE_DEFAULT) {

	private fun collapse(line: String) = collapse(line, indentSize)

	private fun expand(line: String) = expand(line, indentSize)

	fun getOperator(name: String): (String) -> String = when (name) {
		"collapse" -> ::collapse
		"expand" -> ::expand
		else -> throw IllegalArgumentException()
	}
}
