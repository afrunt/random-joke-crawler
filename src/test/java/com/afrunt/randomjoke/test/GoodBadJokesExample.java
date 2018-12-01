package com.afrunt.randomjoke.test;

import com.afrunt.randomjoke.suppliers.GoodBadJokes;

/**
 * @author Andrii Frunt
 */
public class GoodBadJokesExample {
    public static void main(String[] args) {
        GoodBadJokes goodBadJokes = new GoodBadJokes();
        for (int i = 0; i < 10; i++) {
            System.out.println(goodBadJokes.get().getText());
        }
    }
}
