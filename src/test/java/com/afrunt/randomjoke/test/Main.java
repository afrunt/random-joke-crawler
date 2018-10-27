package com.afrunt.randomjoke.test;

import com.afrunt.randomjoke.Joke;
import com.afrunt.randomjoke.JokeCrawler;

/**
 * @author Andrii Frunt
 */
public class Main {
    public static void main(String[] args) {
        JokeCrawler jokeCrawler = new JokeCrawler().withDefaultSuppliers();

        for (int i = 0; i < 100; i++) {
            jokeCrawler.randomJoke().ifPresent(Main::printJoke);
        }

    }

    private static void printJoke(Joke joke) {
        System.out.print(joke.getTimeout() + "ms");
        System.out.println(" -> " + joke.getSource());
        System.out.println(joke.getText());
        System.out.println();
    }
}
