package com.abach42.redmineworklogrevolver.Display;

import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public interface Logable {
    public static final String LOGGING_CONF_PATH = "/.configuration/logging.properties";

    public default Logger getLogger(Object issue) {
        try (InputStream configStream = issue.getClass().getResourceAsStream(LOGGING_CONF_PATH)){
            LogManager.getLogManager().readConfiguration(configStream);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Logger.getLogger(issue.getClass().getName());
    }

    public default void logServere(Object issue, String message) {
        getLogger(issue).logp(Level.SEVERE, issue.getClass().getName(), "", message);
    }
}
