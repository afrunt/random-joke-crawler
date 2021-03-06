package com.afrunt.randomjoke.suppliers;

import com.afrunt.randomjoke.Joke;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Andrii Frunt
 */
public class ICanHazDadJoke extends AbstractRemoteHostJokeSupplier {
    public ICanHazDadJoke() {
        setHost("icanhazdadjoke.com");
    }

    @Override
    public Joke get() {
        Map<String, String> headers = Map.of(
                "Accept", "application/json",
                "User-Agent", String.valueOf(ThreadLocalRandom.current().nextInt())
        );
        return new Joke()
                .setText(
                        jsonObjectFromUrl("https://" + getHost() + "/", headers)
                                .optString("joke")
                );
    }

    @Override
    public String getSource() {
        return "icanhazdadjoke.com";
    }
}
