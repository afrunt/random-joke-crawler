package com.afrunt.randomjoke;

/**
 * @author Andrii Frunt
 */
public class Joke {
    private String source;
    private String text;
    private long timeout;

    public String getText() {
        return text;
    }

    public Joke setText(String text) {
        this.text = text;
        return this;
    }

    public String getSource() {
        return source;
    }

    public Joke setSource(String source) {
        this.source = source;
        return this;
    }

    public long getTimeout() {
        return timeout;
    }

    public Joke setTimeout(long timeout) {
        this.timeout = timeout;
        return this;
    }
}
