package dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SingletonConexion {
    private static Connection connection;

    private SingletonConexion() {
        // Carga la configuración desde el archivo properties
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream("Conexion_BD.properties")) {
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al cargar el archivo de propiedades.");
        }

        // Inicializa la conexión a la base de datos
        String name = properties.getProperty("db.name");
        String url = properties.getProperty("db.url");
        String driver = properties.getProperty("db.driver");
        String user = properties.getProperty("db.user", "");
        String password = properties.getProperty("db.password", "");

        try {
            Class.forName(driver);
            // Verifica si el usuario y contraseña están vacíos para SQLite
            if (user != null && !user.isEmpty() && password != null && !password.isEmpty()) {
                connection = DriverManager.getConnection(url, user, password);
            } else {
                connection = DriverManager.getConnection(url);
            }
            
            System.out.println("Conexion Exitosa a: " + name);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al establecer la conexión a la base de datos.");
        }
    }

 // Método estático que devuelve la conexión
    public static Connection getConnection() {
        if (connection == null) {
            new SingletonConexion();
        }
        return connection;
    }
}
