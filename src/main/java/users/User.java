package users;

import gui.custom_components.option_panes.ErrorJOptionPane;
import gui.custom_components.option_panes.MessageJOptionPane;
import operations.DBOperations;

public abstract class User implements DBOperations {
    String userName;
    String password;
    String userType;

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getUserType() {
        return userType;
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;

        String table = "T_USUARIOS";
        String setClause = "SET PASSWORD='" + newPassword + "'";
        String whereClause = "WHERE NOMBRE='" + this.userName + "'";

        if (update(table, setClause, whereClause) == 0) {
            new MessageJOptionPane("Se ha cambiado la contraseña");
        } else {
            new MessageJOptionPane("No se ha podido cambiar la contraseña");
        }
    }

    public int createRegister(String table, String data) {
        if (insert(table, data) == 0) {
            new MessageJOptionPane("Se ha creado el registro");
            return 0;

        } else {
            return 1;

        }
    }

    public int createRegister(String table, String data, String columns) {
        if (insert(table, data, columns) == 0) {
            new MessageJOptionPane("Se ha creado el registro");
            return 0;
        } else {
            return 1;

        }
    }

    public int modifyRegister(String table, String data, String where) {
        if (update(table, data, where) == 0) {
            new MessageJOptionPane("Se ha modificado el registro");
            return 0;
        } else {
            new ErrorJOptionPane("No se ha podido modificar el registro");
            return 1;
        }
    }

    public int modifyRegister(String table, String data, String where, String customMessage) {
        if (update(table, data, where) == 0) {
            new MessageJOptionPane(customMessage);
            return 0;
        } else {
            new ErrorJOptionPane("No se ha podido modificar el registro");
            return 1;
        }
    }

    public int deleteRegister(String table, String where) {
        if (delete(table, where) == 0) {
            new MessageJOptionPane("Se ha eliminado el registro");
            return 0;

        } else {
            return 1;
        }
    }
}
