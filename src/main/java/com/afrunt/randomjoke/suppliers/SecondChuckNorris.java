package com.afrunt.randomjoke.suppliers;

import com.afrunt.randomjoke.Joke;

/**
 * @author Andrii Frunt
 */
public class SecondChuckNorris extends AbstractRemoteHostJokeSupplier {
    public SecondChuckNorris() {
        setHost("api.icndb.com");
    }

    @Override
    public Joke get() {
        return new Joke()
                .setText(jsonObjectFromUrl("https://" + getHost() + "/jokes/random")
                        .optJSONObject("value")
                        .optString("joke")
                );
    }

    @Override
    public String getSource() {
        return "api.icndb.com";
    }
}
