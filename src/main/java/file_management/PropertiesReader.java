package file_management;

import gui.custom_components.option_panes.ErrorJOptionPane;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public interface PropertiesReader {
    Properties properties = new Properties();

    private static void loadConfiguration() {
        try {
            ClassLoader cl = PropertiesReader.class.getClassLoader();
            InputStream configFile = cl.getResourceAsStream("src/main/resources/config.properties");
            properties.load(configFile);
        } catch (RuntimeException | IOException ex) {
            new ErrorJOptionPane(ex.getMessage());
        }
    }

    private static void loadData() {
        try {
            ClassLoader cl = PropertiesReader.class.getClassLoader();
            InputStream configFile = cl.getResourceAsStream("src/main/resources/data.properties");
            properties.load(configFile);
        } catch (RuntimeException | IOException ex) {
            new ErrorJOptionPane(ex.getMessage());
        }
    }

    static String getSshHost() {
        loadConfiguration();
        return properties.getProperty("ssh_host");
    }

    static String getSshUser() {
        loadConfiguration();
        return properties.getProperty("ssh_user");
    }

    static String getSshPassword() {
        loadConfiguration();
        return properties.getProperty("ssh_password");
    }

    static int getSshPort() {
        loadConfiguration();
        return Integer.parseInt(properties.getProperty("ssh_port"));
    }

    static int getLocalPort() {
        loadConfiguration();
        return Integer.parseInt(properties.getProperty("ssh_local_port"));
    }

    static String getRemoteHost() {
        loadConfiguration();
        return properties.getProperty("db_host");
    }

    static int getRemotePort() {
        loadConfiguration();
        return Integer.parseInt(properties.getProperty("db_port"));
    }

    static String getRemoteUser() {
        loadConfiguration();
        return properties.getProperty("db_user");
    }

    static String getRemotePassword() {
        loadConfiguration();
        return properties.getProperty("db_password");
    }

    static String getDatabaseName() {
        loadConfiguration();
        return properties.getProperty("db_name");
    }

    static String getDataFilesPath() {
        loadData();
        return properties.getProperty("data_file_path");
    }

    static String getOlympiadsFileName() {
        loadData();
        return properties.getProperty("olympiads_file");
    }

    static String getItinerariesFileName() {
        loadData();
        return properties.getProperty("itineraries_file");
    }

    static String getExercisesFileName() {
        loadData();
        return properties.getProperty("exercises_file");
    }

    static String getRubricsFileName() {
        loadData();
        return properties.getProperty("rubrics_file");
    }

    static String getTeamsFileName() {
        loadData();
        return properties.getProperty("teams_file");
    }

    static String getExOlympAssignationsFileName() {
        loadData();
        return properties.getProperty("ex_olymp_file");
    }

    static String getUsersFileName() {
        loadData();
        return properties.getProperty("users_file");
    }

    static String getExMonitorAssignationsFileName() {
        loadData();
        return properties.getProperty("ex_monitor_file");
    }

    static String getItOrganizerAssignationsFileName() {
        loadData();
        return properties.getProperty("it_org_file");
    }
}

