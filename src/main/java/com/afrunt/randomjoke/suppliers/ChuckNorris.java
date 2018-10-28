package com.afrunt.randomjoke.suppliers;

import com.afrunt.randomjoke.Joke;

/**
 * @author Andrii Frunt
 */
public class ChuckNorris extends AbstractJokeSupplier {
    @Override
    public Joke get() {
        return new Joke().setText(jsonObjectFromUrl("https://api.chucknorris.io/jokes/random").optString("value"));
    }

    @Override
    public String getSource() {
        return "chucknorris.io";
    }
}
