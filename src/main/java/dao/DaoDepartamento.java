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
	    StringBuffer sb = new StringBuffer();
	    try {
	        String sql = "SELECT departamento.id, departamento.nombre, departamento.jefe, empleado.nombre " +
	                     "FROM departamento " +
	                     "LEFT JOIN empleado ON departamento.jefe = empleado.id";

	        Statement stmt = MenuPrincipal.conn.createStatement();
	        ResultSet rs = stmt.executeQuery(sql);

	        String format = "%n[ %-36s ][ %-15s ][ %-4s ]%n";
            sb.append(String.format(format, "ID", "NOMBRE", "JEFE"));
	        while (rs.next()) {
	            UUID uuid = UUID.fromString(rs.getString("departamento.id"));
	            String nombre = rs.getString("departamento.nombre");
	            String jefeId = rs.getString("departamento.jefe");
	            String jefeNombre = rs.getString("empleado.nombre");

	            Empleado idEmpleado = (jefeId == null) ? null : new Empleado(UUID.fromString(jefeId), jefeNombre);
	            sb.append(new Departamento(uuid, nombre, idEmpleado).toString()).append("\n");
	        }
	        rs.close();
	        stmt.close();
	    } catch (SQLException e) {
	    }
	    return sb.toString();
	}

	//TODO: Controlar que pasa si el Jefe que intentas asignar ya es jefe de otro departamento, habria que hacer update en el departamento correspondiente para settear a NULL el jefe.
	@Override
	public Boolean insert(Departamento departamento) {
		PreparedStatement pstmt;
		try {
	            String sql = "INSERT INTO departamento (id, nombre, jefe) VALUES (?, ?, ?)";
	            pstmt = MenuPrincipal.conn.prepareStatement(sql);
	            
	            pstmt.setString(1, departamento.getId().toString());
	            pstmt.setString(2, departamento.getNombre());
	            String departamentoID = (departamento.getJefe().getId() == null) ? null : departamento.getJefe().getId().toString();
	            pstmt.setString(3, departamentoID);
	            int affectedRows = pstmt.executeUpdate();
	            
	            try {
	            	sql = "UPDATE empleado SET departamento = ? WHERE id = ?;";
	            	pstmt = MenuPrincipal.conn.prepareStatement(sql);
	            	pstmt.setString(1, departamento.getId().toString());
		            pstmt.setString(2, departamentoID);
		            pstmt.executeUpdate();
	            }catch(SQLException e) {
	            }
	            pstmt.close();
	            return affectedRows > 0;
	        } catch (SQLException e) {
	            return false;
	        }
	}
	
	//TODO: Controlar que pasa si el nuevo Jefe ya es Jefe de otro departamento.
	@Override
	public Boolean update(Departamento departamento) { 
        PreparedStatement stmt = null;
        int parameterIndex = 1;
        try {
        	//Creamos la sentencia SQL
            StringBuffer sql = new StringBuffer();
            sql.append("UPDATE departamento SET ");

            if (!departamento.getNombre().equals(""))sql.append("nombre = ?, ");
            if (departamento.getJefe().getId() != null)sql.append("jefe = ?, ");
            
         // Elimina la coma final y agrega la condición WHERE
            int length = sql.length();
            if (sql.charAt(length - 2) == ',') sql.delete(length - 2, length);
            sql.append(" WHERE id = ?");

            stmt = MenuPrincipal.conn.prepareStatement(sql.toString());

            // Asignamos los parámetros de la consulta
            if (!departamento.getNombre().equals(""))stmt.setString(parameterIndex++, departamento.getNombre());
            if (departamento.getJefe().getId() != null)stmt.setString(parameterIndex++, departamento.getJefe().getId().toString());
            
            // Establece el ID del departamento a actualizar
            stmt.setString(parameterIndex, departamento.getId().toString());

            // Ejecuta la consulta SQL con los valores actualizados y el ID
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
        }
        return false;
    }

	@Override
	public Boolean delete(Departamento departamento) {
	    PreparedStatement pstmt;
	    try {
	        try {
	            // Primero, actualiza los empleados para que el campo "departamento" sea NULL
	            String updateEmpleadosSQL = "UPDATE empleado SET departamento = NULL WHERE departamento = ?";
	            pstmt = MenuPrincipal.conn.prepareStatement(updateEmpleadosSQL);
	            pstmt.setString(1, departamento.getId().toString());
	            pstmt.execute();
	        } catch (SQLException e) {
	        }
	        // Luego, elimina el departamento
	        pstmt = MenuPrincipal.conn.prepareStatement("DELETE FROM departamento WHERE id = ?");
	        pstmt.setString(1, departamento.getId().toString());
	        pstmt.execute();

	        return true;
	    } catch (SQLException e) { 
	    }
	    return false;
	}

}


