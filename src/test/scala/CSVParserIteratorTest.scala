import org.scalatest.{FunSuite, Matchers}
import csvparser.CSVParserIterator


class CSVParserIteratorTest extends FunSuite with Matchers {
  test("tets CSVParserIterator newline in value") {
    val csv = "abc,\"de\nf\",ghi\nabc,\"def\n\",ghi\n\"abc\n,\"onetwo,three,doremi"

    val parser = new CSVParserIterator(io.Source.fromString(csv).toBuffer.iterator)

    val test_out = parser.toList.map(row => row.map(value => value.getOrElse("None")))
    val real_output = List(
      List("abc", "de\nf", "ghi"),
      List("abc", "def\n", "ghi"),
      List("\"abc\n,\"onetwo", "three", "doremi"))

    assert(real_output === test_out)
  }

  test("tets CSVParserIterator broken quotes in value") {
    val csv = "\"abc,\"onetwo,three,doremi"

    val parser = new CSVParserIterator(io.Source.fromString(csv).toBuffer.iterator)

    val test_out = parser.toList.map(row => row.map(value => value.getOrElse("None")))
    val real_output = List(
      List("\"abc,\"onetwo", "three", "doremi"))

    assert(real_output === test_out)
  }

  test("tets CSVParserIterator empty values") {
    val csv = ",,\naaa,,\n,,aaa"

    val parser = new CSVParserIterator(io.Source.fromString(csv).toBuffer.iterator)

    val test_out = parser.toList.map(row => row.map(value => value.getOrElse("None")))
    val real_output = List(
      List("None", "None", "None"),
      List("aaa", "None", "None"),
      List("None", "None", "aaa"))

    assert(real_output === test_out)
  }

  test("tets CSVParserIterator quoted value") {
    val csv = "abc,\"a,b,c\",c"

    val parser = new CSVParserIterator(io.Source.fromString(csv).toBuffer.iterator)

    val test_out = parser.toList.map(row => row.map(value => value.getOrElse("None")))
    val real_output = List(
      List("abc", "a,b,c", "c"),
     )

    assert(real_output === test_out)
  }

  test("tets CSVParserIterator quotingChar ` ") {
    val csv = "abc,`def`,ghi\nabc,`de\nf`,ghi\nabc,`def`,ghi\na,`b,c,d`,e\na,b,\n`abc,`onetwo,three,doremi\n,,\n"

    val parser = new CSVParserIterator(
      io.Source.fromString(csv).toBuffer.iterator,
      quotingChar = '`')

    val test_out = parser.toList.map(row => row.map(value => value.getOrElse("None")))
    val real_output = List(
      List("abc", "def", "ghi"),
      List("abc", "de\nf", "ghi"),
      List("abc", "def", "ghi"),
      List("a", "b,c,d", "e"),
      List("a", "b", "None"),
      List("`abc,`onetwo", "three", "doremi"),
      List("None", "None", "None"))

    assert(real_output === test_out)
  }

  test("tets CSVParserIterator seperator ~ ") {
    val csv = "abc,`def`,ghi~abc,`de~f`,ghi~abc,`def`,ghi~a,`b,c,d`,e~a,b,~`abc,`onetwo,three,doremi~,,~"

    val parser = new CSVParserIterator(
      io.Source.fromString(csv).toBuffer.iterator,
      quotingChar = '`',
      separator = '~')

    val test_out = parser.toList.map(row => row.map(value => value.getOrElse("None")))
    val real_output = List(
      List("abc", "def", "ghi"),
      List("abc", "de~f", "ghi"),
      List("abc", "def", "ghi"),
      List("a", "b,c,d", "e"),
      List("a", "b", "None"),
      List("`abc,`onetwo", "three", "doremi"),
      List("None", "None", "None"))

    assert(real_output === test_out)
  }

  test("tets CSVParserIterator delimiter \\t ") {
    val csv = "abc\t`def`\tghi~abc\t`de~f`\tghi~abc\t`def`\tghi~a\t`b\tc\td`\te~a\tb\t~`abc\t`onetwo\tthree\tdoremi~\t\t~"

    val parser = new CSVParserIterator(
      io.Source.fromString(csv).toBuffer.iterator,
      quotingChar = '`',
      separator = '~',
      delimiter = '\t')

    val test_out = parser.toList.map(row => row.map(value => value.getOrElse("None")))
    val real_output = List(
      List("abc", "def", "ghi"),
      List("abc", "de~f", "ghi"),
      List("abc", "def", "ghi"),
      List("a", "b\tc\td", "e"),
      List("a", "b", "None"),
      List("`abc\t`onetwo", "three", "doremi"),
      List("None", "None", "None"))

    assert(real_output === test_out)
  }

  test("tets CSVParserIterator empty stream") {
    val csv = ""

    val parser = new CSVParserIterator(io.Source.fromString(csv).toBuffer.iterator)

    val test_out = parser.toList.map(row => row.map(value => value.getOrElse("None")))
    val real_output = List()

    assert(real_output === test_out)
  }

}
