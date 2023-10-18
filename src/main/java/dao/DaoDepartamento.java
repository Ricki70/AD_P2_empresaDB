package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import model.Departamento;
import model.Empleado;
import view.MenuPrincipal;

public class DaoDepartamento implements DaoInterface<Departamento>{
	
	@Override
	public String listar() {
		StringBuilder sb = new StringBuilder();
        try {
            String sql = "SELECT * FROM departamento";
            Statement stmt = MenuPrincipal.conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                UUID uuid = UUID.fromString(rs.getString("id"));
                String nombre = rs.getString("nombre");
                Empleado idEmpleado = new Empleado(UUID.fromString(rs.getString("jefe")));
                sb.append(new Departamento(uuid, nombre, idEmpleado).toString()).append("\n");
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sb.toString();
	}

	@Override
	public Boolean insert(Departamento departamento) {
		 try {
//	        	conn = SingletonSQLite.getConnection();
	            String sql = "INSERT INTO departamento (id, nombre, jefe) VALUES (?, ?, ?)";
	            PreparedStatement pstmt = MenuPrincipal.conn.prepareStatement(sql);
	            
	            pstmt.setString(1, departamento.getId().toString());
	            pstmt.setString(2, departamento.getNombre());
	            pstmt.setString(3, departamento.getJefe().getId().toString());

	            int affectedRows = pstmt.executeUpdate();
//	            conn.close();
	            pstmt.close();
	            return affectedRows > 0;
	        } catch (SQLException e) {
	            e.printStackTrace();
	            return false;
	        }
	}
	
	@Override
	public Boolean update(Departamento departamento) { //TODO: Arreglar los mensajes de error clase (DaoDepartamento).
        PreparedStatement stmt = null;

        try {
            String sql = "UPDATE departamento SET ";

            if (!departamento.getNombre().equals("")) {
                sql += "nombre = ?, ";
            }
            if (!departamento.getJefe().getId().toString().equals(new UUID(0, 0).toString())) {
                sql += "jefe = ?, ";
            }
            
            // Elimina la coma final y agrega la condición WHERE
            sql = sql.substring(0, sql.length() - 2) + " WHERE id = ?";

            stmt = MenuPrincipal.conn.prepareStatement(sql);

            int parameterIndex = 1;

            if (!departamento.getNombre().equals("")) {
                stmt.setString(parameterIndex++, departamento.getNombre());
            }
            if (!departamento.getJefe().getId().toString().equals(new UUID(0, 0).toString())) {
                stmt.setString(parameterIndex++, departamento.getJefe().getId().toString());
            }
            // Establece el ID del empleado a actualizar
            stmt.setString(parameterIndex, departamento.getId().toString());

            // Ejecuta la consulta SQL con los valores actualizados y el ID
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
        	System.err.println("Empleado introducido no existe en la tabla empleado"); //Aqui entra siempre, de el error que de
        	//Habria que afinar los errores para que solo salte el que deberia saltar en cada momento
        } 
        
        return false;
    }

	@Override
	public Boolean delete(Departamento t) {
		// TODO Auto-generated method stub
		return false;
	}

}
