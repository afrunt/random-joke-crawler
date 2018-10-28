[![Build Status](https://travis-ci.org/afrunt/jach.svg?branch=master)](https://travis-ci.org/afrunt/random-joke-crawler)
## Java library for getting random jokes from different sources
Add random-joke-crawler to your project. for maven projects just add this dependency:
```xml
<dependency>
  <groupId>com.afrunt.randomjoke</groupId>
  <artifactId>random-joke-crawler</artifactId>
  <version>0.1.5</version>
</dependency>
```
It is extremely easy to initialize and start working with crawler:
```java
Jokes jokes = new Jokes()
                 .withDefaultSuppliers();

jokes.randomJoke().ifPresent(joke -> System.out.println(joke.getText()));
``` 

You can customize the sources of jokes:
```java
Jokes jokes = new Jokes()
                .withDefaultSuppliers()
                .with(BashOrg.class);
``` 

Or you can define your own set of sources:
```java
Jokes jokes = new Jokes()
                .with(BashOrg.class, ChuckNorris.class);
``` 

Create your own source by extending the AbstractJokeSupplier:
```java
public class MyJokes extends AbstractJokeSupplier {
    @Override
    public Joke get() {
        return new Joke().setText("The best joke ever");
    }

    @Override
    public String getSource() {
        return "my.joke";
    }
}
``` 
And add those source using:
```java
Jokes jokes = new Jokes()
                .with(MyJokes.class);
``` 
