package com.abach42.redmineworklogrevolver.Init;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import com.abach42.redmineworklogrevolver.Exception.ConfigFileConnectorException;

/*
 * This Test does not classically follow unit rules, but include partly functional aspects 
 * to be able to run over file behavior.
 */
public class ConfigFileConnectorFunctionalTest {
    public ConfigFileConnector subject = new ConfigFileConnector();
    public ConfigFileConnector subjectFalse = new ConfigFileConnector();
    
    public String existingPath = "src/test/java/com/abach42/redmineworklogrevolver/Init/Fixtures/";
    public String exisitingFile = "a.file";

    public String noPath = "/x/foo/";
    public String noFile = "/bar.baz";

    public class DummyBuilder extends FileBasedConfigurationBuilder<FileBasedConfiguration> {
        public DummyBuilder(Class<? extends FileBasedConfiguration> resultClass) {
            super(resultClass);
        }
        
        @Override
        public FileBasedConfiguration getConfiguration() throws ConfigurationException {
            throwException();

            return null;
        }

        @Override
        public void save() throws ConfigurationException {
            throwException();
        }

        private void throwException() throws ConfigurationException {
            throw new ConfigurationException("foo");
        }
    }

    @BeforeEach
    public void setUp() {
        subject.setup(existingPath, exisitingFile);
        subjectFalse.setFile(noPath + noFile);
    }

    @Test
    @DisplayName("Setup sets propertiesConfiguration to not null")
    public void testSetup() {
        subject.setup(existingPath, exisitingFile);
        assertThat(subject.propertiesConfiguration).isNotNull();
    }
    
    @ParameterizedTest
    @DisplayName("No path: should throw Exception")
    @NullAndEmptySource
    public void testSetFileNull(String candidate) {
        Exception exception = assertThrows(IllegalArgumentException.class, () 
            -> subjectFalse.setFile(candidate));

        assertEquals(ConfigFileConnector.NO_FILE_PATH_MSG, exception.getMessage());
	}

    @Test
    @DisplayName("Should set {@File}")
    public void testSetFileReturnPath() {
        String expected = "foobar";
        subject.setFile(expected);
        String actual = subject.file.getPath();

        assertThat(actual).isEqualTo(expected);
	}

    @Test
    @DisplayName("No file: return false")
    public void testDoesFileExistThrowsException() {
        boolean actual = subjectFalse.exists();

        assertThat(actual).isFalse();
    }

    @Test
    @DisplayName("File exists: return true")
    public void testDoesFileExistReturnsTrue() {
        boolean actual = subject.exists();

        assertThat(actual).isTrue();
    }

    @Test
    @DisplayName("Creates file, not exists before.")
    public void createFile() {
        ConfigFileConnector dummySubject = new ConfigFileConnector();
        dummySubject.setFile(existingPath + "foobar");
        assertThat(dummySubject.exists()).isFalse();
        dummySubject.create(existingPath);
        assertThat(dummySubject.exists()).isTrue();

        dummySubject.file.delete();
    }

    @Test
    @DisplayName("File not opened: throw Exception to end Application")
    public void testCanReadFileThrowsException() {
        Exception exception = assertThrows(ConfigFileConnectorException.class, () ->
            subjectFalse.canRead());
        assertEquals(ConfigFileConnector.FILE_NOT_READ_MSG, exception.getMessage());
    }

    @Test
    @DisplayName("File can be read: return true")
    public void testCanReadFileReturnsTrue() {
        boolean actual = subject.canRead();

        assertThat(actual).isTrue();
    }


    @Test
    @DisplayName("No file: throw Exception")
    public void testSetPropertiesConfigurationThrowsException() {
        ConfigFileConnector dummySubject = new ConfigFileConnector();

        dummySubject.builder = new DummyBuilder(PropertiesConfiguration.class);

        assertThrows(ConfigFileConnectorException.class, () ->
        dummySubject.setPropertiesConfiguration());
    }

    @Test
    @DisplayName("No file: throw Exception")
    public void testSetPropertiesConfiguration() {
        subject.setPropertiesConfiguration(); 
        Configuration actual = subject.propertiesConfiguration; 

        assertThat(actual).isNotNull();
    }

    @Test
    @DisplayName("Should throw if no config")
    public void testReadConfigurationNoConfigThrowsExecption() {
        ConfigFileConnector dummySubject = new ConfigFileConnector();

        assertThrows(ConfigFileConnectorException.class, () -> 
            dummySubject.readConfiguration());
    }

    @Test
    @DisplayName("Returns map of key - value")
    public void testReadConfigurationReturnsMap() {
        Map<String, String> actualMap = subject.readConfiguration();

        assertThat(actualMap).contains(entry("foo.bar", "baz"), entry("foo.baz", "boo"));
    }

    @Test
    @DisplayName("Should throw if no config")
    public void testCanWriteNoConcifgurationFileThrowsException() {
        subjectFalse.propertiesConfiguration = null;

        assertThrows(ConfigFileConnectorException.class, () -> 
            subjectFalse.readConfiguration());
    }

    @Test
    @DisplayName("File not written: throw Exception to end Application")
    public void testCanWriteFileThrowsException() {
        Exception exception = assertThrows(ConfigFileConnectorException.class, () ->
            subjectFalse.canWrite());
        assertEquals(ConfigFileConnector.FILE_NOT_WRITTEN_MSG, exception.getMessage());
    }

    @Test
    @DisplayName("File can be written: return true")
    public void testCanWriteFileReturnsTrue() {
        boolean actual = subject.canWrite();

        assertThat(actual).isTrue();
    }

    @Test
    @DisplayName("Should set fileBasedConfiguration")
    public void writeConfigurationToFile() {
        ConfigFileConnector dummySubject = new ConfigFileConnector();
        dummySubject.setup(existingPath, "foobar");

        assertThat(dummySubject.readConfiguration()).isEmpty();

        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("foo", "bar");
        dummySubject.writeConfiguration(expectedMap);

        Map<String, String> actualMap = dummySubject.readConfiguration();
        
        assertThat(actualMap).isEqualTo(expectedMap);

        dummySubject.file.delete();
    }

    @Test
    @DisplayName("Should throw exception")
    public void saveFile() {
        ConfigFileConnector dummySubject = new ConfigFileConnector();
        dummySubject.setup(existingPath, "foobar");

        dummySubject.builder = new DummyBuilder(PropertiesConfiguration.class);
        
        assertThrows(ConfigFileConnectorException.class, ()
            -> dummySubject.save());
        
        dummySubject.file.delete();
    }
}
