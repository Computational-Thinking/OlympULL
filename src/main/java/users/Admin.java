package users;

import gui.custom_components.option_panes.MessageJOptionPane;

import java.sql.*;

public class Admin extends User {
    public Admin(String nombre, String password) {
        this.userName = nombre;
        this.password = password;
        this.userType = "administrador";
    }

    public int importData(String tableName, String tableTuple, String where) throws SQLException {
        if (!selectCol(tableName, tableTuple, where).next()) {
            return insert(tableName, tableTuple);
        }
        return 1;
    }

}
