package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import model.Contacto;

public class AccesoSQLite {
    
    private Connection conn; // Utiliza la conexión Singleton

    public AccesoSQLite() {
        // Constructor para inicializar la base de datos si es necesario
        initDatabase();
    }

    private void initDatabase() {
        try {
            conn = SingletonSQLite.getConnection();
            Statement stmt = conn.createStatement();

            // Crear la tabla si no existe
            String sql = "CREATE TABLE IF NOT EXISTS contactos (id TEXT PRIMARY KEY, nombre TEXT, telefono TEXT, edad INT)";
            stmt.execute(sql);
            stmt.close();
//            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String show() {
        StringBuilder sb = new StringBuilder();
        try {
//        	conn = SingletonSQLite.getConnection();
            String sql = "SELECT * FROM contactos";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                UUID uuid = UUID.fromString(rs.getString("id"));
                String nombre = rs.getString("nombre");
                String telefono = rs.getString("telefono");
                int edad = rs.getInt("edad");
                sb.append(new Contacto(uuid, nombre, telefono, edad).toString()).append("\n");
            }
            rs.close();		//PORQUE NO SE CREA UN SINGLETON CON ESTO TAMBIEN EN LUGAR DE ABRIRLO Y CERRARLO POR CADA METODO?
            stmt.close();
//            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public Contacto buscarPorCodigo(String id) {
        try {
//        	conn = SingletonSQLite.getConnection();
            String sql = "SELECT * FROM contactos WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                UUID uuid = UUID.fromString(rs.getString("id"));
                String nombre = rs.getString("nombre");
                String telefono = rs.getString("telefono");
                int edad = rs.getInt("edad");
//                conn.close();
                return new Contacto(uuid, nombre, telefono, edad);
            }
            rs.close();
            pstmt.close();
//            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Contacto> buscarPorNombre(String inicio) {
        List<Contacto> contactos = new ArrayList<>();
        try {
//        	conn = SingletonSQLite.getConnection();
            String sql = "SELECT * FROM contactos WHERE nombre LIKE ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, inicio + "%");

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                UUID uuid = UUID.fromString(rs.getString("id"));
                String nombre = rs.getString("nombre");
                String telefono = rs.getString("telefono");
                int edad = rs.getInt("edad");
                contactos.add(new Contacto(uuid, nombre, telefono, edad));
            }
            rs.close();
            pstmt.close();
//            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contactos;
    }

    public boolean add(Contacto c) {
        try {
//        	conn = SingletonSQLite.getConnection();
            String sql = "INSERT INTO contactos (id, nombre, telefono, edad) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, c.getUsuario().toString());
            pstmt.setString(2, c.getNombre());
            pstmt.setString(3, c.getTelefono());
            pstmt.setInt(4, c.getEdad());

            int affectedRows = pstmt.executeUpdate();
//            conn.close();
            pstmt.close();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String id) {
        try {
//        	conn = SingletonSQLite.getConnection();
            String sql = "DELETE FROM contactos WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, id);

            int affectedRows = pstmt.executeUpdate();
//            conn.close();
            pstmt.close();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void drop() {
        try {
//        	conn = SingletonSQLite.getConnection();
            String sql = "DELETE FROM contactos";
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
//            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
//    public void pack() {
//        try {
//            // Crear una copia de seguridad de la tabla de contactos
//            String backupTableName = "contactos_backup";
//            conn = SingletonSQLite.getConnection();
//            Statement stmt = conn.createStatement();
//            stmt.execute("CREATE TABLE IF NOT EXISTS " + backupTableName + " AS SELECT * FROM contactos");
//            stmt.close();
//
//            // Borrar todos los contactos marcados como borrados
//            String sqlDelete = "DELETE FROM contactos WHERE id = ?";
//            PreparedStatement pstmtDelete = conn.prepareStatement(sqlDelete);
//            pstmtDelete.setString(1, "00000000-0000-0000-0000-000000000000");
//            pstmtDelete.executeUpdate();
//            pstmtDelete.close();
//
//            // Recuperar los contactos de la copia de seguridad que no están marcados como borrados
//            String sqlInsert = "INSERT INTO contactos (id, nombre, telefono, edad) VALUES (?, ?, ?, ?)";
//            PreparedStatement pstmtInsert = conn.prepareStatement(sqlInsert);
//            String sqlSelect = "SELECT * FROM " + backupTableName;
//            Statement stmtSelect = conn.createStatement();
//            ResultSet rs = stmtSelect.executeQuery(sqlSelect);
//
//            while (rs.next()) {
//                String id = rs.getString("id");
//                String nombre = rs.getString("nombre");
//                String telefono = rs.getString("telefono");
//                int edad = rs.getInt("edad");
//
//                if (!id.equals("00000000-0000-0000-0000-000000000000")) {
//                    pstmtInsert.setString(1, id);
//                    pstmtInsert.setString(2, nombre);
//                    pstmtInsert.setString(3, telefono);
//                    pstmtInsert.setInt(4, edad);
//                    pstmtInsert.executeUpdate();
//                }
//            }
//
//            // Eliminar la copia de seguridad
//            stmt.execute("DROP TABLE IF EXISTS " + backupTableName);
//            stmt.close();
//
//            conn.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
    
    public void cerrar() {
    	try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
