package com.afrunt.randomjoke;

import com.afrunt.randomjoke.suppliers.AbstractSupplier;
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
public class JokeCrawler {
    private List<AbstractSupplier> jokeSuppliers = new ArrayList<>();

    public Optional<Joke> randomJoke() {
        if (jokeSuppliers.isEmpty()) {
            return Optional.empty();
        }
        int supplierIndex = ThreadLocalRandom.current().nextInt(0, jokeSuppliers.size());
        AbstractSupplier jokeSupplier = jokeSuppliers.get(supplierIndex);
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
            Logger.getLogger(JokeCrawler.class.getName()).log(Level.WARNING, "Error getting joke from " + jokeSupplier.getClass().getSimpleName() + " " + e.getLocalizedMessage());
            return Optional.empty();
        }
    }

    public JokeCrawler withDefaultSuppliers() {
        List<AbstractSupplier> suppliers = new ArrayList<>();
        for (Class<? extends AbstractSupplier> spc : Constants.DEFAULT_JOKE_SUPPLIERS) {
            suppliers.add(initSupplier(spc));
        }
        return setJokeSuppliers(suppliers);
    }

    public JokeCrawler without(Class<? extends AbstractSupplier> supplierType) {
        return setJokeSuppliers(
                getJokeSuppliers().stream()
                        .filter(s -> supplierType.isAssignableFrom(s.getClass()))
                        .collect(Collectors.toList())
        );
    }

    public JokeCrawler with(Class<? extends AbstractSupplier>... supplierTypes) {
        for (Class<? extends AbstractSupplier> supplierType : supplierTypes) {
            if (!alreadyHasSupplier(supplierType)) {
                addSupplier(initSupplier(supplierType));
            }
        }

        return this;
    }

    public List<AbstractSupplier> getJokeSuppliers() {
        return jokeSuppliers;
    }

    public JokeCrawler setJokeSuppliers(List<AbstractSupplier> jokeSuppliers) {
        this.jokeSuppliers = jokeSuppliers;
        return this;
    }

    public JokeCrawler addSupplier(AbstractSupplier supplier) {
        if (!alreadyHasSupplier(supplier.getClass())) {
            ArrayList<AbstractSupplier> suppliers = new ArrayList<>(getJokeSuppliers());
            suppliers.add(supplier);
            return setJokeSuppliers(suppliers);
        } else {
            return this;
        }
    }

    private boolean alreadyHasSupplier(Class<? extends AbstractSupplier> supplierType) {
        return getJokeSuppliers().stream().anyMatch(s -> supplierType.isAssignableFrom(s.getClass()));
    }

    private AbstractSupplier initSupplier(Class<? extends AbstractSupplier> supplierType) {
        try {
            return supplierType.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
