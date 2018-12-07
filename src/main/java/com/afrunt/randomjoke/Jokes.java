package com.afrunt.randomjoke;

import com.afrunt.randomjoke.suppliers.*;
import org.apache.commons.text.StringEscapeUtils;

import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author Andrii Frunt
 */
public class Jokes {

    private static final Logger LOGGER = Logger.getLogger(Jokes.class.getName());

    private List<AbstractJokeSupplier> jokeSuppliers = new ArrayList<>();

    private Map<AbstractJokeSupplier, Integer> errors = new HashMap<>();

    private Map<AbstractJokeSupplier, Date> disabledSuppliers = new HashMap<>();

    private long disablingTimeoutMillis = TimeUnit.MINUTES.toMillis(1);

    private int errorsToDisable = 10;

    public Optional<Joke> randomJoke() {
        if (jokeSuppliers.isEmpty()) {
            return Optional.empty();
        }
        AbstractJokeSupplier jokeSupplier = getRandomJokeSupplier();
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
            handleError(jokeSupplier, e);
            return Optional.empty();
        }
    }

    public List<Joke> randomJokes(int count) {
        return randomJokes(count, Runtime.getRuntime().availableProcessors());
    }

    public List<Joke> randomJokes(int count, int parallelism) {
        ExecutorService pool = Executors.newFixedThreadPool(parallelism);
        try {
            return randomJokeStream(pool)
                    .limit(count)
                    .collect(Collectors.toList());
        } finally {
            pool.shutdown();
        }
    }

    public Stream<Joke> randomJokeStream() {
        return randomJokeStream(ForkJoinPool.commonPool());
    }

    public Stream<Joke> randomJokeStream(ExecutorService executor) {
        try {
            return executor.submit(() -> IntStream
                    .range(0, Integer.MAX_VALUE)
                    .boxed()
                    .parallel()
                    .map(i -> CompletableFuture.supplyAsync(this::waitForJoke, executor))
                    .map(CompletableFuture::join)).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
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

    public Jokes withErrorsToDisable(int errorCount) {
        errorsToDisable = errorCount;
        return this;
    }

    public Jokes withDisablingTimeout(long millis) {
        disablingTimeoutMillis = millis;
        return this;
    }

    Joke waitForJoke() {
        Optional<Joke> joke = randomJoke();
        while (!joke.isPresent()) {
            joke = randomJoke();
        }
        return joke.get();
    }

    private void handleError(AbstractJokeSupplier jokeSupplier, Exception e) {
        LOGGER.log(Level.WARNING, "Error getting joke from " + jokeSupplier.getClass().getSimpleName() + " " + e.getLocalizedMessage());
        Integer errorCount = errors.getOrDefault(jokeSupplier, 0);
        errorCount = errorCount + 1;
        if (errorCount >= errorsToDisable) {
            HashMap<AbstractJokeSupplier, Date> localDisabledSuppliers = new HashMap<>(disabledSuppliers);

            localDisabledSuppliers.put(jokeSupplier, new Date(System.currentTimeMillis() + disablingTimeoutMillis));
            ArrayList<AbstractJokeSupplier> localSuppliers = new ArrayList<>(jokeSuppliers);
            localSuppliers.remove(jokeSupplier);
            HashMap<AbstractJokeSupplier, Integer> localErrors = new HashMap<>(errors);
            localErrors.remove(jokeSupplier);

            disabledSuppliers = localDisabledSuppliers;
            errors = localErrors;
            jokeSuppliers = localSuppliers;

            LOGGER.log(Level.WARNING, "Supplier " + jokeSupplier.getSource() + " temporary disabled");
        } else {
            errors.put(jokeSupplier, errorCount);
        }
    }

    private AbstractJokeSupplier getRandomJokeSupplier() {
        List<AbstractJokeSupplier> suppliersToEnable = disabledSuppliers.entrySet().stream()
                .filter(entry -> entry.getValue().before(new Date()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        if (!suppliersToEnable.isEmpty()) {
            ArrayList<AbstractJokeSupplier> suppliers = new ArrayList<>(jokeSuppliers);
            suppliers.addAll(suppliersToEnable);
            HashMap<AbstractJokeSupplier, Date> disabled = new HashMap<>(disabledSuppliers);

            suppliersToEnable
                    .stream()
                    .peek(s -> LOGGER.log(Level.WARNING, "Supplier " + s.getSource() + " enabled again"))
                    .forEach(disabled::remove);

            disabledSuppliers = disabled;
            jokeSuppliers = suppliers;
        }

        int supplierIndex = ThreadLocalRandom.current().nextInt(0, jokeSuppliers.size());
        return jokeSuppliers.get(supplierIndex);
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
                ICanHazDadJoke.class, BashOrg.class, GoodBadJokes.class
        );
    }

}
