package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SingletonSQLite {

    private static Connection conn = null;

    private final String driver = "org.sqlite.JDBC";
    private final String dsn = "jdbc:sqlite:datos.sqlite";

    // Constructor privado
    private SingletonSQLite() {
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(dsn);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    // Método estático que devuelve la conexión
    public static Connection getConnection() {
        if (conn == null) {
            new SingletonSQLite();
        }
        return conn;
    }
}