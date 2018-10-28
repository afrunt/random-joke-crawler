package com.afrunt.randomjoke.suppliers;

import com.afrunt.randomjoke.Joke;

/**
 * @author Andrii Frunt
 */
public class GeekJoke extends AbstractJokeSupplier {
    @Override
    public Joke get() {
        return new Joke()
                .setText(removeSurroundingQuotes(stringFromUrl("https://geek-jokes.sameerkumar.website/api")));
    }

    @Override
    public String getSource() {
        return "geek-jokes.sameerkumar.website";
    }
}
