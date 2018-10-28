package com.afrunt.randomjoke;

import com.afrunt.randomjoke.suppliers.*;
import org.apache.commons.text.StringEscapeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author Andrii Frunt
 */
public class Jokes {
    private List<AbstractJokeSupplier> jokeSuppliers = new ArrayList<>();

    public Optional<Joke> randomJoke() {
        if (jokeSuppliers.isEmpty()) {
            return Optional.empty();
        }
        int supplierIndex = ThreadLocalRandom.current().nextInt(0, jokeSuppliers.size());
        AbstractJokeSupplier jokeSupplier = jokeSuppliers.get(supplierIndex);
        try {
            long start = System.currentTimeMillis();
            Joke joke = jokeSupplier.get();
            if (joke != null) {
                return Optional.of(joke
                        .setText(StringEscapeUtils.unescapeHtml4(joke.getText()))
                        .setTimeout(System.currentTimeMillis() - start)
                        .setSource(jokeSupplier.getSource())
                );
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            Logger.getLogger(Jokes.class.getName()).log(Level.WARNING, "Error getting joke from " + jokeSupplier.getClass().getSimpleName() + " " + e.getLocalizedMessage());
            return Optional.empty();
        }
    }

    public Jokes withDefaultSuppliers() {
        List<AbstractJokeSupplier> suppliers = new ArrayList<>();

        for (Class<? extends AbstractJokeSupplier> spc : getDefaultSuppliers()) {
            suppliers.add(initSupplier(spc));
        }
        return setJokeSuppliers(suppliers);
    }

    public Jokes without(Class<? extends AbstractJokeSupplier> supplierType) {
        return setJokeSuppliers(
                getJokeSuppliers().stream()
                        .filter(s -> !supplierType.isAssignableFrom(s.getClass()))
                        .collect(Collectors.toList())
        );
    }

    public Jokes with(Class<? extends AbstractJokeSupplier>... supplierTypes) {
        for (Class<? extends AbstractJokeSupplier> supplierType : supplierTypes) {
            addSupplier(initSupplier(supplierType));
        }

        return this;
    }

    public List<AbstractJokeSupplier> getJokeSuppliers() {
        return jokeSuppliers;
    }

    public Jokes setJokeSuppliers(List<AbstractJokeSupplier> jokeSuppliers) {
        this.jokeSuppliers = jokeSuppliers;
        return this;
    }

    public Jokes addSupplier(AbstractJokeSupplier supplier) {
        if (!alreadyHasSupplier(supplier.getClass())) {
            List<AbstractJokeSupplier> suppliers = new ArrayList<>(getJokeSuppliers());
            suppliers.add(supplier);
            return setJokeSuppliers(suppliers);
        } else {
            return this;
        }
    }

    private boolean alreadyHasSupplier(Class<? extends AbstractJokeSupplier> supplierType) {
        return getJokeSuppliers().stream().anyMatch(s -> supplierType.isAssignableFrom(s.getClass()));
    }

    private AbstractJokeSupplier initSupplier(Class<? extends AbstractJokeSupplier> supplierType) {
        try {
            return supplierType.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<Class<? extends AbstractJokeSupplier>> getDefaultSuppliers() {
        return List.of(
                ChuckNorris.class, GeekJoke.class, SecondChuckNorris.class,
                ICanHazDadJoke.class, Dkatz.class, BashOrg.class, GoodBadJokes.class
        );
    }
}
