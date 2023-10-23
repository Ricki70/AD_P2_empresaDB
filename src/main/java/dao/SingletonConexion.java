package dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SingletonConexion {
	/**
	 * Variable estática de tipo Connection que será nuestra conexión a la base de datos
	 */
    private static Connection connection;
    
    /**
     * Variable estática para indicar el SGBD
     */
    public static String name;

    /**
     * Constructor donde creamos la conexión a la base de datos
     */
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
        name = properties.getProperty("db.name");
        String url = properties.getProperty("db.url");
        String driver = properties.getProperty("db.driver");
        String user = properties.getProperty("db.user", "");
        String password = properties.getProperty("db.password", "");

        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, user, password);
            
            // Creamos las tablas
            crearTablas();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al establecer la conexión a la base de datos.");
        }
    }

 	/**
 	 * Método que devuelve la conexión a la base de datos.
 	 * @return conexión
 	 */
    public static Connection getConnection() {
        if (connection == null) {
            new SingletonConexion();
        }
        return connection;
    }
    
    /**
 	 * Método que cierra la conexión a la base de datos.
 	 */
    public static void closeConnection() {
        try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * Creamos las tablas Empleado y Departamento en la base de datos
     */
    private void crearTablas() {
    	try {
            Statement statement = connection.createStatement();
            String tipo = (name.equals("MariaDB")) ? "VARCHAR(50)" : "TEXT";

            // Creamos la tabla departamento
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS departamento ("
                    + "id " + tipo + " PRIMARY KEY,"
                    + "nombre " + tipo + ","
                    + "jefe " + tipo + ")");

            // Creamos la tabla empleado
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS empleado ("
                    + "id " + tipo + " PRIMARY KEY,"
                    + "nombre " + tipo + ","
                    + "salario DOUBLE,"
                    + "nacido " + tipo + ","
                    + "departamento " + tipo + ")");
            
            // Creamos las foreign key de las tablas
            // Controlamos que no se creen las foreign key si ya existen las tablas
//            try {
//            	statement.executeUpdate("ALTER TABLE departamento "
//                		+ "ADD CONSTRAINT fk_jefe " 
//                		+ "FOREIGN KEY (jefe) REFERENCES empleado(id)");
//                
//                statement.executeUpdate("ALTER TABLE empleado "
//                		+ "ADD CONSTRAINT fk_departamento " 
//                		+ "FOREIGN KEY (departamento) REFERENCES departamento(id)"); 	
//            } catch(Exception e) {
//            }
            
            // Cerramos la declaración
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al crear las tablas en la base de datos.");
        }
    }
}
