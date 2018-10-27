package com.afrunt.randomjoke.suppliers;

import com.afrunt.randomjoke.Joke;
import org.json.JSONObject;

/**
 * @author Andrii Frunt
 */
public class ChuckNorris extends AbstractSupplier {
    @Override
    public Joke get() {
        JSONObject jsonObject = jsonObjectFromUrl("https://api.chucknorris.io/jokes/random");
        return new Joke().setText(jsonObject.optString("value"));
    }

    @Override
    public String getSource() {
        return "chucknorris.io";
    }
}
