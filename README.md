[![Build Status](https://travis-ci.org/afrunt/jach.svg?branch=master)](https://travis-ci.org/afrunt/random-joke-crawler)
## Java library for getting random jokes from different sources
Add random-joke-crawler to your project. for maven projects just add this dependency:
```xml
<dependency>
  <groupId>com.afrunt.randomjoke</groupId>
  <artifactId>random-joke-crawler</artifactId>
  <version>0.1</version>
</dependency>
```
It is extremely easy to initialize and start working with crawler:
```java
JokeCrawler jokeCrawler = new JokeCrawler()
                            .withDefaultSuppliers();
jokeCrawler.randomJoke().ifPresent(joke -> System.out.println(joke.getText()));
``` 

You can customize the sources of jokes:
```java
JokeCrawler jokeCrawler = new JokeCrawler()
                .withDefaultSuppliers()
                .with(BashOrg.class);
``` 

Or you can define your own set of sources:
```java
JokeCrawler jokeCrawler = new JokeCrawler()
                .with(BashOrg.class, ChuckNorris.class);
``` 