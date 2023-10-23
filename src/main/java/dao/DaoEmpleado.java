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

public class DaoEmpleado implements DaoInterface<Empleado> {

	@Override
	public String listar() {
		StringBuffer sb = new StringBuffer();
		try {
			// Creamos la consulta que devolverá los registros de la tabla empleado
			String sql = "SELECT empleado.id, empleado.nombre, empleado.salario, empleado.nacido, empleado.departamento, departamento.nombre "
					+ "FROM empleado LEFT JOIN departamento " 
					+ "ON empleado.departamento = departamento.id";

			Statement stmt = MenuPrincipal.conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			// Damos formato a la muestra de datos
			String format = "%n[ %-36s ][ %-15s ][ %-8s ][ %-10s ][ %-10s ]%n";
			sb.append(String.format(format, "ID", "NOMBRE", "SALARIO", "NACIDO", "DEPARTAMENTO"));

			// Recorremos el conjunto de registros obtenidos y lo almacenamos en un string
			while (rs.next()) {
				// Para cada registro, asignamos los parámetros de la consulta
				UUID uuid = UUID.fromString(rs.getString("empleado.id"));
				String nombre = rs.getString("empleado.nombre");
				Double salario = rs.getDouble("empleado.salario");
				LocalDate nacido = LocalDate.parse(rs.getString("empleado.nacido"),
						DateTimeFormatter.ofPattern("dd-MM-yyyy"));
				Departamento idDepartamento = (rs.getString("empleado.departamento") == null) ? null
						: new Departamento(UUID.fromString(rs.getString("empleado.departamento")),
								rs.getString("departamento.nombre"));
				sb.append(new Empleado(uuid, nombre, salario, nacido, idDepartamento).toString()).append("\n");
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
		}
		
		// Devolvemos el string con todos los registros de la tabla
		return sb.toString();
	}

	@Override
	public Boolean insert(Empleado empleado) {
		try {
			// Creamos la consulta para insertar un registro en la tabla empleado
			String sql = "INSERT INTO empleado (id, nombre, salario, nacido, departamento) VALUES (?, ?, ?, ?, ?)";
			PreparedStatement pstmt = MenuPrincipal.conn.prepareStatement(sql);

			// Asignamos los parámetros de la consulta
			pstmt.setString(1, empleado.getId().toString());
			pstmt.setString(2, empleado.getNombre());
			pstmt.setDouble(3, empleado.getSalario());
			pstmt.setString(4, empleado.getNacido().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
			String departamentoID = (empleado.getDepartamento().getId() == null) ? null : empleado.getDepartamento().getId().toString();
			pstmt.setString(5, departamentoID);

			return pstmt.executeUpdate() > 0;  // true si hay filas afectadas, false si no
		} catch (SQLException e) {
			return false;
		}
	}

	@Override
	public Boolean update(Empleado empleado) {
		PreparedStatement stmt = null;
		try {
			// Creamos la sentencia SQL
			StringBuffer sql = new StringBuffer();
			sql.append("UPDATE empleado SET ");

			if (!empleado.getNombre().equals(""))
				sql.append("nombre = ?, ");
			if (empleado.getSalario() != null)
				sql.append("salario = ?, ");
			if (empleado.getNacido() != null)
				sql.append("nacido = ?, ");
			if (empleado.getDepartamento().getId() != null)
				sql.append("departamento = ?, ");

			// Eliminamos la coma final y agregamos la condición WHERE
			int length = sql.length();
			if (sql.charAt(length - 2) == ',')
				sql.delete(length - 2, length);
			sql.append(" WHERE id = ?");

			stmt = MenuPrincipal.conn.prepareStatement(sql.toString());

			// Asignamos los parámetros de la consulta
			int parameterIndex = 1;
			if (!empleado.getNombre().equals(""))
				stmt.setString(parameterIndex++, empleado.getNombre());
			if (empleado.getSalario() != null)
				stmt.setDouble(parameterIndex++, empleado.getSalario());
			if (empleado.getNacido() != null)
				stmt.setString(parameterIndex++, empleado.getNacido().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
			if (empleado.getDepartamento().getId() != null)
				stmt.setString(parameterIndex++, empleado.getDepartamento().getId().toString());

			// Establecemos el ID del empleado a actualizar
			stmt.setString(parameterIndex, empleado.getId().toString());

			// Ejecutamos la consulta SQL y comprobamos si se ha modificado algún registro o no
			return stmt.executeUpdate() > 0;

		} catch (SQLException e) {
		}
		return false;
	}

	@Override
	public Boolean delete(Empleado empleado) {
		PreparedStatement pstmt;
		try {
			// Primero, actualizamos los departamentos para que el campo "jefe" sea NULL
			try {
				String updateDepartamentosSQL = "UPDATE departamento SET jefe = NULL WHERE jefe = ?";
				pstmt = MenuPrincipal.conn.prepareStatement(updateDepartamentosSQL);
				pstmt.setString(1, empleado.getId().toString());
				pstmt.execute();
			} catch (SQLException e) {
			}
			
			// Luego, eliminamos el empleado
			pstmt = MenuPrincipal.conn.prepareStatement("DELETE FROM empleado WHERE id = ?");
			pstmt.setString(1, empleado.getId().toString());
			pstmt.execute();

			return true;
		} catch (SQLException e) {
		}
		return false;
	}
}
