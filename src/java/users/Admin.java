package users;

import com.jcraft.jsch.JSchException;
import interfaz.custom_components.MessageJOptionPane;
import interfaz.OperacionesBD;

import java.sql.*;

public class Admin extends User implements OperacionesBD {
    public Admin(String nombre, String password) {
        this.userName = nombre;
        this.password = password;
        this.userType = "administrador";
    }

    // Operaciones con olimpiadas
    public int createOlympiad(String code, String title, String desc, int year) {
        String table = "T_OLIMPIADAS";
        String data = "'" + code + "', '" + title + "', '" + desc + "', " + year;

        if (insert(table, data) == 0) {
            new MessageJOptionPane("Se ha creado la olimpiada");
            return 0;

        } else {
            return 1;

        }
    }

    public int modifyOlympiad(String oldCode, String code, String title, String desc, int year) throws JSchException, SQLException {
        String table = "T_OLIMPIADAS";
        String setClause = "SET CODIGO='" + code + "', TITULO='" + title + "', DESCRIPCION='" + desc + "', YEAR=" + year;
        String whereClause = "WHERE CODIGO='" + oldCode + "';";

        if (update(table, setClause, whereClause) == 0) {
            new MessageJOptionPane("Se ha modificado la olimpiada");
            return 0;

        } else {
            return 1;

        }
    }

    public int deleteOlympiad(String codigoOlimpiada) throws JSchException, SQLException {
        String table = "T_OLIMPIADAS";
        String whereClause = "WHERE CODIGO='" + codigoOlimpiada + "';";

        if (delete(table, whereClause) == 0) {
            new MessageJOptionPane("Se ha eliminado la olimpiada");
            return 0;

        } else {
            return 1;

        }
    }

    // Operaciones itinerario
    public int createItinerario(String code, String title, String desc, String olymp) throws JSchException, SQLException {
        String table = "T_ITINERARIOS";
        String data = "'" + code + "', '" + title + "', '" + desc + "', '" + olymp + "'";

        if (insert(table, data) == 0) {
            new MessageJOptionPane("Se ha creado el itinerario");
            return 0;
        } else {
            return 1;
        }
    }

    public int modifyItinerario(String oldCode, String code, String title, String desc, String olymp) throws JSchException, SQLException {
        String table = "T_ITINERARIOS";
        String setClause = "SET CODIGO='" + code + "', TITULO='" + title + "', DESCRIPCION='" + desc + "', OLIMPIADA='" + olymp + "'";
        String whereClause = "WHERE CODIGO='" + oldCode + "';";

        if (update(table, setClause, whereClause) == 0) {
            new MessageJOptionPane("Se ha modificado el itinerario");
            return 0;
        } else {
            return 1;
        }
    }

    public int deleteItinerario(String codigoItinerario) throws JSchException, SQLException {
        String table = "T_ITINERARIOS";
        String whereClause = "WHERE CODIGO='" + codigoItinerario + "';";

        if (delete(table, whereClause) == 0) {
            new MessageJOptionPane("Se ha eliminado el itinerario");
            return 0;

        } else {
            return 1;
        }
    }

    public int createExercise(String code, String title, String desc, String concept, String resources, String type, String rubrica) {
        String table = "T_EJERCICIOS";
        String data = "'" + code + "', '" + title + "', '" + desc + "', '" + concept + "', '" + resources + "', '" + type + "', '" + rubrica + "'";

        if (insert(table, data) == 0) {
            new MessageJOptionPane("Se ha creado el ejercicio");
            return 0;
        } else {
            return 1;
        }
    }

    public int modifyExercise(String oldCode, String code, String title, String desc, String concepto, String recurso, String tipo, String rubrica) {
        String table = "T_EJERCICIOS";
        String setClause = "SET CODIGO='" + code + "', TITULO='" + title + "', DESCRIPCION='" + desc +
                           "', CONCEPTO='" + concepto + "', RECURSOS='" + recurso + "', TIPO='" + tipo +
                           "', RUBRICA='" + rubrica + "'";
        String whereClause = "WHERE CODIGO='" + oldCode + "';";

        if (update(table, setClause, whereClause) == 0) {
            new MessageJOptionPane("Se ha modificado el ejercicio");
            return 0;
        } else {
            return 1;
        }
    }

    public int deleteEjercicio(String codigoEjercicio) {
        String table = "T_EJERCICIOS";
        String whereClause = "WHERE CODIGO='" + codigoEjercicio + "';";

        if (delete(table, whereClause) == 0) {
            new MessageJOptionPane("Se ha eliminado el ejercicio");
            return 0;
        } else {
            return 1;
        }
    }

    public int createRubric(String code, String title, String desc, String values, String tags) {
        String table = "T_RUBRICAS";
        String data = "'" + code + "', '" + title + "', '" + desc + "', '" + values + "', '" + tags + "'";

        if (insert(table, data) == 0) {
            new MessageJOptionPane("Se ha creado la rúbrica");
            return 0;
        } else {
            return 1;
        }
    }

    public int modifyRubric(String oldCode, String code, String title, String desc, String values, String tags) {
        String table = "T_RUBRICAS";
        String setClause = "SET CODIGO='" + code + "', NOMBRE='" + title + "', DESCRIPCION='" + desc +
                "', PUNTOS_RUBRICA='" + values + "', ETIQUETAS_RUBRICA='" + tags + "'";
        String whereClause = "WHERE CODIGO='" + oldCode + "';";

        if (update(table, setClause, whereClause) == 0) {
            new MessageJOptionPane("Se ha modificado la rúbrica");
            return 0;
        } else {
            return 1;
        }
    }

    public int deleteRubric(String codigoRubrica) {
        String table = "T_RUBRICAS";
        String whereClause = "WHERE CODIGO='" + codigoRubrica + "';";

        if (delete(table, whereClause) == 0) {
            new MessageJOptionPane("Se ha eliminado la rúbrica");
            return 0;
        } else {
            return 1;
        }
    }

    public int createTeam(String code, String name, String school, String itinerary) {
        String table = "T_EQUIPOS";
        String data = "'" + code + "', '" + name + "', '" + school + "', '" + itinerary + "'";
        String selection = "(CODIGO, NOMBRE, CENTRO_EDUCATIVO, ITINERARIO)";

        if (insert(table, data, selection) == 0) {
            new MessageJOptionPane("Se ha creado el equipo");
            return 0;
        } else {
            return 1;
        }
    }

    public int modifyTeam(String oldCode, String code, String name, String school, String itinerario) {
        String table = "T_EQUIPOS";
        String setClause = "SET CODIGO='" + code + "', NOMBRE='" + name + "', CENTRO_EDUCATIVO='" + school +
                "', ITINERARIO='" + itinerario + "'";
        String whereClause = "WHERE CODIGO='" + oldCode + "';";

        if (update(table, setClause, whereClause) == 0) {
            new MessageJOptionPane("Se ha modificado el equipo");
            return 0;
        } else {
            return 1;
        }
    }

    public int deleteTeam(String codigo) {
        String table = "T_EQUIPOS";
        String whereClause = "WHERE CODIGO='" + codigo + "';";

        if (delete(table, whereClause) == 0) {
            new MessageJOptionPane("Se ha eliminado el equipo");
            return 0;
        } else {
            return 1;
        }
    }

    public int assignExerciseToOlympiad(String exercise, String olympiad, String itinerario) {
        String table = "T_EJERCICIOS_OLIMPIADA_ITINERARIO";
        String data = "'" + exercise + "', '" + olympiad + "', '" + itinerario + "'";

        if (insert(table, data) == 0) {
            new MessageJOptionPane("Se ha asignado el ejercicio");
            return 0;

        } else {
            return 1;

        }
    }

    public int modifyAssignationExOlymp(String oldEx, String oldOlymp, String oldIt, String ex, String olymp, String it) {
        String table = "T_EJERCICIOS_OLIMPIADA_ITINERARIO";
        String setClause = "SET EJERCICIO='" + ex + "', OLIMPIADA='" + olymp + "', ITINERARIO='" + it + "'";
        String whereClause = "WHERE EJERCICIO='" + oldEx + "' AND OLIMPIADA='" + oldOlymp + "' AND ITINERARIO='" + oldIt + "'";

        if (update(table, setClause, whereClause) == 0) {
            new MessageJOptionPane("Se ha modificado la asignación");
            return 0;
        } else {
            return 1;
        }
    }

    public int deleteAssignationEjOlimp(String ejercicio, String olimpiada) {
        String table = "T_EJERCICIOS_OLIMPIADA_ITINERARIO";
        String whereClause = "WHERE EJERCICIO='" + ejercicio +  "' AND OLIMPIADA='" + olimpiada + "';";

        if (delete(table, whereClause) == 0) {
            new MessageJOptionPane("Se ha eliminado la asignación");
            return 0;
        } else {
            return 1;
        }
    }

    public int createUser(String name, String passw, String type) {
        String table = "T_USUARIOS";
        String data = "'" + name + "', '" + passw + "', '" + type + "'";

        if (insert(table, data) == 0) {
            new MessageJOptionPane("Se ha creado el usuario");
            return 0;
        } else {
            return 1;
        }
    }

    public int modifyUser(String oldName, String name, String password, String type) {
        String table = "T_USUARIOS";
        String setClause = "SET NOMBRE='" + name + "', PASSWORD='" + password + "', TIPO='" + type + "'";
        String whereClause = "WHERE NOMBRE='" + oldName + "';";

        if (update(table, setClause, whereClause) == 0) {
            new MessageJOptionPane("Se ha modificado el usuario");
            return 0;
        } else {
            return 1;
        }
    }

    public int deleteUser(String name) {
        String table = "T_USUARIOS";
        String whereClause = "WHERE NOMBRE='" + name + "';";

        if (delete(table, whereClause) == 0) {
            new MessageJOptionPane("Se ha eliminado el usuario");
            return 0;
        } else {
            return 1;
        }
    }

    public int assignExerciseToUser(String monitor, String exerCode, String olympCode, String itineraryCode) {
        String table = "T_MONITORES";
        String data = "'" + monitor + "', '" + exerCode + "', '" + olympCode + "', '" + itineraryCode + "'";

        if (insert(table, data) == 0) {
            new MessageJOptionPane("Se ha asignado el ejercicio");
            return 0;
        } else {
            return 1;
        }
    }

    public int modifyAssignationExUser(String oldMoni, String oldEx, String oldOlymp, String monitor, String ex, String olymp, String it) {
        String table = "T_MONITORES";
        String setClause = "SET NOMBRE='" + monitor + "', EJERCICIO='" + ex + "', OLIMPIADA='" + olymp + "', ITINERARIO='" + it + "'";
        String whereClause = "WHERE NOMBRE='" + oldMoni + "' AND EJERCICIO='" + oldEx + "' AND OLIMPIADA='" + oldOlymp + "'";

        if (update(table, setClause, whereClause) == 0) {
            new MessageJOptionPane("Se ha modificado la asignación");
            return 0;
        } else {
            return 1;
        }
    }

    public int deleteAssignationExUser(String name, String ex, String olymp) {
        String table = "T_MONITORES";
        String whereClause = "WHERE NOMBRE='" + name +  "' AND EJERCICIO='" + ex + "' AND OLIMPIADA='"  + olymp + "';";

        if (delete(table, whereClause) == 0) {
            new MessageJOptionPane("Se ha eliminado la asignación");
            return 0;
        } else {
            return 1;
        }
    }

    public int assignItineraryToOrganiser(String org, String itinerary) {
        String table = "T_ORGANIZADORES";
        String data = "'" + org + "', '" + itinerary + "'";

        if (insert(table, data) == 0) {
            new MessageJOptionPane("Se ha asignado el itinerario");
            return 0;
        } else {
            return 1;
        }
    }

    public int modifyAssignationItOrg(String oldOrg, String oldIt, String org, String it) {
        String table = "T_ORGANIZADORES";
        String setClause = "SET ORGANIZADOR='" + org + "', ITINERARIO='" + it + "'";
        String whereClause = "WHERE ORGANIZADOR='" + oldOrg + "' AND ITINERARIO='" + oldIt + "'";

        if (update(table, setClause, whereClause) == 0) {
            new MessageJOptionPane("Se ha modificado la asignación");
            return 0;
        } else {
            return 1;
        }
    }

    public int deleteAssignationItOrg(String org, String it) {
        String table = "T_ORGANIZADORES";
        String whereClause = "WHERE ORGANIZADOR='" + org +  "' AND ITINERARIO='" + it + "';";

        if (delete(table, whereClause) == 0) {
            new MessageJOptionPane("Se ha eliminado la asignación");
            return 0;
        } else {
            return 1;
        }
    }
}
