@file:JvmName("Main")

package ilchev.stefan.indentfilter

import ilchev.stefan.utils.thisJar
import ilchev.stefan.utils.thisWorkspace
import java.io.File
import java.nio.file.Files
import java.util.*

const val SWITCH_SMUDGE_TYPE = "smudge-type"

const val SWITCH_SMUDGE_SIZE = "smudge-size"

const val SWITCH_SMUDGE_DEBUG = "smudge-debug"

const val SWITCH_CLEAN_TYPE = "clean-type"

const val SWITCH_CLEAN_SIZE = "clean-size"

const val SWITCH_CLEAN_DEBUG = "clean-debug"

const val SWITCH_DEBUG = "debug"

fun createSwitchEntry(switchKey: String, switchValue: String) = """--$switchKey="$switchValue""""

fun createDebugSwitchEntry(path: String): String {
	val switchValue = path.replace('\\', '/')
	return createSwitchEntry(SWITCH_DEBUG, switchValue)
}

fun Array<String>.findSwitchValue(switchKey: String, switchValueDefault: String? = null): String? {
	val switchPrefix = "--$switchKey"
	for (arg in this) {
		if (!arg.startsWith(switchPrefix)) {
			continue
		}
		val equalsIndex = switchPrefix.length
		if (arg.length == equalsIndex) {
			return ""
		}
		if (arg[equalsIndex] != '=') {
			throw IllegalArgumentException()
		}
		return arg.substring(equalsIndex + 1)
				.removeSurrounding("\"")
	}
	return switchValueDefault
}

fun mainGitConfig(args: Array<String>) {
	val dir = thisWorkspace.toPath()
	val repo = Files.createTempDirectory(dir, null)
			.toFile()
	val repoPath = repo.canonicalPath.replace('\\', '/')
	val thisJarPath = thisJar.canonicalPath.replace('\\', '/')
	val smudgeType = args.findSwitchValue(SWITCH_SMUDGE_TYPE, "collapse")!!
	val smudgeSize = args.findSwitchValue(SWITCH_SMUDGE_SIZE, "$INDENT_SIZE_DEFAULT")!!
			.toInt()
	val cleanType = args.findSwitchValue(SWITCH_CLEAN_TYPE, "expand")!!
	val cleanSize = args.findSwitchValue(SWITCH_CLEAN_SIZE, "$INDENT_SIZE_DEFAULT")!!
			.toInt()
	var smudgeArgs = """git smudge "$repoPath" "%f" $smudgeType $smudgeSize"""
	var cleanArgs = """git clean "$repoPath" "%f" $smudgeType $smudgeSize $cleanType $cleanSize"""
	val smudgePath = args.findSwitchValue(SWITCH_SMUDGE_DEBUG)
	if (smudgePath != null) {
		smudgeArgs += " ${createDebugSwitchEntry(smudgePath)}"
	}
	val cleanPath = args.findSwitchValue(SWITCH_CLEAN_DEBUG)
	if (cleanPath != null) {
		cleanArgs += " ${createDebugSwitchEntry(cleanPath)}"
	}
	val config = """.git/config:
		|[filter "${thisWorkspace.name}"]
		|	smudge = java -jar "$thisJarPath" $smudgeArgs
		|	clean = java -jar "$thisJarPath" $cleanArgs
		|	required = true
		|[merge]
		|	renormalize = true"""
			.trimMargin()
	System.out.println(config)
	System.out.println()
	val attributes = """.git/info/attributes:
		|* filter=${thisWorkspace.name}"""
			.trimMargin()
	System.out.println(attributes)
}

fun mainGitSmudge(args: Array<String>) {
	val repo = args[2]
	val path = args[3]
	val type = args[4]
	val size = args[5].toInt()
	gitSmudge(repo, path, type, size, System.`in`, System.out)
}

fun mainGitClean(args: Array<String>) {
	val repo = args[2]
	val path = args[3]
	val smudgeType = args[4]
	val smudgeSize = args[5].toInt()
	val cleanType = args[6]
	val cleanSize = args[7].toInt()
	gitClean(repo, path, smudgeType, smudgeSize, cleanType, cleanSize, System.`in`, System.out)
}

fun mainDebug(args: Array<String>, error: Throwable?) {
	val path = args.findSwitchValue(SWITCH_DEBUG) ?: return
	val writer = File(path)
			.printWriter()
	writer.use {
		val argsString = Arrays.toString(args)
		it.println(argsString)
		error?.printStackTrace(it)
	}
}

fun main(args: Array<String>) {
	var error: Throwable? = null
	try {
		when (args[0]) {
			"git" -> when (args[1]) {
				"config" -> mainGitConfig(args)
				"smudge" -> mainGitSmudge(args)
				"clean" -> mainGitClean(args)
				else -> throw IllegalArgumentException()
			}
			else -> throw IllegalArgumentException()
		}
	} catch (t: Throwable) {
		error = t
		throw t
	} finally {
		mainDebug(args, error)
	}
}
