package com.abach42.redmineworklogrevolver.Exception;

import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public interface Logable {
    public static final String LOGGING_CONF_PATH = "/.configuration/logging.properties";

    public default Logger getLogger(Exception exception) {
        try (InputStream configStream = exception.getClass().getResourceAsStream(LOGGING_CONF_PATH)){
            LogManager.getLogManager().readConfiguration(configStream);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Logger.getLogger(exception.getClass().getName());
    }

    public default void logServere(Exception exception) {
        getLogger(exception).logp(Level.SEVERE, exception.getClass().getName(), "", exception.getMessage());
    }
}
