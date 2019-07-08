package com.afrunt.randomjoke.suppliers;

import com.afrunt.randomjoke.Joke;

/**
 * @author Andrii Frunt
 */
public class ChuckNorris extends AbstractRemoteHostJokeSupplier {
    public ChuckNorris() {
        setHost("api.chucknorris.io");
    }

    @Override
    public Joke get() {
        return new Joke().setText(jsonObjectFromUrl("https://" + getHost() + "/jokes/random").optString("value"));
    }

    @Override
    public String getSource() {
        return "chucknorris.io";
    }
}
