package csvparser

object CSVParsingExample {

  def main(args: Array[String]): Unit = {
    if (args.length == 0) {
      println("Missing CSV file path parameter!")
      return
    }

    if (args.length > 1 && args.length < 4) {
      println("Please provide all parsing characters: [quotingChar] [separator] [delimiter]. The dafault values are: \" \\n ,")
      return
    }

    val filename = args(0)
    val parser = args.length match {
      case 1 => new CSVParserIterator(io.Source.fromFile(filename).buffered)
      case 4 => new CSVParserIterator(io.Source.fromFile(filename).buffered,
        quotingChar = args(1)(0),
        separator = args(2)(0),
        delimiter = args(3)(0))
    }

    for (row <- parser) println(row.map(value => value.getOrElse("None")).mkString(", "))
  }

}


