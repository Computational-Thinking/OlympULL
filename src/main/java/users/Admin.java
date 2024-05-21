package users;

import gui.custom_components.option_panes.MessageJOptionPane;

import java.sql.*;

public class Admin extends User {
    public Admin(String nombre, String password) {
        this.userName = nombre;
        this.password = password;
        this.userType = "administrador";
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


    public int importData(String tableName, String tableTuple, String where) throws SQLException {
        if (!selectCol(tableName, tableTuple, where).next()) {
            return insert(tableName, tableTuple);
        }
        return 1;
    }

}
