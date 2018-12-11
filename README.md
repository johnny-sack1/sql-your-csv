# SQL your CSV

The goal was to write a program which will allow users to query their google sheet files using SQL. Google sheet tables should act as tables in the RDBMS.

Functionality:

 - selecting which columns should be displayed (SELECT * FROM xyz.csv; should also work).
 - filtering data (using WHERE, >, <, =, <>, LIKE, AND, OR)

## Code status
[![Build Status](https://travis-ci.org/johnny-sack1/sql-your-csv.svg?branch=master)](https://travis-ci.org/johnny-sack1/sql-your-csv)
## Getting Started
### Prerequisites

You need to have:
- maven
- java
- git

### Installing

A step by step series of examples that tell you have to get a development env running

Clone the repo:

```
git clone https://github.com/johnny-sack1/sql-your-csv.git
```

Build jar file

```
mvn package
```

Run jar file 
```
java -jar sqlyourcsv-0.0.1-SNAPSHOT.jar
```

Now you can access the application at [http://localhost:8080](http://localhost:8080)

## Built With

* [JAVA](https://java.com) - General-purpose computer-programming 
* [Maven](https://maven.apache.org/) - Dependency Management


## Authors

* **Mączyński Jonatan** - *Initial work* - [johnny-sack1](https://github.com/johnny-sack1)
* **Brzozowski Filip** - *Initial work* - [fbrzozowski](https://github.com/fbrzozowski)

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE) file for details
