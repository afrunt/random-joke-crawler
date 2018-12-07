package com.afrunt.randomjoke.test;

import com.afrunt.randomjoke.Jokes;

import java.util.concurrent.TimeUnit;

/**
 * @author Andrii Frunt
 */
public class ExampleMultipleJokes {
    public static void main(String[] args) {
        long started = System.currentTimeMillis();
        new Jokes()
                .withDefaultSuppliers()
                .randomJokes(100, 10)
                .forEach(j -> System.out.println(j.getSource() + "\n" + j.getText() + "\n"));

        System.out.println("Elapsed " + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - started) + "s");
    }
}
