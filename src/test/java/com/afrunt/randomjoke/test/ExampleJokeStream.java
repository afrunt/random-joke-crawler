package com.afrunt.randomjoke.test;

import com.afrunt.randomjoke.Jokes;
import com.afrunt.randomjoke.suppliers.ICanHazDadJoke;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

/**
 * @author Andrii Frunt
 */
public class ExampleJokeStream {
    public static void main(String[] args) {
        long started = System.currentTimeMillis();
        ForkJoinPool executor = new ForkJoinPool(10);
        new Jokes()
                .withDefaultSuppliers()
                .without(ICanHazDadJoke.class)
                .randomJokeStream(executor)
                .limit(100)
                .forEach(j -> System.out.println(j.getSource() + "\n" + j.getText() + "\n"));

        executor.shutdownNow();
        System.out.println("Elapsed " + TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - started) + "s");
    }
}
