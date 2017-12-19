package csvparser

import scala.collection.mutable.ListBuffer


class CSVParserIterator(stream: Iterator[Char],
                        quotingChar: Char = '\"',
                        separator: Char = '\n',
                        delimiter: Char = ',') extends Iterator[List[Option[String]]] {

  val value = new StringBuilder
  val row = new ListBuffer[Option[String]]
  var prevChar: Option[Char] = None
  var quotes = false

  def deserialize_value(value: String): Option[String] = {
    value match {
      case "" => None
      case v =>
        if (v(0) == quotingChar && v(value.length - 1) == quotingChar)
          Some(v.substring(1, value.length - 1))
        else Some(v)
    }
  }

  def hasNext(): Boolean = {
    stream.hasNext
  }

  def next(): List[Option[String]] = {
    value.clear()
    prevChar = None
    row.clear()
    quotes = false

    while (stream.hasNext) {
      val c = stream.next()

      c match {
        case `delimiter` => // might have save to row
          if (quotes) {     // if inside quoted value, keep picking up chars
            value += delimiter
            prevChar = Some(c)
          } else {          // end of value, save to row
            row += deserialize_value(value.toString()) // add current value
            value.clear()
            prevChar = None
          }

        case `quotingChar` =>
          if (prevChar.isDefined && prevChar.get == '\\') { // if quote was escaped, continue
            value += quotingChar
            prevChar = Some(c)
          } else {
            value += quotingChar
            quotes = !quotes   // flip quotes state
          }

        case `separator` =>
          if (quotes) {        // if inside quoted value, keep to pick up chars
            value += separator
            prevChar = Some(c)
          } else {             // end of row, return row
            row += deserialize_value(value.toString())
            return row.toList  // yield row
          }

        case otherChar =>
          value += otherChar
          prevChar = Some(c)

      }
    }
    row += deserialize_value(value.toString())

    row.toList
  }

}