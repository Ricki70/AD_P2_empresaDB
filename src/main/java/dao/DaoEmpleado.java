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
	//TODO: Revisar todos los posibles fallos (En principio estan todos controlamos pero ya nos conocemos)
	@Override
	public String listar() {
		StringBuffer sb = new StringBuffer();
        try {
            String sql = "SELECT * FROM empleado"; //TODO:Completar query para que devuelva tambien el nombre asociado al ID del departamento
            Statement stmt = MenuPrincipal.conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            String format = "%n[ %-36s ][ %-25s ][ %-8s ][ %-10s ][ %-30s ]%n";
            sb.append(String.format(format, "ID", "NOMBRE", "SALARIO", "NACIDO", "EMPLEADO"));
            while (rs.next()) {
                UUID uuid = UUID.fromString(rs.getString("id"));
                String nombre = rs.getString("nombre");
                Double salario = rs.getDouble("salario");
                LocalDate nacido =  LocalDate.parse(rs.getString("nacido"), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                Departamento idDepartamento = (rs.getString("departamento") == null) ? null : new Departamento(UUID.fromString(rs.getString("departamento")));
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
