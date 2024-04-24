package interfaz;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import javax.swing.*;
import java.io.IOException;
import java.sql.*;

public interface OperacionesBD extends ConfigReader {
    // Información de la máquina local
    String sshHost = ConfigReader.getSshHost();
    String sshUser = ConfigReader.getSshUser();
    String sshPassword = ConfigReader.getSshPassword();
    int sshPort = ConfigReader.getSshPort(); // Puerto SSH por defecto
    int localPort = ConfigReader.getLocalPort(); // Puerto local para el túnel SSH

    // Información de la máquina remota
    String remoteHost = ConfigReader.getRemoteHost(); // La conexión MySQL se hará desde la máquina remota
    int remotePort = ConfigReader.getRemotePort(); // Puerto MySQL en la máquina remota
    String dbUser = ConfigReader.getRemoteUser();
    String dbPassword = ConfigReader.getRemotePassword();
    String databaseName = ConfigReader.getDatabaseName();

    // Conexión a MySQL por SSH
    String dbUrl = "jdbc:mysql://localhost:" + localPort + "/" + databaseName;

    JSch jsch = new JSch();

    // Método para obtener filas de tabla
    default ResultSet selectRows(String table, String orderColumn) throws JSchException, SQLException {
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
        Connection conn;
        conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

        String select = "SELECT * FROM " + table + " ORDER BY " + orderColumn + " ASC;";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(select);

        // Se cierra la conexión
        session.disconnect();

        return rs;
    }

    // Método para obtener valores de una columna
    default ResultSet selectCol(String table, String col) throws JSchException, SQLException {
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
        Connection conn;
        conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

        String select = "SELECT " + col + " FROM " + table +";";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(select);

        // Se cierra la conexión
        session.disconnect();

        return rs;
    }

    default ResultSet selectColWhere(String table, String col, String where) throws JSchException, SQLException {
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
        Connection conn;
        conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

        String select = "SELECT " + col + " FROM " + table + " " + where + ";";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(select);

        // Se cierra la conexión
        session.disconnect();

        return rs;
    }

    // Método para insertar en la base de datos
    default int insert(String table, String data, String safeInsertClause) throws JSchException, SQLException {
        // Conexión SSH a la MV remota
        Session session = jsch.getSession(sshUser, sshHost, sshPort);
        session.setPassword(sshPassword);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();

        // Túnel SSH al puerto MySQL en la máquina remota
        session.setPortForwardingL(localPort, remoteHost, remotePort);
        Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

        // Crear la sentencia
        Statement stmt = conn.createStatement();

        // Comprobación para no insertar si ya existe una fila con la misma key
        String checkClause = "SELECT * FROM "  + table + " " + safeInsertClause;
        ResultSet results = stmt.executeQuery(checkClause);

        if (results.next()) {
            new CustomJOptionPane("ERROR - Ya existe un registro en " + table + " con esa clave o se viola una restricción");
            stmt.close();
            conn.close();
            session.disconnect();

            return 1;
        }

        // Consulta para añadir nuevo registro
        String insertClause = "INSERT INTO " + table + " VALUES(" + data + ")";
        int rowsAffected = stmt.executeUpdate(insertClause);

        if (!(rowsAffected > 0)) {
            new CustomJOptionPane("No se ha podido insertar en la tabla");
        }

        // Se cierra la conexión
        stmt.close();
        conn.close();
        session.disconnect();

        return 0;
    }

    default int insertSelectedCols(String table, String data, String selection, String safeInsertClause) throws JSchException, SQLException {
        // Conexión SSH a la MV remota
        Session session = jsch.getSession(sshUser, sshHost, sshPort);
        session.setPassword(sshPassword);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();

        // Túnel SSH al puerto MySQL en la máquina remota
        session.setPortForwardingL(localPort, remoteHost, remotePort);
        Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

        // Crear la sentencia
        Statement stmt = conn.createStatement();

        // Comprobación para no insertar si ya existe una fila con la misma key
        String checkClause = "SELECT * FROM "  + table + " " + safeInsertClause;
        ResultSet results = stmt.executeQuery(checkClause);

        if (results.next()) {
            new CustomJOptionPane("ERROR - Ya existe un registro en " + table + " con esa clave o se viola una restricción de clave única");
            stmt.close();
            conn.close();
            session.disconnect();

            return 1;
        }

        // Consulta para añadir nuevo registro
        String insertClause = "INSERT INTO " + table + selection + " VALUES(" + data + ");";
        int rowsAffected = stmt.executeUpdate(insertClause);

        if (!(rowsAffected > 0)) {
            new CustomJOptionPane("No se ha podido insertar en la tabla");
        }

        // Se cierra la conexión
        stmt.close();
        conn.close();
        session.disconnect();

        return 0;
    }

    // Método para actualizar valor en la tabla
    default int update(String table, String setClause, String whereClause, String safeInsertClause) throws JSchException, SQLException {
        // Conexión SSH a la MV remota
        Session session = jsch.getSession(sshUser, sshHost, sshPort);
        session.setPassword(sshPassword);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();

        // Túnel SSH al puerto MySQL en la máquina remota
        session.setPortForwardingL(localPort, remoteHost, remotePort);
        Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

        // Crear la sentencia
        Statement stmt = conn.createStatement();

        /**
        // Comprobación para no insertar si ya existe una fila con la misma key
        String checkClause = "SELECT * FROM "  + table + " " + safeInsertClause;
        ResultSet results = stmt.executeQuery(checkClause);

        if (results.next()) {
            new CustomJOptionPane("ERROR - Ya existe un registro en " + table + " con esa clave o se viola una restricción");
            stmt.close();
            conn.close();
            session.disconnect();

            return 1;
        }
         */

        // Consulta para añadir
        String updateClause = "UPDATE " + table + " " + setClause + " " + whereClause;
        int rowsAffected = stmt.executeUpdate(updateClause);

        if (!(rowsAffected > 0)) {
            new CustomJOptionPane("No se ha podido actualizar la tabla");

            stmt.close();
            conn.close();
            session.disconnect();

            return 1;
        }

        // Se cierra la conexión
        stmt.close();
        conn.close();
        session.disconnect();

        return 0;
    }

    // Método para eliminar fila de la tabla
    default int delete(String table, String whereClause) throws JSchException, SQLException {
        // Conexión SSH a la MV remota
        Session session = jsch.getSession(sshUser, sshHost, sshPort);
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

        try {
            stmt.executeUpdate(deleteClause);

        } catch (SQLException e) {
            new CustomJOptionPane("No se puede eliminar el registro porque existen referencias a él en otras tablas");
            stmt.close();
            conn.close();
            session.disconnect();
            return 1;

        }

        // Se cierra la conexión
        stmt.close();
        conn.close();
        session.disconnect();

        return 0;
    }
}
