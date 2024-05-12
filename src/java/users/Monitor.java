package users;

import interfaz.custom_components.ErrorJOptionPane;
import interfaz.custom_components.MessageJOptionPane;
import interfaz.OperacionesBD;

import java.sql.*;
import java.util.ArrayList;

public class Monitor extends User implements OperacionesBD {
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

    public void puntuarEquipo(int puntuacion, String concepto, String equipo, String itinerario) throws SQLException {
        String columnaPuntuacion = null;

        switch(concepto) {
            case "ABSTRACCION" -> columnaPuntuacion = "P_ABSTRACCION";
            case "ALGORITMOS" -> columnaPuntuacion = "P_ALGORITMOS";
            case "BUCLES" -> columnaPuntuacion = "P_BUCLES";
            case "CONDICIONALES" -> columnaPuntuacion = "P_CONDICIONALES";
            case "DESCOMPOSICION" -> columnaPuntuacion = "P_DESCOMPOSICION";
            case "FUNCIONES" -> columnaPuntuacion = "P_FUNCIONES";
            case "IA" -> columnaPuntuacion = "P_IA";
            case "RECONOCIMIENTO DE PATRONES" -> columnaPuntuacion = "P_REC_PATRONES";
            case "SECUENCIAS" -> columnaPuntuacion = "P_SECUENCIAS";
            case "SECUENCIAS Y BUCLES" -> columnaPuntuacion = "P_SECUENCIAS_Y_BUCLES";
            case "VARIABLES" -> columnaPuntuacion = "P_VARIABLES";
            case "VARIABLES Y FUNCIONES" -> columnaPuntuacion = "P_VARIABLES_Y_FUNC";
            case "OTROS" -> columnaPuntuacion = "P_OTROS";
        }

        // Se comprueba que no se ha puntuado ya al equipo
        String wherePrueba = "WHERE NOMBRE='" + equipo + "' AND ITINERARIO='" + itinerario + "'";
        ResultSet prueba = selectCol("T_EQUIPOS", columnaPuntuacion, wherePrueba);

        if (prueba.next()) {
            if (prueba.getString(columnaPuntuacion) != null) {
                new ErrorJOptionPane("Este equipo ya ha recibido una puntuación para el ejercicio seleccionado");

            } else {
                String table = "T_EQUIPOS";
                String data = "SET " + columnaPuntuacion + "=" + puntuacion;
                String where = "WHERE NOMBRE='" + equipo + "' AND ITINERARIO='" + itinerario + "'";

                if (update(table, data, where) == 0) {
                    new MessageJOptionPane("Se ha puntuado al equipo");
                } else {
                    new MessageJOptionPane("No se ha podido puntuar al equipo");
                }
            }
        }
    }

    public void modificarPuntuacion(int puntuacion, String concepto, String equipo, String itinerario) throws SQLException {
        String columnaPuntuacion = null;

        switch(concepto) {
            case "ABSTRACCION" -> columnaPuntuacion = "P_ABSTRACCION";
            case "ALGORITMOS" -> columnaPuntuacion = "P_ALGORITMOS";
            case "BUCLES" -> columnaPuntuacion = "P_BUCLES";
            case "CONDICIONALES" -> columnaPuntuacion = "P_CONDICIONALES";
            case "DESCOMPOSICION" -> columnaPuntuacion = "P_DESCOMPOSICION";
            case "FUNCIONES" -> columnaPuntuacion = "P_FUNCIONES";
            case "IA" -> columnaPuntuacion = "P_IA";
            case "RECONOCIMIENTO DE PATRONES" -> columnaPuntuacion = "P_REC_PATRONES";
            case "SECUENCIAS" -> columnaPuntuacion = "P_SECUENCIAS";
            case "SECUENCIAS Y BUCLES" -> columnaPuntuacion = "P_SECUENCIAS_Y_BUCLES";
            case "VARIABLES" -> columnaPuntuacion = "P_VARIABLES";
            case "VARIABLES Y FUNCIONES" -> columnaPuntuacion = "P_VARIABLES_Y_FUNC";
            case "OTROS" -> columnaPuntuacion = "P_OTROS";
        }

        String table = "T_EQUIPOS";
        String data = "SET " + columnaPuntuacion + "=" + puntuacion;
        String where = "WHERE NOMBRE='" + equipo + "' AND ITINERARIO='" + itinerario + "'";

        if (update(table, data, where) == 0) {
            new MessageJOptionPane("Se ha modificado la puntuación del equipo");
        } else {
            new MessageJOptionPane("No se ha podido modificar la puntuación");
        }

    }

}
