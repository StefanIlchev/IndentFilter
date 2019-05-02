package ilchev.stefan.indentfilter

import org.junit.Assert
import org.junit.Test

class ConverterTest {

	@Test
	fun countIndentsTest00() {
		val actual = countIndents("", 4)
		val expected = 0
		Assert.assertEquals(expected, actual)
	}

	@Test
	fun countIndentsTest01() {
		val actual = countIndents("\t\t\t", 4)
		val expected = 0
		Assert.assertEquals(expected, actual)
	}

	@Test
	fun countIndentsTest02() {
		val actual = countIndents("\t\t\tz", 4)
		val expected = 3
		Assert.assertEquals(expected, actual)
	}

	@Test
	fun countIndentsTest03() {
		val actual = countIndents("            z", 4)
		val expected = 3
		Assert.assertEquals(expected, actual)
	}

	@Test
	fun countIndentsTest04() {
		val actual = countIndents(" \t  \t   \tz", 4)
		val expected = 3
		Assert.assertEquals(expected, actual)
	}

	@Test
	fun countIndentsTest05() {
		val actual = countIndents(" \t  \t   \t z", 4)
		val expected = 4
		Assert.assertEquals(expected, actual)
	}

	@Test
	fun collapseTest00() {
		val actual = collapse("", 4)
		val expected = ""
		Assert.assertEquals(expected, actual)
	}

	@Test
	fun collapseTest01() {
		val actual = collapse("\t\t\t", 4)
		val expected = ""
		Assert.assertEquals(expected, actual)
	}

	@Test
	fun collapseTest02() {
		val actual = collapse("\t\t\tz", 4)
		val expected = "\t\t\tz"
		Assert.assertEquals(expected, actual)
	}

	@Test
	fun collapseTest03() {
		val actual = collapse("            z", 4)
		val expected = "\t\t\tz"
		Assert.assertEquals(expected, actual)
	}

	@Test
	fun collapseTest04() {
		val actual = collapse(" \t  \t   \tz", 4)
		val expected = "\t\t\tz"
		Assert.assertEquals(expected, actual)
	}

	@Test
	fun collapseTest05() {
		val actual = collapse(" \t  \t   \t z", 4)
		val expected = "\t\t\t\tz"
		Assert.assertEquals(expected, actual)
	}

	@Test
	fun expandTest00() {
		val actual = expand("", 4)
		val expected = ""
		Assert.assertEquals(expected, actual)
	}

	@Test
	fun expandTest01() {
		val actual = expand("\t\t\t", 4)
		val expected = ""
		Assert.assertEquals(expected, actual)
	}

	@Test
	fun expandTest02() {
		val actual = expand("\t\t\tz", 4)
		val expected = "            z"
		Assert.assertEquals(expected, actual)
	}

	@Test
	fun expandTest03() {
		val actual = expand("            z", 4)
		val expected = "            z"
		Assert.assertEquals(expected, actual)
	}

	@Test
	fun expandTest04() {
		val actual = expand(" \t  \t   \tz", 4)
		val expected = "            z"
		Assert.assertEquals(expected, actual)
	}

	@Test
	fun expandTest05() {
		val actual = expand(" \t  \t   \t z", 4)
		val expected = "                z"
		Assert.assertEquals(expected, actual)
	}
}
