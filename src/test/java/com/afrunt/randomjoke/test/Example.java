package com.afrunt.randomjoke.test;

import com.afrunt.randomjoke.Joke;
import com.afrunt.randomjoke.Jokes;
import com.afrunt.randomjoke.suppliers.BashOrg;

/**
 * @author Andrii Frunt
 */
public class Example {
    public static void main(String[] args) {
        Jokes jokes = new Jokes();

        jokes = jokes.withDefaultSuppliers()
                .without(BashOrg.class)
                .with(BashOrg.class);
        
        for (int i = 0; i < 1000; i++) {
            jokes
                    .randomJoke()
                    .ifPresent(Example::printJoke);
        }
    }

    private static void printJoke(Joke joke) {
        System.out.print(joke.getTimeout() + "ms");
        System.out.println(" -> " + joke.getSource());
        System.out.println(joke.getText());
        System.out.println();
    }
}
