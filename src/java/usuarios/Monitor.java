package usuarios;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Monitor extends Usuario {
    public Monitor(String nombreUsuario, String password) {
        this.userName = nombreUsuario;
        this.password = password;
        this.userType = "monitor";
    }


}
