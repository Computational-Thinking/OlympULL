package users;

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
}
