package usuarios;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import interfaz.CustomJOptionPane;
import interfaz.OperacionesBD;

import javax.swing.*;
import java.sql.*;

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
            new CustomJOptionPane("Se ha cambiado la contraseña");
        } else {
            new CustomJOptionPane("No se ha podido cambiar la contraseña");
        }
    }
}
