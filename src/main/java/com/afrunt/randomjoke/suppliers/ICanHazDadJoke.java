package com.afrunt.randomjoke.suppliers;

import com.afrunt.randomjoke.Joke;
import org.json.JSONObject;

import java.util.Map;

/**
 * @author Andrii Frunt
 */
public class ICanHazDadJoke extends AbstractSupplier {
    @Override
    public Joke get() {
        JSONObject jsonObject = jsonObjectFromUrl("https://icanhazdadjoke.com/", Map.of("Accept", "application/json"));

        return new Joke()
                .setText(jsonObject.optString("joke"));
    }

    @Override
    public String getSource() {
        return "icanhazdadjoke.com";
    }
}
