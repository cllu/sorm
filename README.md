# SORM

SORM is an object-relational mapping framework designed to eliminate boilerplate and maximize productivity. 
It is absolutely abstracted from relational side, automatically creating database tables,
  emitting queries, inserting, updating and deleting records. 
This all functionality is presented to the user with a simple API around standard Scala's case classes. 

For more info, tutorials and documentation please visit the [official site](http://sorm-framework.org).

This fork has the following changes:

- Add support for JValue from json4s, which will be converted to String internally
- Use Java 8 time instead of joda-time

To install

    libraryDependencies += "org.sorm-framework" % "sorm" % "0.3.18"

## Supported databases

* MySQL
* PostgreSQL
* H2
* HSQLDB


---

[![Build Status](https://travis-ci.org/sorm/sorm.png?branch=master)](https://travis-ci.org/sorm/sorm)
