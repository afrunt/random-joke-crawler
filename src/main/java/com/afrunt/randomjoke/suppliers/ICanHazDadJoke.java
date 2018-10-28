package com.afrunt.randomjoke.suppliers;

import com.afrunt.randomjoke.Joke;

import java.util.Map;

/**
 * @author Andrii Frunt
 */
public class ICanHazDadJoke extends AbstractJokeSupplier {
    @Override
    public Joke get() {
        return new Joke()
                .setText(
                        jsonObjectFromUrl("https://icanhazdadjoke.com/", Map.of("Accept", "application/json"))
                                .optString("joke")
                );
    }

    @Override
    public String getSource() {
        return "icanhazdadjoke.com";
    }
}
