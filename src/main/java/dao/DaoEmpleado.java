package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import model.Departamento;
import model.Empleado;
import view.MenuPrincipal;

public class DaoEmpleado implements DaoInterface<Empleado>{
	private static Boolean obtenerFK() {
	    try {
	        String sql = "SELECT departamento FROM empleado WHERE departamento IS NOT NULL";
	        Statement stmt = MenuPrincipal.conn.createStatement();
	        ResultSet rs = stmt.executeQuery(sql);
	        
	        // Si la consulta devuelve al menos una fila, significa que hay registros con jefe no nulo
	        if (rs.next()) {
	            rs.close();
	            stmt.close();
	            return true;
	        } else {
	            rs.close();
	            stmt.close();
	            return false;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false; // En caso de error, se devuelve false
	    }
	}
	
	@Override
	public String listar() {
		StringBuffer sb = new StringBuffer();
        try {
        	String sql;
        	if (obtenerFK()) {
        		sql = "SELECT empleado.id, empleado.nombre, empleado.salario, empleado.nacido, empleado.departamento, departamento.nombre " +
                   	 "FROM empleado " +
                   	 "INNER JOIN departamento ON empleado.departamento = departamento.id";
        	}else {
        		sql = "SELECT * FROM empleado";
        	}
            
            Statement stmt = MenuPrincipal.conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            String format = "%n[ %-36s ][ %-15s ][ %-8s ][ %-10s ][ %-10s ]%n";
            sb.append(String.format(format, "ID", "NOMBRE", "SALARIO", "NACIDO", "EMPLEADO"));
            while (rs.next()) {
                UUID uuid = UUID.fromString(rs.getString("empleado.id"));
                String nombre = rs.getString("empleado.nombre");
                Double salario = rs.getDouble("empleado.salario");
                LocalDate nacido =  LocalDate.parse(rs.getString("empleado.nacido"), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                Departamento idDepartamento = (rs.getString("empleado.departamento") == null) ? null : new Departamento(UUID.fromString(rs.getString("empleado.departamento")), rs.getString("departamento.nombre"));
                sb.append(new Empleado(uuid, nombre, salario, nacido, idDepartamento).toString()).append("\n");
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sb.toString();
	}

	@Override
	public Boolean insert(Empleado empleado) {
		 try {
	            String sql = "INSERT INTO empleado (id, nombre, salario, nacido, departamento) VALUES (?, ?, ?, ?, ?)";
	            PreparedStatement pstmt = MenuPrincipal.conn.prepareStatement(sql);
	            
	            pstmt.setString(1, empleado.getId().toString());
	            pstmt.setString(2, empleado.getNombre());
	            pstmt.setDouble(3, empleado.getSalario());
	            pstmt.setString(4, empleado.getNacido().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
	            String departamentoID = (empleado.getDepartamento().getId() == null) ? null : empleado.getDepartamento().getId().toString();
	            pstmt.setString(5, departamentoID);

	            int affectedRows = pstmt.executeUpdate();
	            pstmt.close();
	            return affectedRows > 0;
	        } catch (SQLException e) {
	            e.printStackTrace();
	            return false;
	        }
	}
	
	@Override
	public Boolean update(Empleado empleado) {
        PreparedStatement stmt = null;

        try {
            StringBuffer sql = new StringBuffer();
            sql.append("UPDATE empleado SET ");

            if (!empleado.getNombre().equals("")) sql.append("nombre = ?, "); 
            if (empleado.getSalario() != null) sql.append("salario = ?, ");
            if (empleado.getNacido() != null) sql.append("nacido = ?, ");            
            if (empleado.getDepartamento().getId() != null)sql.append("departamento = ?, ");
            
            // Elimina la coma final y agrega la condición WHERE
            int length = sql.length();
            if (sql.charAt(length - 2) == ',') sql.delete(length - 2, length);
            
            sql.append(" WHERE id = ?");

            stmt = MenuPrincipal.conn.prepareStatement(sql.toString());

            // Asignamos los parámetros de la consulta
            int parameterIndex = 1;
            if (!empleado.getNombre().equals("")) stmt.setString(parameterIndex++, empleado.getNombre());           
            if (empleado.getSalario() != null) stmt.setDouble(parameterIndex++, empleado.getSalario());          
            if (empleado.getNacido() != null) stmt.setString(parameterIndex++, empleado.getNacido().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));      
            if (empleado.getDepartamento().getId() != null) stmt.setString(parameterIndex++, empleado.getDepartamento().getId().toString());
            
            // Establece el ID del empleado a actualizar
            stmt.setString(parameterIndex, empleado.getId().toString());

            // Ejecuta la consulta SQL con los valores actualizados y el ID
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
        }
		return false;
    }

	@Override
	public Boolean delete(Empleado empleado) {
		//TODO:Programar metodo eliminar empleado
		return false;
	}
}
