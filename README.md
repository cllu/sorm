# SORM2

This is my fork of the excellent ORM framework [SORM](http://sorm-framework.org).
SORM is an object-relational mapping framework designed to eliminate boilerplate and maximize productivity. 
It is absolutely abstracted from relational side, automatically creating database tables,
  emitting queries, inserting, updating and deleting records. 
This all functionality is presented to the user with a simple API around standard Scala's case classes.

This fork is strongly opinionated, very unstable, and everything may change overnight.
If you still want to use it, add the following lines to your sbt settings:
    
    resolvers += "Chunliang's Maven Repository" at "https://repo.chunlianglyu.com"
    libraryDependencies += "com.chunlianglyu.sorm2" %% "sorm2" % "0.4.0"
    
## Differences with SORM

Main:

- Add support for JValue from json4s, which will be converted to String internally
- Use Java 8 time instead of joda-time
- remove dependencies on joda-convert and guava
- use sbt instead of maven
- use slf4j instead of scala-logging
- `lazy val` and `implicit val` will be skipped 
- You can define things like `case class User(name: String) extends Persistable`
- TODO: remove dependencies on sext and embrace

Breaks:

- Tuple does not work

Caveats:

- Do not rely on the `copy` method provided by case classes, by default they do not copy the id value.

## Supported databases

* MySQL
* PostgreSQL
* H2
* HSQLDB

---

[![Build Status](https://travis-ci.org/cllu/sorm.svg?branch=master)](https://travis-ci.org/cllu/sorm)
