package org.moskito.central.connectors;

/**
 * Abstract central connector config.
 */
public abstract class AbstractCentralConnectorConfig {

    private String[] supportedIntervals = new String[0];

    public String[] getSupportedIntervals() {
        return supportedIntervals;
    }

    public void setSupportedIntervals(String[] supportedIntervals) {
        this.supportedIntervals = supportedIntervals;
    }
}
