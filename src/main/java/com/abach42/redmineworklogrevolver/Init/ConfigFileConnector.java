package com.abach42.redmineworklogrevolver.Init;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;

import com.abach42.redmineworklogrevolver.Exception.ConfigFileConnectorException;

/*
 * Connects to {@File} and reads+writes using configuration2 API.
 * First it initializes file, creates it, validates and sets up configuration builder.
 * You can read and write configuration to file specified in setup.
 */
public class ConfigFileConnector implements ConfigFileConnectable {
    protected File file; 

    protected FileBasedConfigurationBuilder<FileBasedConfiguration> builder;
    protected Configuration propertiesConfiguration; 
    
    protected static final String NO_FILE_PATH_MSG = "Functional issue. No file path set.";
    protected static final String FILE_NOT_WRITTEN_MSG = "Configuration file can not be written.";
    protected static final String FILE_NOT_READ_MSG = "Configuration file can not be opened.";
    protected static final String CONFIGURATION_NOT_SET = "Functional issue. Configuration not initialized.";

    @Override
    public void setup(String folder, String filenName) throws ConfigFileConnectorException {
        setFile(folder + filenName);

        if(!exists()) {
            create(folder);
        }

        canRead();

        setBuilder();
        setPropertiesConfiguration();
    }

    @Override
    public Map<String, String> readConfiguration() {
        checkSetup();

        Iterator<String> keys = propertiesConfiguration.getKeys();

        Map<String, String> map = new HashMap<>();

        while (keys.hasNext()) {
            String key = keys.next();
            String value = propertiesConfiguration.getString(key);
            map.put(key, value);
        }

        return map;
    }

    @Override
    public void writeConfiguration(Map<String, String> configurationMap) {
        checkSetup();
        canWrite();

        configurationMap.forEach(
            (key, value) -> propertiesConfiguration.setProperty(key, value)
        );

        save();
    }

    private void checkSetup() throws ConfigFileConnectorException {
        if(propertiesConfiguration == null) {
            throw new ConfigFileConnectorException(CONFIGURATION_NOT_SET);
        }
    }

    protected void setFile(String filePath) {
        if(filePath == null || filePath == "") {
            throw new IllegalArgumentException(NO_FILE_PATH_MSG);
        }

        file = new File(filePath);
    }
    
    protected boolean exists() {
        return file.exists();
    }

    protected void create(String folder) {
        File propertiesFolder = new File(folder);
        propertiesFolder.mkdirs();

        try {
            this.file.createNewFile();
        } catch (IOException e) {
            throw new ConfigFileConnectorException(e.getMessage());
        }
    }

    protected boolean canRead() {
        if(! file.canRead()) {
            throw new ConfigFileConnectorException(FILE_NOT_READ_MSG);
        }

        return true;
    }

    private void setBuilder() {
        Parameters params = new Parameters();
		this.builder = new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
                .configure(
                    params.fileBased().setFile(file)		
                );
    }

    protected void setPropertiesConfiguration() {
        try {
            this.propertiesConfiguration = builder.getConfiguration();
        } catch (ConfigurationException e) {
            throw new ConfigFileConnectorException(e.getMessage());
        }
    }

    protected boolean canWrite() {
        if(! file.canWrite()) {
            throw new ConfigFileConnectorException(FILE_NOT_WRITTEN_MSG);
        }

        return true;
    }

    protected void save() {
        try {
            builder.save();
        } catch (ConfigurationException e) {
            throw new ConfigFileConnectorException(e.getMessage());
        }
    }

}
