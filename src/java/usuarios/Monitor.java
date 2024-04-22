package usuarios;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Monitor extends Usuario {
    ArrayList<String> exercises; // Código del ejercicio asociado a este monitor

    public Monitor(String nombreUsuario, String password, ArrayList<String> exercises) {
        this.userName = nombreUsuario;
        this.password = password;
        this.userType = "monitor";
        this.exercises = exercises;
    }

    public ArrayList<String> getExerciseCode() {
        return this.exercises;
    }

}
