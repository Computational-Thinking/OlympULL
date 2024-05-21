package users;

import java.util.ArrayList;

public class Monitor extends User {
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
