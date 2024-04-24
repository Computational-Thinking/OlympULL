import interfaz.CustomJOptionPane;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class ScriptRunner {
    public static void main(String[] args) {
        try {
            // Establecer conexión a la base de datos
            Connection connection = DriverManager.getConnection("jdbc:mysql://host_mysql:3306/nombre_base_de_datos", "usuario_mysql", "contraseña_mysql");

            // Ejecutar script SQL
            Statement statement = connection.createStatement();
            String sqlScript = "CREATE TABLE IF NOT EXISTS usuarios (id INT AUTO_INCREMENT PRIMARY KEY, nombre VARCHAR(50), edad INT);";
            statement.executeUpdate(sqlScript);

            // Repite este proceso para cada sentencia CREATE TABLE en tu script

            // Cerrar la conexión
            connection.close();

            new CustomJOptionPane("Se ha creado la base de datos.");
        } catch (Exception e) {
            new CustomJOptionPane("ERROR - " + e.getMessage() + ".");
        }
    }
}

