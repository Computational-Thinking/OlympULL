package usuarios;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import interfaz.CustomJOptionPane;
import interfaz.OperacionesBD;

import javax.swing.*;
import java.sql.*;

public class Administrador extends Usuario implements OperacionesBD {
    public Administrador(String nombre, String password) {
        this.userName = nombre;
        this.password = password;
        this.userType = "admin";
    }

    // Operaciones con olimpiadas
    public int createOlympiad(String code, String title, String desc, int year) throws JSchException, SQLException {
        String table = "T_OLIMPIADAS";
        String data = "'" + code + "', '" + title + "', '" + desc + "', " + Integer.toString(year);
        String safeInsertClause = "WHERE CODIGO='" + code + "';";

        if (insert(table, data, safeInsertClause) == 0) {
            new CustomJOptionPane("Se ha creado la olimpiada");
            return 0;
        } else {
            return 1;
        }
    }

    public void modifyOlympiad(String oldCode, String code, String title, String desc, int year) throws JSchException, SQLException {
        String table = "T_OLIMPIADAS";
        String setClause = "SET CODIGO='" + code + "', TITULO='" + title + "', DESCRIPCION='" + desc + "', YEAR=" + Integer.toString(year);
        String whereClause = "WHERE CODIGO='" + oldCode + "';";

        update(table, setClause, whereClause);
        new CustomJOptionPane("Se ha modificado la olimpiada");
    }

    public int deleteOlympiad(String codigoOlimpiada) throws JSchException, SQLException {
        String table = "T_OLIMPIADAS";
        String whereClause = "WHERE CODIGO='" + codigoOlimpiada + "';";

        return delete(table, whereClause);
    }

    // Operaciones itinerario
    public int createItinerario(String code, String title, String desc, String olymp) throws JSchException, SQLException {
        String table = "T_ITINERARIOS";
        String data = "'" + code + "', '" + title + "', '" + desc + "', '" + olymp + "'";
        String safeInsertClause = "WHERE CODIGO='" + code + "';";

        if (insert(table, data, safeInsertClause) == 0) {
            new CustomJOptionPane("Se ha creado el itinerario");
            return 0;
        } else {
            return 1;
        }
    }

    public void modifyItinerario(String oldCode, String code, String title, String desc, String olymp) throws JSchException, SQLException {
        String table = "T_ITINERARIOS";
        String setClause = "SET CODIGO='" + code + "', TITULO='" + title + "', DESCRIPCION='" + desc + "', OLIMPIADA='" + olymp + "'";
        String whereClause = "WHERE CODIGO='" + oldCode + "';";

        update(table, setClause, whereClause);
        new CustomJOptionPane("Se ha modificado el itinerario");
    }

    public int deleteItinerario(String codigoItinerario) throws JSchException, SQLException {
        String table = "T_ITINERARIOS";
        String whereClause = "WHERE CODIGO='" + codigoItinerario + "';";

        return delete(table, whereClause);
    }

    public int createExercise(String code, String title, String desc, String concept, String resources, String type, String rubrica) throws JSchException, SQLException {
        String table = "T_EJERCICIOS";
        String data = "'" + code + "', '" + title + "', '" + desc + "', '" + concept + "', '" + resources + "', '" + type + "', '" + rubrica + "'";
        String safeInsertClause = "WHERE CODIGO='" + code + "';";

        if (insert(table, data, safeInsertClause) == 0) {
            new CustomJOptionPane("Se ha creado el ejercicio");
            return 0;
        } else {
            return 1;
        }
    }

    public void modifyExercise(String oldCode, String code, String title, String desc, String concepto, String recurso, String tipo, String rubrica) throws JSchException, SQLException {
        String table = "T_EJERCICIOS";
        String setClause = "SET CODIGO='" + code + "', TITULO='" + title + "', DESCRIPCION='" + desc +
                           "', CONCEPTO='" + concepto + "', RECURSOS='" + recurso + "', TIPO='" + tipo +
                           "', RUBRICA='" + rubrica + "'";
        String whereClause = "WHERE CODIGO='" + oldCode + "';";

        update(table, setClause, whereClause);
        new CustomJOptionPane("Se ha modificado el ejercicio");
    }

    public int deleteEjercicio(String codigoEjercicio) throws JSchException, SQLException {
        String table = "T_EJERCICIOS";
        String whereClause = "WHERE CODIGO='" + codigoEjercicio + "';";

        return delete(table, whereClause);
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

    public void createTeam(String code, String name, String school, String itinerario) throws JSchException, SQLException {
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

        /**
        // Ejecutar la consulta SQL
        String sql1 = "SELECT NOMBRE FROM T_USUARIOS WHERE NOMBRE = " + "'" + name + "'";
        ResultSet rs = stmt.executeQuery(sql1);
         */

        String sql2 = "INSERT INTO T_EQUIPOS(CODIGO, NOMBRE, CENTRO_EDUCATIVO, ITINERARIO) VALUES ('" + code + "', '" + name + "', '" + school + "', '" + itinerario + "');";
        int rowsAffected = stmt.executeUpdate(sql2);

        if (rowsAffected > 0) {
            System.out.println("Se ha creado el equipo.");
        } else {
            System.out.println("No se ha podido crear el equipo.");
        }
        JOptionPane.showMessageDialog(null, "Equipo creado");

        // Consulta para añadir el usuario a la base de datos


        conn.close();
        session.disconnect();
    }

    public void modifyTeam(String oldCode, String code, String name, String school, String itinerario) throws JSchException, SQLException {
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
        String sql2 = "UPDATE T_EQUIPOS " +
                "SET CODIGO='" + code + "', " +
                "NOMBRE='" + name + "', " +
                "CENTRO_EDUCATIVO='" + school + "', " +
                "ITINERARIO='" + itinerario + "' WHERE CODIGO='" + oldCode + "';";
        int rowsAffected = stmt.executeUpdate(sql2);

        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(null, "Equipo modificado con éxito.");

        } else {
            JOptionPane.showMessageDialog(null, "ERROR. No se ha podido modificar el equipo.");
        }

        conn.close();
        session.disconnect();
    }

    public void deleteTeam(String codigo) throws JSchException, SQLException {
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
        String sql1 = "SELECT CODIGO FROM T_EQUIPOS WHERE CODIGO = " + "'" + codigo + "'";
        ResultSet rs = stmt.executeQuery(sql1);

        if (rs.next()) {
            String sql2 = "DELETE FROM T_EQUIPOS WHERE CODIGO = '" + codigo + "';";
            int rowsAffected = stmt.executeUpdate(sql2);

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Equipo eliminado con éxito.");

            } else {
                JOptionPane.showMessageDialog(null, "ERROR. No se ha podido eliminar el equipo.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "ERROR. No existe el equipo.");
        }

        conn.close();
        session.disconnect();
    }

    public void assignExerciseToOlympiad(String exercise, String olympiad, String itinerario) {
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
        String sql = "INSERT INTO T_EJERCICIOS_OLIMPIADA_ITINERARIO VALUES('" + exercise + "', '" + olympiad + "', '" + itinerario + "');";

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

    public void modifyAssignationExOlymp(String oldEx, String oldOlymp, String oldIt, String exercise, String olymp, String it) throws JSchException, SQLException {
        assignExerciseToOlympiad(exercise, olymp, it);
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
        String sql1 = "SELECT EJERCICIO FROM T_EJERCICIOS_OLIMPIADA_ITINERARIO WHERE EJERCICIO = " + "'" + oldEx + "'";
        ResultSet rs = stmt.executeQuery(sql1);

        if (rs.next()) {
            String sql2 = "DELETE FROM T_EJERCICIOS_OLIMPIADA_ITINERARIO WHERE EJERCICIO = '" + oldEx + "' AND OLIMPIADA='" + oldOlymp + "' AND ITINERARIO='" + oldIt + "';";
            int rowsAffected = stmt.executeUpdate(sql2);

            if (!(rowsAffected > 0)) {
                JOptionPane.showMessageDialog(null, "ERROR. No se ha podido eliminar la asignación.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "ERROR. No existe la fila.");
        }

        conn.close();
        session.disconnect();
    }

    public void deleteAssignationEjOlimp(String codigo) throws JSchException, SQLException {
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
        String sql1 = "SELECT EJERCICIO FROM T_EJERCICIOS_OLIMPIADA_ITINERARIO WHERE EJERCICIO = " + "'" + codigo + "'";
        ResultSet rs = stmt.executeQuery(sql1);

        if (rs.next()) {
            String sql2 = "DELETE FROM T_EJERCICIOS_OLIMPIADA_ITINERARIO WHERE EJERCICIO = '" + codigo + "';";
            int rowsAffected = stmt.executeUpdate(sql2);

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Asignación eliminada con éxito.");

            } else {
                JOptionPane.showMessageDialog(null, "ERROR. No se ha podido eliminar la asignación.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "ERROR. No existe la fila.");
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
        String sql1 = "SELECT NOMBRE FROM T_USUARIOS WHERE NOMBRE = " + "'" + name + "'";
        ResultSet rs = stmt.executeQuery(sql1);

        if (rs.next()) {
            JOptionPane.showMessageDialog(null, "ERROR. Ya existe un usuario con este nombre de usuario.");
        } else {
            String sql2 = "INSERT INTO T_USUARIOS(NOMBRE, PASSWORD, TIPO) VALUES ('" + name + "', '" + passw + "', '" + type + "');";
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

    public void modifyUser(String oldName, String name, String password, String type) throws JSchException, SQLException {
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
        String sql1 = "SELECT * FROM T_USUARIOS WHERE NOMBRE= " + "'" + oldName + "'";
        ResultSet rs = stmt.executeQuery(sql1);

        if (rs.next()) {
            String sql2 = "UPDATE T_USUARIOS " +
                    "SET NOMBRE='" + name + "', " +
                    "PASSWORD='" + password + "', " +
                    "TIPO='" + type + "' WHERE NOMBRE='" + oldName + "';";
            int rowsAffected = stmt.executeUpdate(sql2);

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Usuario modificado con éxito.");

            } else {
                JOptionPane.showMessageDialog(null, "ERROR. No se ha podido modificar el usuario.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "ERROR. No existe el usuario.");
        }

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
        String sql1 = "SELECT NOMBRE FROM T_USUARIOS WHERE NOMBRE = " + "'" + name + "'";
        ResultSet rs = stmt.executeQuery(sql1);

        if (rs.next()) {
            String sql2 = "DELETE FROM T_USUARIOS WHERE NOMBRE = '" + name + "';";
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
