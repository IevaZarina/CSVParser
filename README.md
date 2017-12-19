# CSV file parser program

This is a simple CSV file parser program written in Scala.
It reads from file character by character and yields parsed rows. 
Because of this it can parse file with any size.

## Usage

Dowload the project to your machine. 
Use [sbt](http://www.scala-sbt.org/download.html) to install dependancies, run the tests and compile a jar.

```
sbt test
sbt package
```

Run the project .jar file from the command line. 
It takes in parameter with the path to your CSV file.

```
scala target/scala-2.12/csvparser_2.12-0.1.jar src/test/resources/input.csv
```

You should see output in your terminal.

Optionally you can set the
 ```quotingChar``` 
 ```seperator```
 ```delimiter``` characters. 
They default to:

* ```quotingChar```: "
* ```seperator```: \n
* ```delimiter```: ,
 

 ```
 scala target/scala-2.12/csvparser_2.12-0.1.jar src/test/resources/input.csv " \n ,
 ```
 