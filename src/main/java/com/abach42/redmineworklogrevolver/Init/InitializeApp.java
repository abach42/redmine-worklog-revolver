package com.abach42.redmineworklogrevolver.Init;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.abach42.redmineworklogrevolver.Context.AppConfig;
import com.abach42.redmineworklogrevolver.Context.ContextInterface;
import com.abach42.redmineworklogrevolver.Display.TerminalInputable;
import com.abach42.redmineworklogrevolver.Display.UserOutput;
import com.abach42.redmineworklogrevolver.Exception.ConfigFileConnectorException;
import com.abach42.redmineworklogrevolver.Exception.InitializeAppException;
import com.abach42.redmineworklogrevolver.Exception.InitializeValidationException;
import com.abach42.redmineworklogrevolver.Exception.WrongAccessKeyException;

/*
 * Initializes configuration file, 
 * in case it is not set, it takes up all default values from {@DefaultKeys} numeration to create a new one -
 * by using {@ConfigFileConnectorInterface}. 
 * If something is unset in configuration file, it starts compeletely new, asking user for Api Access Key 
 * using {@ConsoleInputInterface}. 
 */
public class InitializeApp {
    
    protected static final String CONFIG_MISSING_MSG = "Configuration is corrupt. Please delete config file.";
    protected static final String WRONG_ACCESS_KEY_MSG = "API access key missing. Please enter:";

    private static final String CONFIG_FOLDER = ".configuration/";
    private static final String CONFIG_FILE_PATH = "config.properties";
    
    protected ContextInterface context;
    protected TerminalInputable input; 
    protected ConfigFileConnectable connection;

    public InitializeApp(TerminalInputable input, ConfigFileConnectable connection) {
            this.input = input;
            this.connection = connection; 
    }

    //fluent interface to make shortcut possible, but it still is train wreck
    public InitializeApp withContext(ContextInterface context) {
        this.context = context;

        return this;
    }

    /**
    * Default keys to keep it up with generated configuration file. 
    * Every key must be set in file.
    */
    public static enum DefaultKeys {
        DEFAULT_URI("config.default_uri", "https://frs.plan.io"),
        DEFAULT_LIMIT("config.default_limit", "100"),
        DEFAULT_DATE_FORMAT("config.date_format", "dd.MM.yyyy"),
        
        //will be set by user console input
        API_ACCESS_KEY("config.api_access_key", "");
        
        private String configKey;
        private String defaultValue;
        
        DefaultKeys(String configKey, String defaultValue) {
            this.configKey = configKey;
            this.defaultValue = defaultValue;
        }
        
        public String getConfigKey() {
            return configKey;
        }

        public String getDefaultValue() {
            return defaultValue;
        }
    }

    public void initialize() {
        Map<String, String> configurationMap = new HashMap<>();

        try {
            configurationMap = readConfiguration();

            validateConfiguration(configurationMap);

        } catch (ConfigFileConnectorException e) {
            throw new InitializeAppException(e.getMessage());

        } catch (InitializeValidationException e) {
            configurationMap = getDefaultValues();

        } catch (WrongAccessKeyException e) {
            String apiAccessKey = askUserForKey(e.getMessage());
            putAccesKeyToConfiguration(configurationMap, apiAccessKey);

        }

        writeConfiguration(configurationMap);
        writeConfigurationToObject(configurationMap);
    }

    public void reInitializeAccessKey() {

        Map<String, String> configurationMap = new HashMap<>();

        try {
            configurationMap = readConfiguration();

        } catch (ConfigFileConnectorException e) {
            throw new InitializeAppException(e.getMessage());

        } 
        
        //invalidate false key
        putAccesKeyToConfiguration(configurationMap, "");
        //write this to file, to be able to restart configuration
        writeConfiguration(configurationMap);

        initialize();
    }

    private Map<String, String> readConfiguration() {
        Map<String, String> configurationMap;

        connection.setup(CONFIG_FOLDER, CONFIG_FILE_PATH);
        configurationMap = connection.readConfiguration();

        return configurationMap;
    }    
    
    private void writeConfiguration(Map<String, String> configurationMap) {
        connection.writeConfiguration(configurationMap);
    }

    protected boolean validateConfiguration(Map<String, String> configurationMap)  {
        if(
            isStringNotSet(configurationMap.get(DefaultKeys.DEFAULT_URI.getConfigKey()))
            ||
            isStringNotSet(configurationMap.get(DefaultKeys.DEFAULT_LIMIT.getConfigKey()))
            ||
            isStringNotSet(configurationMap.get(DefaultKeys.DEFAULT_DATE_FORMAT.getConfigKey()))
        ) {
            throw new InitializeValidationException(CONFIG_MISSING_MSG);
        }

        if(isStringNotSet(configurationMap.get(DefaultKeys.API_ACCESS_KEY.getConfigKey()))) {
            throw new WrongAccessKeyException(WRONG_ACCESS_KEY_MSG);
        }

        return true;
    }

    protected  Map<String, String> getDefaultValues() {
        Map<String, String> configurationMap = new HashMap<>();

        configurationMap = Stream.of(DefaultKeys.values())
              .collect(
                    Collectors.toMap(
                        DefaultKeys::getConfigKey, 
                        DefaultKeys::getDefaultValue
                    ));

        putAccesKeyToConfiguration(configurationMap, askUserForKey(WRONG_ACCESS_KEY_MSG));

        return configurationMap;
    }

    protected String askUserForKey(String question) {
        new UserOutput().write(question);
    	return input.getStringFromUser(); 
    }
    
    protected void putAccesKeyToConfiguration(Map<String, String> configurationMap, String apiAccessKey) {
        configurationMap.put(DefaultKeys.API_ACCESS_KEY.getConfigKey(), sanitizeBrackets(apiAccessKey));
    }

    protected void writeConfigurationToObject(Map<String, String> configurationMap) {
        AppConfig appConfig = context.getAppConfig();
        appConfig.setBaseUri(configurationMap.get(DefaultKeys.DEFAULT_URI.getConfigKey()));
        appConfig.setDefaultLimitFromString(configurationMap.get(DefaultKeys.DEFAULT_LIMIT.getConfigKey()));
        appConfig.setDatePattern(configurationMap.get(DefaultKeys.DEFAULT_DATE_FORMAT.getConfigKey()));
        appConfig.setApiKey(configurationMap.get(DefaultKeys.API_ACCESS_KEY.getConfigKey()));
        
        context.setAppConfig(appConfig);
    }

    private boolean isStringNotSet(String string) {
        return string == null || string.isBlank();
    }

    /*
    * Very simple to avoid evil inputs, key will be POST
    */
    private String sanitizeBrackets(String input) {
        Pattern pattern = Pattern.compile("[\\[\\]{}()]");
        
        Matcher matcher = pattern.matcher(input != null ? input : new String(""));
        return matcher.replaceAll("");
    }
}
