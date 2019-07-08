package com.afrunt.randomjoke.suppliers;

import com.afrunt.randomjoke.Joke;

/**
 * @author Andrii Frunt
 */
public class GeekJoke extends AbstractRemoteHostJokeSupplier {
    public GeekJoke() {
        setHost("geek-jokes.sameerkumar.website");
    }

    @Override
    public Joke get() {
        return new Joke()
                .setText(removeSurroundingQuotes(stringFromUrl("https://" + getHost() + "/api")));
    }

    @Override
    public String getSource() {
        return "geek-jokes.sameerkumar.website";
    }
}
