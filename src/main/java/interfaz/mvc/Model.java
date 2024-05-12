package interfaz.mvc;

import interfaz.OperacionesBD;

import java.sql.ResultSet;

public class Model implements OperacionesBD {
    public Model() {

    }

    public ResultSet selectOlympiads() {
        return selectCol("T_OLIMPIADAS", "TITULO");
    }

    public ResultSet selectItineraries(String olymp) {
        String table = "T_ITINERARIOS JOIN T_OLIMPIADAS ON T_OLIMPIADAS.CODIGO=OLIMPIADA";
        String column = "T_ITINERARIOS.CODIGO, T_ITINERARIOS.TITULO";
        String where = "WHERE T_OLIMPIADAS.TITULO='" + olymp + "'";
        return selectCol(table, column, where);
    }

    public ResultSet selectExercises(String olymp, String it) {
        String table = "T_EJERCICIOS_OLIMPIADA_ITINERARIO JOIN T_EJERCICIOS ON EJERCICIO=CODIGO";
        String columns = "TITULO, CONCEPTO";
        String whereOlympiad = "OLIMPIADA=(SELECT CODIGO FROM T_OLIMPIADAS WHERE TITULO='" + olymp + "')";
        String whereItinerary = "ITINERARIO=(SELECT CODIGO FROM T_ITINERARIOS WHERE TITULO='" + it + "')";
        return selectCol(table, columns, "WHERE " + whereOlympiad + " AND " + whereItinerary);
    }

    public ResultSet selectTeams(String it, String punctColumn) {
        String where = "WHERE ITINERARIO='" + it + "'";
        return selectCol("T_EQUIPOS", "NOMBRE, " + punctColumn, where);
    }
}
