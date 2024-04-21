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

    public int modifyOlympiad(String oldCode, String code, String title, String desc, int year) throws JSchException, SQLException {
        String table = "T_OLIMPIADAS";
        String setClause = "SET CODIGO='" + code + "', TITULO='" + title + "', DESCRIPCION='" + desc + "', YEAR=" + Integer.toString(year);
        String whereClause = "WHERE CODIGO='" + oldCode + "';";
        String safeUpdate = "WHERE CODIGO='" + code + "';";

        return update(table, setClause, whereClause, safeUpdate);
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

    public int modifyItinerario(String oldCode, String code, String title, String desc, String olymp) throws JSchException, SQLException {
        String table = "T_ITINERARIOS";
        String setClause = "SET CODIGO='" + code + "', TITULO='" + title + "', DESCRIPCION='" + desc + "', OLIMPIADA='" + olymp + "'";
        String whereClause = "WHERE CODIGO='" + oldCode + "';";
        String safeUpdate = "WHERE CODIGO='" + code + "';";

        return update(table, setClause, whereClause, safeUpdate);
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

    public int modifyExercise(String oldCode, String code, String title, String desc, String concepto, String recurso, String tipo, String rubrica) throws JSchException, SQLException {
        String table = "T_EJERCICIOS";
        String setClause = "SET CODIGO='" + code + "', TITULO='" + title + "', DESCRIPCION='" + desc +
                           "', CONCEPTO='" + concepto + "', RECURSOS='" + recurso + "', TIPO='" + tipo +
                           "', RUBRICA='" + rubrica + "'";
        String whereClause = "WHERE CODIGO='" + oldCode + "';";
        String safeUpdate = "WHERE CODIGO='" + code + "';";

        return update(table, setClause, whereClause, safeUpdate);
    }

    public int deleteEjercicio(String codigoEjercicio) throws JSchException, SQLException {
        String table = "T_EJERCICIOS";
        String whereClause = "WHERE CODIGO='" + codigoEjercicio + "';";

        return delete(table, whereClause);
    }

    public int createRubric(String code, String title, String desc, String values, String tags) throws JSchException, SQLException {
        String table = "T_RUBRICAS";
        String data = "'" + code + "', '" + title + "', '" + desc + "', '" + values + "', '" + tags + "'";
        String safeInsertClause = "WHERE CODIGO='" + code + "';";

        if (insert(table, data, safeInsertClause) == 0) {
            new CustomJOptionPane("Se ha creado la rúbrica");
            return 0;
        } else {
            return 1;
        }
    }

    public int modifyRubric(String oldCode, String code, String title, String desc, String values, String tags) throws JSchException, SQLException {
        String table = "T_RUBRICAS";
        String setClause = "SET CODIGO='" + code + "', NOMBRE='" + title + "', DESCRIPCION='" + desc +
                "', PUNTOS_RUBRICA='" + values + "', ETIQUETAS_RUBRICA='" + tags + "'";
        String whereClause = "WHERE CODIGO='" + oldCode + "';";
        String safeUpdate = "WHERE CODIGO='" + code + "';";

        return update(table, setClause, whereClause, safeUpdate);
    }

    public int deleteRubric(String codigoRubrica) throws JSchException, SQLException {
        String table = "T_RUBRICAS";
        String whereClause = "WHERE CODIGO='" + codigoRubrica + "';";

        return delete(table, whereClause);
    }

    public int createTeam(String code, String name, String school, String itinerary) throws JSchException, SQLException {
        String table = "T_EQUIPOS";
        String data = "'" + code + "', '" + name + "', '" + school + "', '" + itinerary + "'";
        String selection = "(CODIGO, NOMBRE, CENTRO_EDUCATIVO, ITINERARIO)";
        String safeInsertClause = "WHERE CODIGO='" + code + "' OR ((NOMBRE='" + name + "') AND (ITINERARIO='" + itinerary + "'));";

        if (insertSelectedCols(table, data, selection, safeInsertClause) == 0) {
            new CustomJOptionPane("Se ha creado el equipo");
            return 0;
        } else {
            return 1;
        }
    }

    public int modifyTeam(String oldCode, String code, String name, String school, String itinerario) throws JSchException, SQLException {
        String table = "T_EQUIPOS";
        String setClause = "SET CODIGO='" + code + "', NOMBRE='" + name + "', CENTRO_EDUCATIVO='" + school +
                "', ITINERARIO='" + itinerario + "'";
        String whereClause = "WHERE CODIGO='" + oldCode + "';";
        String safeUpdate = "WHERE CODIGO='" + code + "';";

        return update(table, setClause, whereClause, safeUpdate);
    }

    public int deleteTeam(String codigo) throws JSchException, SQLException {
        String table = "T_EQUIPOS";
        String whereClause = "WHERE CODIGO='" + codigo + "';";

        return delete(table, whereClause);
    }

    public int assignExerciseToOlympiad(String exercise, String olympiad, String itinerario) throws JSchException, SQLException {
        String table = "T_EJERCICIOS_OLIMPIADA_ITINERARIO";
        String data = "'" + exercise + "', '" + olympiad + "', '" + itinerario + "'";
        String safeInsertClause = "WHERE (EJERCICIO='" + exercise + "' AND OLIMPIADA='" + olympiad +
                "' AND ITINERARIO='" + itinerario + "') OR (EJERCICIO='" + exercise + "' AND OLIMPIADA='" +
                olympiad + "');";

        System.out.println(safeInsertClause);
        if (insert(table, data, safeInsertClause) == 0) {
            new CustomJOptionPane("Se ha asignado el ejercicio");
            return 0;

        } else {
            return 1;

        }
    }

    public int modifyAssignationExOlymp(String oldEx, String oldOlymp, String oldIt, String ex, String olymp, String it) throws JSchException, SQLException {
        String table = "T_EJERCICIOS_OLIMPIADA_ITINERARIO";
        String setClause = "SET EJERCICIO='" + ex + "', OLIMPIADA='" + olymp + "', ITINERARIO='" + it + "'";
        String whereClause = "WHERE EJERCICIO='" + oldEx + "' AND OLIMPIADA='" + oldOlymp + "' AND ITINERARIO='" + oldIt + "'";
        String safeUpdateClause = "WHERE (EJERCICIO='" + ex + "' AND OLIMPIADA='" + olymp +
                "' AND ITINERARIO='" + it + "') OR (EJERCICIO='" + ex + "' AND OLIMPIADA='" +
                olymp + "');";

        return update(table, setClause, whereClause, safeUpdateClause);
    }

    public int deleteAssignationEjOlimp(String ejercicio, String olimpiada) throws JSchException, SQLException {
        String table = "T_EJERCICIOS_OLIMPIADA_ITINERARIO";
        String whereClause = "WHERE EJERCICIO='" + ejercicio +  "' AND OLIMPIADA='" + olimpiada + "';";

        return delete(table, whereClause);
    }

    public int createUser(String name, String passw, String type) throws JSchException, SQLException {
        String table = "T_USUARIOS";
        String data = "'" + name + "', '" + passw + "', '" + type + "'";
        String safeInsertClause = "WHERE NOMBRE='" + name + "';";

        return insert(table, data, safeInsertClause);
    }

    public int modifyUser(String oldName, String name, String password, String type) throws JSchException, SQLException {
        String table = "T_USUARIOS";
        String setClause = "SET NOMBRE='" + name + "', PASSWORD='" + password + "', TIPO='" + type + "'";
        String whereClause = "WHERE NOMBRE='" + oldName + "';";
        String safeUpdate = "WHERE NOMBRE='" + oldName + "';";

        return update(table, setClause, whereClause, safeUpdate);
    }

    public int deleteUser(String name) throws JSchException, SQLException {
        String table = "T_USUARIOS";
        String whereClause = "WHERE NOMBRE='" + name + "';";

        return delete(table, whereClause);
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
