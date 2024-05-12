package interfaz.custom_components;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static java.lang.Integer.parseInt;

public interface ConfigReader {
    Properties properties = new Properties();

    static String getSshHost() {
        try {
            properties.load(new FileInputStream("config.properties"));

        } catch (IOException e) {
            new ErrorJOptionPane("No se ha podido recuperar el puerto SSH de config.properties");

        }

        return properties.getProperty("ssh_host");
    }

    static String getSshUser() {
        try {
            properties.load(new FileInputStream("config.properties"));

        } catch (IOException e) {
            new ErrorJOptionPane("No se ha podido recuperar el username de la máquina remota de config.properties");

        }

        return properties.getProperty("ssh_user");
    }

    static String getSshPassword() {
        try {
            properties.load(new FileInputStream("config.properties"));

        } catch (IOException e) {
            new ErrorJOptionPane("No se ha podido recuperar la contraseña de la máquina remota de config.properties");

        }

        return properties.getProperty("ssh_password");
    }

    static int getSshPort() {
        try {
            properties.load(new FileInputStream("config.properties"));

        } catch (IOException e) {
            new ErrorJOptionPane("No se ha podido recuperar el username de la máquina remota de config.properties");

        }

        return Integer.parseInt(properties.getProperty("ssh_port"));
    }

    static int getLocalPort() {
        try {
            properties.load(new FileInputStream("config.properties"));

        } catch (IOException e) {
            new ErrorJOptionPane("No se ha podido recuperar el puerto de túnel SSH de config.properties");

        }

        return Integer.parseInt(properties.getProperty("ssh_local_port"));
    }

    static String getRemoteHost() {
        try {
            properties.load(new FileInputStream("config.properties"));

        } catch (IOException e) {
            new ErrorJOptionPane("No se ha podido recuperar el nombre del host remoto de config.properties");

        }

        return properties.getProperty("db_host");
    }

    static int getRemotePort() {
        try {
            properties.load(new FileInputStream("config.properties"));

        } catch (IOException e) {
            new ErrorJOptionPane("No se ha podido recuperar el puerto remoto de config.properties");

        }

        return Integer.parseInt(properties.getProperty("db_port"));
    }

    static String getRemoteUser() {
        try {
            properties.load(new FileInputStream("config.properties"));

        } catch (IOException e) {
            new ErrorJOptionPane("No se ha podido recuperar el nombre de la base de datos de config.properties");

        }

        return properties.getProperty("db_user");
    }

    static String getRemotePassword() {
        try {
            properties.load(new FileInputStream("config.properties"));

        } catch (IOException e) {
            new ErrorJOptionPane("No se ha podido recuperar el username del usuario de la base de datos de config.properties");

        }

        return properties.getProperty("db_password");
    }

    static String getDatabaseName() {
        try {
            properties.load(new FileInputStream("config.properties"));

        } catch (IOException e) {
            new ErrorJOptionPane("No se ha podido recuperar la contraseña del usuario de la base de datos de config.properties");

        }

        return properties.getProperty("db_name");
    }

    static String getDataFilesPath() {
        try {
            properties.load(new FileInputStream("data.properties"));

        } catch (IOException e) {
            new ErrorJOptionPane("No se ha podido recuperar la ruta de los archivos de datos de config.properties");

        }

        return properties.getProperty("data_file_path");
    }

    static String getOlympiadsFileName() {
        try {
            properties.load(new FileInputStream("data.properties"));

        } catch (IOException e) {
            new ErrorJOptionPane("No se ha podido recuperar de config.properties");

        }

        return properties.getProperty("olympiads_file");
    }

    static String getItinerariesFileName() {
        try {
            properties.load(new FileInputStream("data.properties"));

        } catch (IOException e) {
            new ErrorJOptionPane("No se ha podido recuperar de config.properties");

        }

        return properties.getProperty("itineraries_file");
    }

    static String getExercisesFileName() {
        try {
            properties.load(new FileInputStream("data.properties"));

        } catch (IOException e) {
            new ErrorJOptionPane("No se ha podido recuperar de config.properties");

        }

        return properties.getProperty("exercises_file");
    }

    static String getRubricsFileName() {
        try {
            properties.load(new FileInputStream("data.properties"));

        } catch (IOException e) {
            new ErrorJOptionPane("No se ha podido recuperar de config.properties");

        }

        return properties.getProperty("rubrics_file");
    }

    static String getTeamsFileName() {
        try {
            properties.load(new FileInputStream("data.properties"));

        } catch (IOException e) {
            new ErrorJOptionPane("No se ha podido recuperar de config.properties");

        }

        return properties.getProperty("teams_file");
    }

    static String getExOlympAssignationsFileName() {
        try {
            properties.load(new FileInputStream("data.properties"));

        } catch (IOException e) {
            new ErrorJOptionPane("No se ha podido recuperar de config.properties");

        }

        return properties.getProperty("ex_olymp_file");
    }

    static String getUsersFileName() {
        try {
            properties.load(new FileInputStream("data.properties"));

        } catch (IOException e) {
            new ErrorJOptionPane("No se ha podido recuperar de config.properties");

        }

        return properties.getProperty("users_file");
    }

    static String getExMonitorAssignationsFileName() {
        try {
            properties.load(new FileInputStream("data.properties"));

        } catch (IOException e) {
            new ErrorJOptionPane("No se ha podido recuperar de config.properties");

        }

        return properties.getProperty("ex_monitor_file");
    }

    static String getItOrganizerAssignationsFileName() {
        try {
            properties.load(new FileInputStream("data.properties"));

        } catch (IOException e) {
            new ErrorJOptionPane("No se ha podido recuperar de config.properties");

        }

        return properties.getProperty("it_org_file");
    }
}

