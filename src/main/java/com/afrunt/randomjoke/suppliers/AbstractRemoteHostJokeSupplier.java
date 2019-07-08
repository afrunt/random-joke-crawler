package com.afrunt.randomjoke.suppliers;

/**
 * @author Andrii Frunt
 */
public abstract class AbstractRemoteHostJokeSupplier extends AbstractJokeSupplier {
    private String host;

    public String getHost() {
        return host;
    }

    public AbstractRemoteHostJokeSupplier setHost(String host) {
        this.host = host;
        return this;
    }
}
