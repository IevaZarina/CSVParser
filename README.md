# CSV file parser program

This is a simple CSV file parser program written in Scala.
It reads from file character by character and returns an iterator of rows.
It can parse large files (> 1 TB) as long as each line/row fits into memory.

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

 ## Benchmark

Generate a large file

```
cat src/test/resources/input.csv > large.csv
for i in `seq 1 13`; do
    cp large.csv large.csv.tmp
    cat large.csv.tmp large.csv.tmp >> large.csv;
done
```

Generated file is 157 MB with 12.75 million rows

```
$ ls -lh large.csv
-rw-r--r--  1 ievazarina  staff   157M Dec 19 09:35 large.csv
$ wc -l large.csv
 12754584 large.csv
```

Benchmark on MacBook Pro 2017:
```
time scala target/scala-2.12/csvparser_2.12-0.1.jar large.csv > /dev/null
24.63s user 6.80s system 104% cpu 30.041 total
```

`157 MB / 30s = 5.2 MB/s`
