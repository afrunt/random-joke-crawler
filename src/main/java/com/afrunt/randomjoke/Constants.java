package com.afrunt.randomjoke;

import com.afrunt.randomjoke.suppliers.*;

import java.util.List;

/**
 * @author Andrii Frunt
 */
public class Constants {
    public static final List<Class<? extends AbstractSupplier>> DEFAULT_JOKE_SUPPLIERS = List.of(
            ChuckNorris.class, GeekJoke.class, SecondChuckNorris.class,
            ICanHazDadJoke.class, Dkatz.class, BashOrg.class, GoodBadJokes.class
    );

    private Constants() {
    }

}
