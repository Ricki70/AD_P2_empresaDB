package dao;

import java.sql.Connection;

public class GestionEmpleados {
	private Connection conn; // Utiliza la conexión Singleton

    public GestionEmpleados() {
        // Constructor para inicializar la base de datos si es necesario
        initDatabase();
    }

    private void initDatabase() {
        conn = SingletonConexion.getConnection();
        System.out.println(conn);
    }
}
