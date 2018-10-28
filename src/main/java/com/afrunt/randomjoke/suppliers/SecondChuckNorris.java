package com.afrunt.randomjoke.suppliers;

import com.afrunt.randomjoke.Joke;
import org.json.JSONObject;

/**
 * @author Andrii Frunt
 */
public class SecondChuckNorris extends AbstractJokeSupplier {
    @Override
    public Joke get() {
        JSONObject jsonObject = jsonObjectFromUrl("https://api.icndb.com/jokes/random");

        return new Joke()
                .setText(jsonObject
                        .optJSONObject("value")
                        .optString("joke")
                );
    }

    @Override
    public String getSource() {
        return "api.icndb.com";
    }
}
