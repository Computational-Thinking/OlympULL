package operations;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import file_management.PropertiesReader;
import gui.custom_components.option_panes.ErrorJOptionPane;

import java.sql.*;

public interface DBOperations extends PropertiesReader {
    // Información de la máquina local
    String sshHost = PropertiesReader.getSshHost();
    String sshUser = PropertiesReader.getSshUser();
    String sshPassword = PropertiesReader.getSshPassword();
    int sshPort = PropertiesReader.getSshPort(); // Puerto SSH por defecto
    int localPort = PropertiesReader.getLocalPort(); // Puerto local para el túnel SSH

    // Información de la máquina remota
    String remoteHost = PropertiesReader.getRemoteHost(); // La conexión MySQL se hará desde la máquina remota
    int remotePort = PropertiesReader.getRemotePort(); // Puerto MySQL en la máquina remota
    String dbUser = PropertiesReader.getRemoteUser();
    String dbPassword = PropertiesReader.getRemotePassword();
    String databaseName = PropertiesReader.getDatabaseName();

    // Conexión a MySQL por SSH
    String dbUrl = "jdbc:mysql://" + remoteHost + ":" + localPort + "/" + databaseName;
    JSch jsch = new JSch();

    // Método para obtener filas de tabla
    default ResultSet selectRows(String table, String columns, String orderColumn, String where) {
        Session session = null;
        ResultSet rs = null;

        try {
            // Conexión SSH a la MV remota
            session = jsch.getSession(sshUser, sshHost, sshPort);
            session.setPassword(sshPassword);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            // Abrir un túnel SSH al puerto MySQL en la máquina remota
            session.setPortForwardingL(localPort, remoteHost, remotePort);

            // Conexión a MySQL a través del túnel SSH
            String dbUrl = "jdbc:mysql://localhost:" + localPort + "/OLYMPULL_DB";
            String dbUser = "root";
            String dbPassword = "root";
            Connection conn;
            conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

            String select = "SELECT " + columns + " FROM " + table + " " + where + " ORDER BY " + orderColumn + " ASC;";
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(select);

            // Se cierra la conexión
            session.disconnect();

        } catch (JSchException | SQLException ex) {
            new ErrorJOptionPane(ex.getMessage());
            assert session != null;
            session.disconnect();
        }

        return rs;
    }

    default ResultSet selectRows(String table, String orderColumn) {
        return selectRows(table, "*", orderColumn, "");
    }

    // Método para obtener valores de una columna
    default ResultSet selectCol(String table, String col, String where) {
        Session session = null;
        ResultSet rs = null;

        try {
            // Conexión SSH a la MV remota
            session = jsch.getSession(sshUser, sshHost, sshPort);
            session.setPassword(sshPassword);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            // Abrir un túnel SSH al puerto MySQL en la máquina remota
            session.setPortForwardingL(localPort, remoteHost, remotePort);

            // Conexión a MySQL a través del túnel SSH
            String dbUrl = "jdbc:mysql://localhost:" + localPort + "/OLYMPULL_DB";
            String dbUser = "root";
            String dbPassword = "root";
            Connection conn;
            conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

            String select = "SELECT " + col + " FROM " + table + " " + where + ";";
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(select);

            // Se cierra la conexión
            session.disconnect();

        } catch (JSchException | SQLException ex) {
            new ErrorJOptionPane(ex.getMessage());
            assert session != null;
            session.disconnect();
        }

        return rs;
    }

    default ResultSet selectCol(String table, String col) {
        return selectCol(table, col, "");
    }

    // Método para insertar en la base de datos
    default int insert(String table, String data, String selection) {
        Session session = null;

        try {
            // Conexión SSH a la MV remota
            session = jsch.getSession(sshUser, sshHost, sshPort);
            session.setPassword(sshPassword);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            // Túnel SSH al puerto MySQL en la máquina remota
            session.setPortForwardingL(localPort, remoteHost, remotePort);
            Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

            // Crear la sentencia
            Statement stmt = conn.createStatement();

            // Consulta para añadir nuevo registro
            String insertClause = "INSERT INTO " + table + selection + " VALUES(" + data + ")";
            stmt.executeUpdate(insertClause);

            // Se cierra la conexión
            stmt.close();
            conn.close();
            session.disconnect();

            return 0;

        } catch (JSchException | SQLException ex) {
            new ErrorJOptionPane(ex.getMessage());
            assert session != null;
            session.disconnect();

            return 1;

        }
    }

    default int insert(String table, String data) {
        return insert(table, data, "");
    }

    // Método para actualizar valor en la tabla
    default int update(String table, String setClause, String whereClause) {
        Session session = null;

        try {
            // Conexión SSH a la MV remota
            session = jsch.getSession(sshUser, sshHost, sshPort);
            session.setPassword(sshPassword);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            // Túnel SSH al puerto MySQL en la máquina remota
            session.setPortForwardingL(localPort, remoteHost, remotePort);
            Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

            // Crear la sentencia
            Statement stmt = conn.createStatement();

            // Consulta para añadir
            String updateClause = "UPDATE " + table + " " + setClause + " " + whereClause;
            stmt.executeUpdate(updateClause);

            // Se cierra la conexión
            stmt.close();
            conn.close();
            session.disconnect();

            return 0;

        } catch (JSchException | SQLException ex) {
            new ErrorJOptionPane(ex.getMessage());
            assert session != null;
            session.disconnect();

            return 1;

        }
    }

    // Método para eliminar fila de la tabla
    default int delete(String table, String whereClause) {
        Session session = null;

        try {
            // Conexión SSH a la MV remota
            session = jsch.getSession(sshUser, sshHost, sshPort);
            session.setPassword(sshPassword);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            // Túnel SSH al puerto MySQL en la máquina remota
            session.setPortForwardingL(localPort, remoteHost, remotePort);
            Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

            // Crear la sentencia
            Statement stmt = conn.createStatement();

            // Consulta para añadir la nueva olimpiada
            String deleteClause = "DELETE FROM " + table + " " + whereClause;
            stmt.executeUpdate(deleteClause);

            // Se cierra la conexión
            stmt.close();
            conn.close();
            session.disconnect();

            return 0;

        } catch (SQLException ex) {
            new ErrorJOptionPane("No se ha podido eliminar. Hay una referencia a este objeto en otra tabla");
            session.disconnect();

            return 1;

        } catch (JSchException ex) {
            new ErrorJOptionPane(ex.getMessage());
            assert session != null;
            session.disconnect();

            return 1;
        }
    }
}
