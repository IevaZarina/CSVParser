package csvparser

import scala.collection.mutable.ListBuffer


class CSVParserIterator(stream: Iterator[Char],
                        quotingChar: Char = '\"',
                        seperator: Char = '\n',
                        delimiter: Char = ',') extends Iterator[List[Option[String]]] {


  def deserialize_value(value: String): Option[String] = {
    value match {
      case "" => None
      case v =>
        if (v(0) == quotingChar && v(value.length - 1) == quotingChar)
          Some(v.stripSuffix(quotingChar.toString).stripPrefix(quotingChar.toString))
        else Some(v)
    }
  }

  def hasNext(): Boolean = {
    stream.hasNext
  }

  def next(): List[Option[String]] = {
    var value = ""
    var prevChar = ""
    var row = new ListBuffer[Option[String]]
    var quotes = false

    while (stream.hasNext) {
      val c = stream.next()

      c match {
        case `delimiter` => // might have save to row
          if (quotes) { // if inside quoted value, keep to pick up chars
            value += delimiter
            prevChar = delimiter.toString
          } else { // end of value, save to row
            row += deserialize_value(value) // add current value
            value = ""
            prevChar = ""
          }

        case `quotingChar` =>
          if (prevChar == "\\") { // if quote was escaped, continue
            value += quotingChar
            prevChar = quotingChar.toString
          } else {
            value += quotingChar
            quotes = !quotes // flip quotes state
          }

        case `seperator` =>
          if (quotes) { // if inside quoted value, keep to pick up chars
            value += seperator
            prevChar = seperator.toString
          } else { // end of row, return row
            row += deserialize_value(value)
            return row.toList // yield row
          }

        case otherChar =>
          value += otherChar
          prevChar = otherChar.toString

      }
    }
    row += deserialize_value(value)

    row.toList
  }

}