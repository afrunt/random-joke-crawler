package com.afrunt.randomjoke.suppliers;

import com.afrunt.randomjoke.HttpOperationsSupport;
import com.afrunt.randomjoke.Joke;
import org.htmlcleaner.HtmlCleaner;

import java.util.function.Supplier;

/**
 * @author Andrii Frunt
 */
public abstract class AbstractJokeSupplier implements Supplier<Joke>, HttpOperationsSupport {
    private HtmlCleaner cleaner = new HtmlCleaner();

    private boolean useProxy = false;

    public abstract String getSource();


    @Override
    public HtmlCleaner getCleaner() {
        return cleaner;
    }

    protected String removeSurroundingQuotes(String text) {
        if (text.startsWith("\"") && text.endsWith("\"")) {
            text = text.substring(1);
            text = text.substring(0, text.length() - 1);
        }
        return text;
    }
}
