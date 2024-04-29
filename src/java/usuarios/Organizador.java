package usuarios;

import interfaz.CustomJOptionPane;
import interfaz.OperacionesBD;

import java.util.ArrayList;

public class Organizador extends Usuario implements OperacionesBD {
    ArrayList<String> itinerarios;

    public Organizador(String name, String password, ArrayList<String> itinerarios) {
        this.userName = name;
        this.password = password;
        this.itinerarios = itinerarios;
    }

    public ArrayList<String> getItinerarios() {
        return itinerarios;
    }

    public int createAssignationExIt(String ex, String olymp, String it) {
        String table = "T_EJERCICIOS_OLIMPIADA_EJERCICIO";
        String data = "'" + ex + "', '" + olymp + "', '" + it + "'";

        if (insert(table, data) == 0) {
            new CustomJOptionPane("Se ha asignado el ejercicio");
            return 0;
        } else {
            return 1;
        }
    }

    public int modifyAssignationExIt(String oldEx, String oldOlymp, String oldIt, String ex, String olymp, String it) {
        String table = "T_EJERCICIOS_OLIMPIADA_ITINERARIO";
        String setClause = "SET EJERCICIO='" + ex + "', OLIMPIADA='" + olymp + "', ITINERARIO='" + it + "'";
        String whereClause = "WHERE EJERCICIO='" + oldEx + "' AND OLIMPIADA='" + oldOlymp + "' AND ITINERARIO='" + oldIt + "'";

        if (update(table, setClause, whereClause) == 0) {
            new CustomJOptionPane("Se ha modificado la asignación");
            return 0;
        } else {
            return 1;
        }
    }

    public int deleteAssignationEjIt(String ex, String olymp, String it) {
        String table = "T_EJERCICIOS_OLIMPIADA_ITINERARIO";
        String whereClause = "WHERE EJERCICIO='" + ex +  "' AND OLIMPIADA='" + olymp + "' AND ITINERARIO='" + it + "';";

        if (delete(table, whereClause) == 0) {
            new CustomJOptionPane("Se ha eliminado la asignación");
            return 0;
        } else {
            return 1;
        }
    }
}
