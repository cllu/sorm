# SORM

SORM is an object-relational mapping framework designed to eliminate boilerplate and maximize productivity. 
It is absolutely abstracted from relational side, automatically creating database tables,
  emitting queries, inserting, updating and deleting records. 
This all functionality is presented to the user with a simple API around standard Scala's case classes. 

For more info, tutorials and documentation please visit the [official site](http://sorm-framework.org).

This fork has the following changes:

- Add support for JValue from json4s, which will be converted to String internally
- Use Java 8 time instead of joda-time
- remove dependencies on joda-convert and guava
- use sbt instead of maven
- use slf4j instead of scala-logging
- `lazy val` and `implicit val` will be skipped 
- TODO: remove dependencies on sext and embrace


The fork is not published, so you need to compile and install locally (to local ivy2 cache):

    $ # mvn install -DskipTests=true -Dgpg.skip=true 
    $ sbt publish-local
    
Then use a new groupId
    
    libraryDependencies += "com.chunlianglyu.sorm" %% "sorm" % "0.3.18-SNAPSHOT"

## Supported databases

* MySQL
* PostgreSQL
* H2
* HSQLDB

---

[![Build Status](https://travis-ci.org/cllu/sorm.svg?branch=master)](https://travis-ci.org/cllu/sorm)
