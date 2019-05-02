package ilchev.stefan.utils

import java.io.PrintStream

fun PrintStream.printLines(lines: List<String>) = lines.forEach {
	println(it)
}
