package com.afrunt.randomjoke.suppliers;

import com.afrunt.randomjoke.Joke;

/**
 * @author Andrii Frunt
 */
public class SecondChuckNorris extends AbstractJokeSupplier {
    @Override
    public Joke get() {
        return new Joke()
                .setText(jsonObjectFromUrl("https://api.icndb.com/jokes/random")
                        .optJSONObject("value")
                        .optString("joke")
                );
    }

    @Override
    public String getSource() {
        return "api.icndb.com";
    }
}
