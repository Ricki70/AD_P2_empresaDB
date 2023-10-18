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
	
	@Override
	public String listar() {
		StringBuilder sb = new StringBuilder();
        try {
            String sql = "SELECT * FROM empleado";
            Statement stmt = MenuPrincipal.conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                UUID uuid = UUID.fromString(rs.getString("id"));
                String nombre = rs.getString("nombre");
                Double salario = rs.getDouble("salario");
                LocalDate nacido =  LocalDate.parse(rs.getString("nacido"), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                Departamento idDepartamento = new Departamento(UUID.fromString(rs.getString("departamento")));
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
//	        	conn = SingletonSQLite.getConnection();
	            String sql = "INSERT INTO empleado (id, nombre, salario, nacido, departamento) VALUES (?, ?, ?, ?, ?)";
	            PreparedStatement pstmt = MenuPrincipal.conn.prepareStatement(sql);
	            
	            pstmt.setString(1, empleado.getId().toString());
	            pstmt.setString(2, empleado.getNombre());
	            pstmt.setDouble(3, empleado.getSalario());
	            pstmt.setString(4, empleado.getNacido().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
	            pstmt.setString(5, empleado.getDepartamento().getId().toString());

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
	public Boolean update(Empleado empleado) {
        PreparedStatement stmt = null;

        try {
            String sql = "UPDATE empleado SET ";

            if (!empleado.getNombre().equals("")) {
                sql += "nombre = ?, ";
            }
            if (empleado.getSalario() != null) {
                sql += "salario = ?, ";
            }
            if (!empleado.getNacido().toString().equals("")) {
                sql += "nacido = ?, ";
            }
            if (empleado.getDepartamento().getId().toString().equals("")) {
                sql += "departamento = ?, ";
            }

            // Elimina la coma final y agrega la condición WHERE
            sql = sql.substring(0, sql.length() - 2) + " WHERE id = ?";

            stmt = MenuPrincipal.conn.prepareStatement(sql);

            int parameterIndex = 1;

            if (!empleado.getNombre().equals("")) {
                stmt.setString(parameterIndex++, empleado.getNombre());
            }
            if (empleado.getSalario() != null) {
                stmt.setDouble(parameterIndex++, empleado.getSalario());
            }
            if (!empleado.getNacido().toString().equals("")) {
                stmt.setString(parameterIndex++, empleado.getNacido().toString());
            }
            if (!empleado.getDepartamento().getId().toString().equals("")) {
                stmt.setString(parameterIndex++, empleado.getDepartamento().getId().toString());
            }

            // Establece el ID del empleado a actualizar
            stmt.setString(parameterIndex, empleado.getId().toString());

            // Ejecuta la consulta SQL con los valores actualizados y el ID
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            // Maneja excepciones
        }
		return false;
    }

	@Override
	public Boolean delete(Empleado empleado) {
		// TODO Auto-generated method stub
		return false;
	}
}
