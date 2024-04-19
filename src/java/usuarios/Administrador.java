package usuarios;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import javax.swing.*;
import java.sql.*;
import java.util.Arrays;

public class Administrador extends Usuario {
    public Administrador(String nombre, String password) {
        this.userName = nombre;
        this.password = password;
        this.userType = "admin";
    }

    public void createOlympiad(String code, String title, String description, int year) throws JSchException, SQLException {
        // Valores para conexión a MV remota
        String sshHost = "10.6.130.204";
        String sshUser = "usuario";
        String sshPassword = "Usuario";
        int sshPort = 22; // Puerto SSH por defecto
        int localPort = 3307; // Puerto local para el túnel SSH
        String remoteHost = "localhost"; // La conexión MySQL se hará desde la máquina remota
        int remotePort = 3306; // Puerto MySQL en la máquina remota

        // Conexión SSH a la MV remota
        JSch jsch = new JSch();
        Session session = jsch.getSession(sshUser, sshHost, sshPort);
        session.setPassword(sshPassword);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();

        // Debugger
        System.out.println("Conexión con la máquina establecida");

        // Abrir un túnel SSH al puerto MySQL en la máquina remota
        session.setPortForwardingL(localPort, remoteHost, remotePort);

        // Conexión a MySQL a través del túnel SSH
        String dbUrl = "jdbc:mysql://localhost:" + localPort + "/OLYMPULL_DB";
        String dbUser = "root";
        String dbPassword = "root";
        Connection conn;
        conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

        // Debugger
        Statement stmt = conn.createStatement();

        // Consulta para añadir la nueva olimpiada
        String sql = "INSERT INTO T_OLIMPIADAS VALUES ('" + code + "', '" + title + "', '" + description + "', " + year + ")";
        int rowsAffected = stmt.executeUpdate(sql);

        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(null, "Se ha creado la olimpiada " + title + ".");
        } else {
            JOptionPane.showMessageDialog(null, "ERROR. No se ha podido crear la olimpiada " + title + ".");
        }

        stmt.close();
        conn.close();
        session.disconnect();
    }

    public void modifyOlympiad(String oldCode, String code, String title, String desc, int year) throws JSchException, SQLException {
        // Valores para conexión a MV remota
        String sshHost = "10.6.130.204";
        String sshUser = "usuario";
        String sshPassword = "Usuario";
        int sshPort = 22; // Puerto SSH por defecto
        int localPort = 3307; // Puerto local para el túnel SSH
        String remoteHost = "localhost"; // La conexión MySQL se hará desde la máquina remota
        int remotePort = 3306; // Puerto MySQL en la máquina remota

        // Conexión SSH a la MV remota
        JSch jsch = new JSch();
        Session session = jsch.getSession(sshUser, sshHost, sshPort);
        session.setPassword(sshPassword);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();

        // Debugger
        System.out.println("Conexión con la máquina establecida");

        // Abrir un túnel SSH al puerto MySQL en la máquina remota
        session.setPortForwardingL(localPort, remoteHost, remotePort);

        // Conexión a MySQL a través del túnel SSH
        String dbUrl = "jdbc:mysql://localhost:" + localPort + "/OLYMPULL_DB";
        String dbUser = "root";
        String dbPassword = "root";
        Connection conn;
        conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

        // Debugger
        Statement stmt = conn.createStatement();

        // Ejecutar la consulta SQL
        String sql1 = "SELECT * FROM T_OLIMPIADAS WHERE CODIGO= " + "'" + oldCode + "'";
        ResultSet rs = stmt.executeQuery(sql1);

        if (rs.next()) {
            String sql2 = "UPDATE T_OLIMPIADAS " +
                    "SET CODIGO='" + code + "', " +
                        "TITULO='" + title + "', " +
                        "DESCRIPCION='" + desc + "', " +
                        "YEAR=" + year + " WHERE CODIGO='" + oldCode + "';";
            int rowsAffected = stmt.executeUpdate(sql2);

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Olimpiada modificada con éxito.");

            } else {
                JOptionPane.showMessageDialog(null, "ERROR. No se ha podido modificar la olimpiada.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "ERROR. No existe la olimpiada.");
        }

        conn.close();
        session.disconnect();
    }

    public void deleteOlympiad(String codigoOlimpiada) throws JSchException, SQLException {
        // Valores para conexión a MV remota
        String sshHost = "10.6.130.204";
        String sshUser = "usuario";
        String sshPassword = "Usuario";
        int sshPort = 22; // Puerto SSH por defecto
        int localPort = 3307; // Puerto local para el túnel SSH
        String remoteHost = "localhost"; // La conexión MySQL se hará desde la máquina remota
        int remotePort = 3306; // Puerto MySQL en la máquina remota

        // Conexión SSH a la MV remota
        JSch jsch = new JSch();
        Session session = jsch.getSession(sshUser, sshHost, sshPort);
        session.setPassword(sshPassword);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();

        // Debugger
        System.out.println("Conexión con la máquina establecida");

        // Abrir un túnel SSH al puerto MySQL en la máquina remota
        session.setPortForwardingL(localPort, remoteHost, remotePort);

        // Conexión a MySQL a través del túnel SSH
        String dbUrl = "jdbc:mysql://localhost:" + localPort + "/OLYMPULL_DB";
        String dbUser = "root";
        String dbPassword = "root";
        Connection conn;
        conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

        // Debugger
        Statement stmt = conn.createStatement();

        // Ejecutar la consulta SQL
        String sql1 = "SELECT CODIGO FROM T_OLIMPIADAS WHERE CODIGO = " + "'" + codigoOlimpiada + "'";
        ResultSet rs = stmt.executeQuery(sql1);

        if (rs.next()) {
            String sql2 = "DELETE FROM T_OLIMPIADAS WHERE CODIGO = '" + codigoOlimpiada + "';";
            int rowsAffected = stmt.executeUpdate(sql2);

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Olimpiada eliminada con éxito.");

            } else {
                JOptionPane.showMessageDialog(null, "ERROR. No se ha podido eliminar la olimpiada.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "ERROR. No existe la olimpiada.");
        }

        conn.close();
        session.disconnect();
    }

    public void createItinerario(String codigo, String titulo, String descripcion, String olymp) throws JSchException, SQLException {
        // Valores para conexión a MV remota
        String sshHost = "10.6.130.204";
        String sshUser = "usuario";
        String sshPassword = "Usuario";
        int sshPort = 22; // Puerto SSH por defecto
        int localPort = 3307; // Puerto local para el túnel SSH
        String remoteHost = "localhost"; // La conexión MySQL se hará desde la máquina remota
        int remotePort = 3306; // Puerto MySQL en la máquina remota

        // Conexión SSH a la MV remota
        JSch jsch = new JSch();
        Session session = jsch.getSession(sshUser, sshHost, sshPort);
        session.setPassword(sshPassword);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();

        // Debugger
        System.out.println("Conexión con la máquina establecida");

        // Abrir un túnel SSH al puerto MySQL en la máquina remota
        session.setPortForwardingL(localPort, remoteHost, remotePort);

        // Conexión a MySQL a través del túnel SSH
        String dbUrl = "jdbc:mysql://localhost:" + localPort + "/OLYMPULL_DB";
        String dbUser = "root";
        String dbPassword = "root";
        Connection conn;
        conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

        // Debugger
        Statement stmt = conn.createStatement();

        // Consulta para añadir la nueva olimpiada
        String sql = "INSERT INTO T_ITINERARIOS VALUES ('" + codigo + "', '" + titulo + "', '" + descripcion + "', '" + olymp + "')";
        int rowsAffected = stmt.executeUpdate(sql);

        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(null, "Se ha creado el itinerario " + titulo + ".");
        } else {
            JOptionPane.showMessageDialog(null, "ERROR. No se ha podido crear el itinerario " + titulo + ".");
        }

        stmt.close();
        conn.close();
        session.disconnect();
    }

    public void modifyItinerario(String oldCode, String code, String title, String desc, String olymp) throws JSchException, SQLException {
        // Valores para conexión a MV remota
        String sshHost = "10.6.130.204";
        String sshUser = "usuario";
        String sshPassword = "Usuario";
        int sshPort = 22; // Puerto SSH por defecto
        int localPort = 3307; // Puerto local para el túnel SSH
        String remoteHost = "localhost"; // La conexión MySQL se hará desde la máquina remota
        int remotePort = 3306; // Puerto MySQL en la máquina remota

        // Conexión SSH a la MV remota
        JSch jsch = new JSch();
        Session session = jsch.getSession(sshUser, sshHost, sshPort);
        session.setPassword(sshPassword);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();

        // Debugger
        System.out.println("Conexión con la máquina establecida");

        // Abrir un túnel SSH al puerto MySQL en la máquina remota
        session.setPortForwardingL(localPort, remoteHost, remotePort);

        // Conexión a MySQL a través del túnel SSH
        String dbUrl = "jdbc:mysql://localhost:" + localPort + "/OLYMPULL_DB";
        String dbUser = "root";
        String dbPassword = "root";
        Connection conn;
        conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

        // Debugger
        Statement stmt = conn.createStatement();

        // Ejecutar la consulta SQL
        String sql1 = "SELECT * FROM T_ITINERARIOS WHERE CODIGO= " + "'" + oldCode + "'";
        ResultSet rs = stmt.executeQuery(sql1);

        if (rs.next()) {
            String sql2 = "UPDATE T_ITINERARIOS " +
                    "SET CODIGO='" + code + "', " +
                    "TITULO='" + title + "', " +
                    "DESCRIPCION='" + desc + "', " +
                    "OLIMPIADA='" + olymp + "' WHERE CODIGO='" + oldCode + "';";
            int rowsAffected = stmt.executeUpdate(sql2);

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Itinerario modificado con éxito.");

            } else {
                JOptionPane.showMessageDialog(null, "ERROR. No se ha podido modificar el itinerario.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "ERROR. No existe el itinerario.");
        }

        conn.close();
        session.disconnect();
    }

    public void deleteItinerario(String codigoItinerario) throws JSchException, SQLException {
        // Valores para conexión a MV remota
        String sshHost = "10.6.130.204";
        String sshUser = "usuario";
        String sshPassword = "Usuario";
        int sshPort = 22; // Puerto SSH por defecto
        int localPort = 3307; // Puerto local para el túnel SSH
        String remoteHost = "localhost"; // La conexión MySQL se hará desde la máquina remota
        int remotePort = 3306; // Puerto MySQL en la máquina remota

        // Conexión SSH a la MV remota
        JSch jsch = new JSch();
        Session session = jsch.getSession(sshUser, sshHost, sshPort);
        session.setPassword(sshPassword);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();

        // Debugger
        System.out.println("Conexión con la máquina establecida");

        // Abrir un túnel SSH al puerto MySQL en la máquina remota
        session.setPortForwardingL(localPort, remoteHost, remotePort);

        // Conexión a MySQL a través del túnel SSH
        String dbUrl = "jdbc:mysql://localhost:" + localPort + "/OLYMPULL_DB";
        String dbUser = "root";
        String dbPassword = "root";
        Connection conn;
        conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

        // Debugger
        Statement stmt = conn.createStatement();

        // Ejecutar la consulta SQL
        String sql1 = "SELECT CODIGO FROM T_ITINERARIOS WHERE CODIGO = " + "'" + codigoItinerario + "'";
        ResultSet rs = stmt.executeQuery(sql1);

        if (rs.next()) {
            String sql2 = "DELETE FROM T_ITINERARIOS WHERE CODIGO = '" + codigoItinerario + "';";
            int rowsAffected = stmt.executeUpdate(sql2);

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Itinerario eliminado con éxito.");

            } else {
                JOptionPane.showMessageDialog(null, "ERROR. No se ha podido eliminar el itinerario.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "ERROR. No existe el itinerario.");
        }

        conn.close();
        session.disconnect();
    }

    public void createExercise(String code, String title, String desc, String concept, String resources, String type, String rubrica) throws JSchException, SQLException {
        // Valores para conexión a MV remota
        String sshHost = "10.6.130.204";
        String sshUser = "usuario";
        String sshPassword = "Usuario";
        int sshPort = 22; // Puerto SSH por defecto
        int localPort = 3307; // Puerto local para el túnel SSH
        String remoteHost = "localhost"; // La conexión MySQL se hará desde la máquina remota
        int remotePort = 3306; // Puerto MySQL en la máquina remota

        // Conexión SSH a la MV remota
        JSch jsch = new JSch();
        Session session = jsch.getSession(sshUser, sshHost, sshPort);
        session.setPassword(sshPassword);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();

        // Debugger
        System.out.println("Conexión con la máquina establecida");

        // Abrir un túnel SSH al puerto MySQL en la máquina remota
        session.setPortForwardingL(localPort, remoteHost, remotePort);

        // Conexión a MySQL a través del túnel SSH
        String dbUrl = "jdbc:mysql://localhost:" + localPort + "/OLYMPULL_DB";
        String dbUser = "root";
        String dbPassword = "root";
        Connection conn;
        conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

        // Debugger
        Statement stmt = conn.createStatement();

        // Ejecutar consulta para añadir nuevo ejercicio
        String sql = "INSERT INTO T_EJERCICIOS VALUES ('" + code + "', '" + title + "', '" + desc + "', '" + concept + "', '" + resources + "', '" + type + "', '" + rubrica + "');";
        int rowsAffected = stmt.executeUpdate(sql);

        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(null, "Se ha creado el ejercicio.");
        } else {
            JOptionPane.showMessageDialog(null, "No se ha podido crear el ejercicio.");
        }

        conn.close();
        session.disconnect();
    }

    public void modifyExercise(String oldCode, String code, String title, String desc, String concepto, String recurso, String tipo, String rubrica) throws JSchException, SQLException {
        // Valores para conexión a MV remota
        String sshHost = "10.6.130.204";
        String sshUser = "usuario";
        String sshPassword = "Usuario";
        int sshPort = 22; // Puerto SSH por defecto
        int localPort = 3307; // Puerto local para el túnel SSH
        String remoteHost = "localhost"; // La conexión MySQL se hará desde la máquina remota
        int remotePort = 3306; // Puerto MySQL en la máquina remota

        // Conexión SSH a la MV remota
        JSch jsch = new JSch();
        Session session = jsch.getSession(sshUser, sshHost, sshPort);
        session.setPassword(sshPassword);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();

        // Debugger
        System.out.println("Conexión con la máquina establecida");

        // Abrir un túnel SSH al puerto MySQL en la máquina remota
        session.setPortForwardingL(localPort, remoteHost, remotePort);

        // Conexión a MySQL a través del túnel SSH
        String dbUrl = "jdbc:mysql://localhost:" + localPort + "/OLYMPULL_DB";
        String dbUser = "root";
        String dbPassword = "root";
        Connection conn;
        conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

        // Debugger
        Statement stmt = conn.createStatement();

        // Ejecutar la consulta SQL
        String sql1 = "SELECT * FROM T_EJERCICIOS WHERE CODIGO= " + "'" + oldCode + "'";
        ResultSet rs = stmt.executeQuery(sql1);

        if (rs.next()) {
            String sql2 = "UPDATE T_EJERCICIOS " +
                    "SET CODIGO='" + code + "', " +
                    "TITULO='" + title + "', " +
                    "DESCRIPCION='" + desc + "', " +
                    "CONCEPTO='" + concepto + "', " +
                    "RECURSOS='" + recurso + "', " +
                    "TIPO='" + tipo + "', " +
                    "RUBRICA='" + rubrica + "' WHERE CODIGO='" + oldCode + "';";
            int rowsAffected = stmt.executeUpdate(sql2);

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Ejercicio modificado con éxito.");

            } else {
                JOptionPane.showMessageDialog(null, "ERROR. No se ha podido modificar el ejercicio.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "ERROR. No existe el ejercicio.");
        }

        conn.close();
        session.disconnect();
    }

    public void deleteEjercicio(String codigoEjercicio) throws JSchException, SQLException {
        // Valores para conexión a MV remota
        String sshHost = "10.6.130.204";
        String sshUser = "usuario";
        String sshPassword = "Usuario";
        int sshPort = 22; // Puerto SSH por defecto
        int localPort = 3307; // Puerto local para el túnel SSH
        String remoteHost = "localhost"; // La conexión MySQL se hará desde la máquina remota
        int remotePort = 3306; // Puerto MySQL en la máquina remota

        // Conexión SSH a la MV remota
        JSch jsch = new JSch();
        Session session = jsch.getSession(sshUser, sshHost, sshPort);
        session.setPassword(sshPassword);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();

        // Debugger
        System.out.println("Conexión con la máquina establecida");

        // Abrir un túnel SSH al puerto MySQL en la máquina remota
        session.setPortForwardingL(localPort, remoteHost, remotePort);

        // Conexión a MySQL a través del túnel SSH
        String dbUrl = "jdbc:mysql://localhost:" + localPort + "/OLYMPULL_DB";
        String dbUser = "root";
        String dbPassword = "root";
        Connection conn;
        conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

        // Debugger
        Statement stmt = conn.createStatement();

        // Ejecutar la consulta SQL
        String sql1 = "SELECT CODIGO FROM T_EJERCICIOS WHERE CODIGO = " + "'" + codigoEjercicio + "'";
        ResultSet rs = stmt.executeQuery(sql1);

        if (rs.next()) {
            String sql2 = "DELETE FROM T_EJERCICIOS WHERE CODIGO = '" + codigoEjercicio + "';";
            int rowsAffected = stmt.executeUpdate(sql2);

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Ejercicio eliminado con éxito.");

            } else {
                JOptionPane.showMessageDialog(null, "ERROR. No se ha podido eliminar el ejercicio.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "ERROR. No existe el ejercicio.");
        }

        conn.close();
        session.disconnect();
    }

    public void createRubric(String code, String nombre, String descripcion, String values, String tags) throws JSchException, SQLException {
        // Valores para conexión a MV remota
        String sshHost = "10.6.130.204";
        String sshUser = "usuario";
        String sshPassword = "Usuario";
        int sshPort = 22; // Puerto SSH por defecto
        int localPort = 3307; // Puerto local para el túnel SSH
        String remoteHost = "localhost"; // La conexión MySQL se hará desde la máquina remota
        int remotePort = 3306; // Puerto MySQL en la máquina remota

        // Conexión SSH a la MV remota
        JSch jsch = new JSch();
        Session session = jsch.getSession(sshUser, sshHost, sshPort);
        session.setPassword(sshPassword);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();

        // Debugger
        System.out.println("Conexión con la máquina establecida");

        // Abrir un túnel SSH al puerto MySQL en la máquina remota
        session.setPortForwardingL(localPort, remoteHost, remotePort);

        // Conexión a MySQL a través del túnel SSH
        String dbUrl = "jdbc:mysql://localhost:" + localPort + "/OLYMPULL_DB";
        String dbUser = "root";
        String dbPassword = "root";
        Connection conn;
        conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

        // Debugger
        Statement stmt = conn.createStatement();

        // Ejecutar consulta para añadir nuevo ejercicio
        String sql = "INSERT INTO T_RUBRICAS VALUES ('" + code + "', '" + nombre + "', '" + descripcion + "', '" + values + "', '" + tags + "');";
        int rowsAffected = stmt.executeUpdate(sql);

        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(null, "Se ha creado la rúbrica.");
        } else {
            JOptionPane.showMessageDialog(null, "No se ha podido crear la rúbrica.");
        }

        conn.close();
        session.disconnect();
    }

    public void modifyRubric(String oldCode, String code, String title, String desc, String values, String tags) throws JSchException, SQLException {
        // Valores para conexión a MV remota
        String sshHost = "10.6.130.204";
        String sshUser = "usuario";
        String sshPassword = "Usuario";
        int sshPort = 22; // Puerto SSH por defecto
        int localPort = 3307; // Puerto local para el túnel SSH
        String remoteHost = "localhost"; // La conexión MySQL se hará desde la máquina remota
        int remotePort = 3306; // Puerto MySQL en la máquina remota

        // Conexión SSH a la MV remota
        JSch jsch = new JSch();
        Session session = jsch.getSession(sshUser, sshHost, sshPort);
        session.setPassword(sshPassword);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();

        // Debugger
        System.out.println("Conexión con la máquina establecida");

        // Abrir un túnel SSH al puerto MySQL en la máquina remota
        session.setPortForwardingL(localPort, remoteHost, remotePort);

        // Conexión a MySQL a través del túnel SSH
        String dbUrl = "jdbc:mysql://localhost:" + localPort + "/OLYMPULL_DB";
        String dbUser = "root";
        String dbPassword = "root";
        Connection conn;
        conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

        // Debugger
        Statement stmt = conn.createStatement();

        // Ejecutar la consulta SQL
        String sql1 = "SELECT * FROM T_RUBRICAS WHERE CODIGO= " + "'" + oldCode + "'";
        ResultSet rs = stmt.executeQuery(sql1);

        if (rs.next()) {
            String sql2 = "UPDATE T_RUBRICAS " +
                    "SET CODIGO='" + code + "', " +
                    "NOMBRE='" + title + "', " +
                    "DESCRIPCION='" + desc + "', " +
                    "PUNTOS_RUBRICA='" + values + "', " +
                    "ETIQUETAS_RUBRICA='" + tags + "' WHERE CODIGO='" + oldCode + "';";
            int rowsAffected = stmt.executeUpdate(sql2);

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Rúbrica modificada con éxito.");

            } else {
                JOptionPane.showMessageDialog(null, "ERROR. No se ha podido modificar la rúbrica.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "ERROR. No existe la rúbrica.");
        }

        conn.close();
        session.disconnect();
    }

    public void deleteRubric(String codigoRubrica) throws JSchException, SQLException {
        // Valores para conexión a MV remota
        String sshHost = "10.6.130.204";
        String sshUser = "usuario";
        String sshPassword = "Usuario";
        int sshPort = 22; // Puerto SSH por defecto
        int localPort = 3307; // Puerto local para el túnel SSH
        String remoteHost = "localhost"; // La conexión MySQL se hará desde la máquina remota
        int remotePort = 3306; // Puerto MySQL en la máquina remota

        // Conexión SSH a la MV remota
        JSch jsch = new JSch();
        Session session = jsch.getSession(sshUser, sshHost, sshPort);
        session.setPassword(sshPassword);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();

        // Debugger
        System.out.println("Conexión con la máquina establecida");

        // Abrir un túnel SSH al puerto MySQL en la máquina remota
        session.setPortForwardingL(localPort, remoteHost, remotePort);

        // Conexión a MySQL a través del túnel SSH
        String dbUrl = "jdbc:mysql://localhost:" + localPort + "/OLYMPULL_DB";
        String dbUser = "root";
        String dbPassword = "root";
        Connection conn;
        conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

        // Debugger
        Statement stmt = conn.createStatement();

        // Ejecutar la consulta SQL
        String sql1 = "SELECT CODIGO FROM T_RUBRICAS WHERE CODIGO = " + "'" + codigoRubrica + "'";
        ResultSet rs = stmt.executeQuery(sql1);

        if (rs.next()) {
            String sql2 = "DELETE FROM T_RUBRICAS WHERE CODIGO = '" + codigoRubrica + "';";
            int rowsAffected = stmt.executeUpdate(sql2);

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Rúbrica eliminada con éxito.");

            } else {
                JOptionPane.showMessageDialog(null, "ERROR. No se ha podido eliminar la rúbrica.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "ERROR. No existe la rúbrica.");
        }

        conn.close();
        session.disconnect();
    }

    public void createUser(String name, String passw, String type) throws JSchException, SQLException {
        // Valores para conexión a MV remota
        String sshHost = "10.6.130.204";
        String sshUser = "usuario";
        String sshPassword = "Usuario";
        int sshPort = 22; // Puerto SSH por defecto
        int localPort = 3307; // Puerto local para el túnel SSH
        String remoteHost = "localhost"; // La conexión MySQL se hará desde la máquina remota
        int remotePort = 3306; // Puerto MySQL en la máquina remota

        // Conexión SSH a la MV remota
        JSch jsch = new JSch();
        Session session = jsch.getSession(sshUser, sshHost, sshPort);
        session.setPassword(sshPassword);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();

        // Debugger
        System.out.println("Conexión con la máquina establecida");

        // Abrir un túnel SSH al puerto MySQL en la máquina remota
        session.setPortForwardingL(localPort, remoteHost, remotePort);

        // Conexión a MySQL a través del túnel SSH
        String dbUrl = "jdbc:mysql://localhost:" + localPort + "/OLYMPULL_DB";
        String dbUser = "root";
        String dbPassword = "root";
        Connection conn;
        conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

        // Debugger
        Statement stmt = conn.createStatement();

        // Ejecutar la consulta SQL
        String sql1 = "SELECT NOMBRE_USUARIO FROM T_USUARIOS WHERE NOMBRE_USUARIO = " + "'" + name + "'";
        ResultSet rs = stmt.executeQuery(sql1);

        if (rs.next()) {
            JOptionPane.showMessageDialog(null, "ERROR. Ya existe un usuario con este nombre de usuario.");
        } else {
            String sql2 = "INSERT INTO T_USUARIOS(NOMBRE_USUARIO, PASSWORD, TIPO_USUARIO) VALUES ('" + name + "', '" + passw + "', '" + type + "');";
            int rowsAffected = stmt.executeUpdate(sql2);

            if (rowsAffected > 0) {
                System.out.println("Se ha creado el usuario.");
            } else {
                System.out.println("No se ha podido crear el usuario.");
            }
            JOptionPane.showMessageDialog(null, "Usuario dado de alta con éxito.");
        }
        // Consulta para añadir el usuario a la base de datos


        conn.close();
        session.disconnect();
    }

    public void deleteUser(String name) throws JSchException, SQLException {
        // Valores para conexión a MV remota
        String sshHost = "10.6.130.204";
        String sshUser = "usuario";
        String sshPassword = "Usuario";
        int sshPort = 22; // Puerto SSH por defecto
        int localPort = 3307; // Puerto local para el túnel SSH
        String remoteHost = "localhost"; // La conexión MySQL se hará desde la máquina remota
        int remotePort = 3306; // Puerto MySQL en la máquina remota

        // Conexión SSH a la MV remota
        JSch jsch = new JSch();
        Session session = jsch.getSession(sshUser, sshHost, sshPort);
        session.setPassword(sshPassword);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();

        // Debugger
        System.out.println("Conexión con la máquina establecida");

        // Abrir un túnel SSH al puerto MySQL en la máquina remota
        session.setPortForwardingL(localPort, remoteHost, remotePort);

        // Conexión a MySQL a través del túnel SSH
        String dbUrl = "jdbc:mysql://localhost:" + localPort + "/OLYMPULL_DB";
        String dbUser = "root";
        String dbPassword = "root";
        Connection conn;
        conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

        // Debugger
        Statement stmt = conn.createStatement();

        // Ejecutar la consulta SQL
        String sql1 = "SELECT NOMBRE_USUARIO FROM T_USUARIOS WHERE NOMBRE_USUARIO = " + "'" + name + "'";
        ResultSet rs = stmt.executeQuery(sql1);

        if (rs.next()) {
            String sql2 = "DELETE FROM T_USUARIOS WHERE NOMBRE_USUARIO = '" + name + "';";
            int rowsAffected = stmt.executeUpdate(sql2);

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Usuario eliminado con éxito.");

            } else {
                JOptionPane.showMessageDialog(null, "ERROR. No se ha podido eliminar el usuario.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "ERROR. No existe el usuario.");
        }

        conn.close();
        session.disconnect();
    }

    public void assignExerciseToUser(String usuario, int codigo, String titulo) {
        // Valores para conexión a MV remota
        String sshHost = "10.6.130.204";
        String sshUser = "usuario";
        String sshPassword = "Usuario";
        int sshPort = 22; // Puerto SSH por defecto
        int localPort = 3307; // Puerto local para el túnel SSH
        String remoteHost = "localhost"; // La conexión MySQL se hará desde la máquina remota
        int remotePort = 3306; // Puerto MySQL en la máquina remota

        // Conexión SSH a la MV remota
        JSch jsch = new JSch();
        Session session = null;
        try {
            session = jsch.getSession(sshUser, sshHost, sshPort);
        } catch (JSchException ex) {
            throw new RuntimeException(ex);
        }
        session.setPassword(sshPassword);
        session.setConfig("StrictHostKeyChecking", "no");
        try {
            session.connect();
        } catch (JSchException ex) {
            throw new RuntimeException(ex);
        }

        // Debugger
        System.out.println("Conexión con la máquina establecida");

        // Abrir un túnel SSH al puerto MySQL en la máquina remota
        try {
            session.setPortForwardingL(localPort, remoteHost, remotePort);
        } catch (JSchException ex) {
            throw new RuntimeException(ex);
        }

        // Conexión a MySQL a través del túnel SSH
        String dbUrl = "jdbc:mysql://localhost:" + localPort + "/OLYMPULL_DB";
        String dbUser = "root";
        String dbPassword = "root";
        Connection conn;
        try {
            conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        // Ejecutar consulta para añadir nuevo ejercicio
        String sql = "INSERT INTO T_MONITORES VALUES('" + usuario + "', " + codigo + ", '" + titulo + "');";

        Statement stmt = null;
        try {
            stmt = conn.createStatement();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        int rowsAffected = 0;
        try {
            rowsAffected = stmt.executeUpdate(sql);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(null, "Se ha asignado el ejercicio.");
        } else {
            JOptionPane.showMessageDialog(null, "No se ha podido asignar el ejercicio.");
        }


        try {
            conn.close();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        session.disconnect();
    }
}
