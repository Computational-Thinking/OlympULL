package users;

import gui.custom_components.option_panes.MessageJOptionPane;
import operations.DBOperations;

import java.util.ArrayList;

public class Organizer extends User {
    ArrayList<String> itinerarios;

    public Organizer(String name, String password, ArrayList<String> itinerarios) {
        this.userName = name;
        this.password = password;
        this.userType = "organizador";
        this.itinerarios = itinerarios;
    }

    public ArrayList<String> getItinerarios() {
        return itinerarios;
    }

    public int createAssignationExIt(String ex, String olymp, String it) {
        String table = "T_EJERCICIOS_OLIMPIADA_ITINERARIO";
        String data = "'" + ex + "', '" + olymp + "', '" + it + "'";

        if (insert(table, data) == 0) {
            new MessageJOptionPane("Se ha asignado el ejercicio");
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
            new MessageJOptionPane("Se ha modificado la asignación");
            return 0;
        } else {
            return 1;
        }
    }

    public int deleteAssignationEjIt(String ex, String olymp, String it) {
        String table = "T_EJERCICIOS_OLIMPIADA_ITINERARIO";
        String whereClause = "WHERE EJERCICIO='" + ex +  "' AND OLIMPIADA='" + olymp + "' AND ITINERARIO='" + it + "';";

        if (delete(table, whereClause) == 0) {
            new MessageJOptionPane("Se ha eliminado la asignación");
            return 0;
        } else {
            return 1;
        }
    }
}
