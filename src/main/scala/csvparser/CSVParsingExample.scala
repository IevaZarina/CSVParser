package csvparser

object CSVParsingExample {

  def main(args: Array[String]): Unit = {
    if (args.length == 0) {
      println("Missing CSV file path parameter!")
      return
    }
    val filename = args(0)

    val parser = new CSVParserIterator(io.Source.fromFile(filename).iter)
    for (row <- parser) println(row.map(value => value.getOrElse("None")).mkString(", "))
  }

}


