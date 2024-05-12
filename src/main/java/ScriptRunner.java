import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import gui.OperacionesBD;
import gui.custom_components.ErrorJOptionPane;
import gui.custom_components.MessageJOptionPane;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;

public class ScriptRunner implements OperacionesBD {
    private final Connection connection;

    public ScriptRunner() throws SQLException, JSchException {
        // Conexión SSH a la MV remota
        Session session = jsch.getSession(sshUser, sshHost, sshPort);
        session.setPassword(sshPassword);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();

        // Abrir un túnel SSH al puerto MySQL en la máquina remota
        session.setPortForwardingL(localPort, remoteHost, remotePort);

        // Conexión a MySQL a través del túnel SSH
        String dbUrl = "jdbc:mysql://localhost:" + localPort + "/OLYMPULL_DB";
        String dbUser = "root";
        String dbPassword = "root";
        this.connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    public void runScript(String filePath) throws SQLException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            StringBuilder queryBuilder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                // Ignorar líneas en blanco o comentarios
                if (!line.trim().isEmpty() && !line.trim().startsWith("--")) {
                    queryBuilder.append(line.trim());

                    // Si la línea no termina con punto y coma, la sentencia es multilínea
                    if (!line.trim().endsWith(";")) {
                        queryBuilder.append(" "); // Agregar espacio entre líneas
                    } else {
                        executeQuery(queryBuilder.toString());
                        queryBuilder.setLength(0); // Limpiar el constructor para la siguiente sentencia
                    }
                }
            }
        } catch (Exception e) {
            new ErrorJOptionPane(e.getMessage());
        }
    }

    private void executeQuery(String query) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute(query);
        }
    }

    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    public static void main(String[] args) {
        try {
            ScriptRunner scriptRunner = new ScriptRunner();
            scriptRunner.runScript("create-database.sql");
            scriptRunner.closeConnection();
            new MessageJOptionPane("Script ejecutado con éxito");
        } catch (SQLException | JSchException e) {
            new ErrorJOptionPane(e.getMessage());
        }
    }
}
