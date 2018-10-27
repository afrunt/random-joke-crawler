package com.afrunt.randomjoke.suppliers;

import com.afrunt.randomjoke.Joke;
import org.json.JSONObject;

/**
 * @author Andrii Frunt
 */
public class Dkatz extends AbstractSupplier {
    @Override
    public Joke get() {
        JSONObject jsonObject = jsonObjectFromUrl("https://08ad1pao69.execute-api.us-east-1.amazonaws.com/dev/random_joke");

        return new Joke()
                .setText(jsonObject.optString("setup") + " " + jsonObject.optString("punchline"));
    }

    @Override
    public String getSource() {
        return "08ad1pao69.execute-api.us-east-1.amazonaws.com";
    }
}
