package usuarios;

import interfaz.custom_components.MessageJOptionPane;
import interfaz.OperacionesBD;

public abstract class Usuario implements OperacionesBD {
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
