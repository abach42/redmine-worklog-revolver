package com.abach42.redmineworklogrevolver.Init;

import java.util.Map;

import com.abach42.redmineworklogrevolver.Exception.ConfigFileConnectorException;

public interface ConfigFileConnectable {

    void setup(String folder, String filenName) throws ConfigFileConnectorException;

    Map<String, String> readConfiguration();

    void writeConfiguration(Map<String, String> configurationMap);

}