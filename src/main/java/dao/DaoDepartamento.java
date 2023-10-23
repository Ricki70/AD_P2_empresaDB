package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import model.Departamento;
import model.Empleado;
import view.MenuPrincipal;

public class DaoDepartamento implements DaoInterface<Departamento> {
	
	@Override
	public String listar() {
		StringBuffer sb = new StringBuffer();
		try {
			// Creamos la consulta que devolverá los registros de la tabla departamento
			String sql = "SELECT departamento.id, departamento.nombre, departamento.jefe, empleado.nombre "
					+ "FROM departamento LEFT JOIN empleado " 
					+ "ON departamento.jefe = empleado.id";

			Statement stmt = MenuPrincipal.conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			// Damos formato a la muestra de datos
			String format = "%n[ %-36s ][ %-15s ][ %-4s ]%n";
			sb.append(String.format(format, "ID", "NOMBRE", "JEFE"));
			
			// Recorremos el conjunto de registros obtenidos y lo almacenamos en un string
			while (rs.next()) {
				// Para cada registro, asignamos los parámetros de la consulta
				UUID uuid = UUID.fromString(rs.getString("departamento.id"));
				String nombre = rs.getString("departamento.nombre");
				String jefeId = rs.getString("departamento.jefe");
				String jefeNombre = rs.getString("empleado.nombre");

				Empleado empleado = (jefeId == null) ? null : new Empleado(UUID.fromString(jefeId), jefeNombre);
				sb.append(new Departamento(uuid, nombre, empleado).toString()).append("\n");
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
		}
		// Devolvemos el string con todos los registros de la tabla
		return sb.toString();
	}

	// TODO: Controlar que pasa si el Jefe que intentas asignar ya es jefe de otro
	// departamento, habria que hacer update en el departamento correspondiente para
	// settear a NULL el jefe.
	@Override
	public Boolean insert(Departamento departamento) {
		PreparedStatement pstmt;
		try {
			String sql;
			String empleadoID = (departamento.getJefe().getId() == null) ? null : departamento.getJefe().getId().toString();
			
			if (empleadoID == null || existeEmpleado(empleadoID)) {
				
				actualizarJefes(empleadoID);
				// Creamos la consulta para insertar un registro en la tabla departamento
				sql = "INSERT INTO departamento (id, nombre, jefe) VALUES (?, ?, ?)";
				pstmt = MenuPrincipal.conn.prepareStatement(sql);
	
				// Asignamos los parámetros de la consulta
				pstmt.setString(1, departamento.getId().toString());
				pstmt.setString(2, departamento.getNombre());
				pstmt.setString(3, empleadoID);
				
				int affectedRows = pstmt.executeUpdate();
				
				actualizarEmpleado(empleadoID, departamento);
				return affectedRows > 0; // true si hay filas afectadas, false si no
			}
		} catch (SQLException e) {
			return false;
		}
		return false;
	}

	// TODO: Controlar que pasa si el nuevo Jefe ya es Jefe de otro departamento.
	@Override
	public Boolean update(Departamento departamento) {
		PreparedStatement pstmt = null;
		try {
			String empleadoID = (departamento.getJefe().getId() == null) ? null : departamento.getJefe().getId().toString();
			actualizarJefes(empleadoID);
			
			// Creamos la sentencia SQL
			StringBuffer sql = new StringBuffer();
			sql.append("UPDATE departamento SET ");

			if (!departamento.getNombre().equals(""))
				sql.append("nombre = ?, ");
			if (departamento.getJefe().getId() != null)
				sql.append("jefe = ?, ");

			// Elimina la coma final y agrega la condición WHERE
			int length = sql.length();
			if (sql.charAt(length - 2) == ',') sql.delete(length - 2, length);
			sql.append(" WHERE id = ?");

			pstmt = MenuPrincipal.conn.prepareStatement(sql.toString());

			// Asignamos los parámetros de la consulta
			int parameterIndex = 1;
			if (!departamento.getNombre().equals("")) 
				pstmt.setString(parameterIndex++, departamento.getNombre());
			if (departamento.getJefe().getId() != null)
				pstmt.setString(parameterIndex++, departamento.getJefe().getId().toString());

			// Establece el ID del departamento a actualizar
			pstmt.setString(parameterIndex, departamento.getId().toString());
			
			actualizarEmpleado(empleadoID, departamento);
			// Ejecuta la consulta SQL y comprueba si se ha modificado algún registro o no
			return pstmt.executeUpdate() > 0;

		} catch (SQLException e) {
		}
		return false;
	}

	@Override
	public Boolean delete(Departamento departamento) {
		PreparedStatement pstmt;
		try {
			// Primero, actualiza los empleados para que el campo "departamento" sea NULL
			try {
				String updateEmpleadosSQL = "UPDATE empleado SET departamento = NULL WHERE departamento = ?";
				pstmt = MenuPrincipal.conn.prepareStatement(updateEmpleadosSQL);
				pstmt.setString(1, departamento.getId().toString());
				pstmt.execute();
			} catch (SQLException e) {
			}
			
			// Luego, eliminamos el departamento
			pstmt = MenuPrincipal.conn.prepareStatement("DELETE FROM departamento WHERE id = ?");
			pstmt.setString(1, departamento.getId().toString());
			pstmt.execute();

			return true;
		} catch (SQLException e) {
		}
		return false;
	}
	
	private static Boolean existeEmpleado(String empleadoID) throws SQLException {
		PreparedStatement pstmt;
		String sql = "SELECT * FROM empleado WHERE id = ?";
		pstmt = MenuPrincipal.conn.prepareStatement(sql);
		pstmt.setString(1, empleadoID);
		return pstmt.executeQuery().next();
	}
	
	private static Boolean actualizarJefes(String empleadoID) throws SQLException {
		String sql;
		PreparedStatement pstmt = null;
		sql = "UPDATE departamento SET jefe = NULL WHERE jefe = ?";
		pstmt = MenuPrincipal.conn.prepareStatement(sql);
		pstmt.setString(1, empleadoID);
		return pstmt.executeUpdate() > 0;
	}
	
	private static Boolean actualizarEmpleado(String empleadoID, Departamento departamento) throws SQLException {
		// Actualizamos en la tabla empleado el registro del jefe introducido asignándole el departamento creado
		String sql = "UPDATE empleado SET departamento = ? WHERE id = ?;";
		PreparedStatement pstmt = null;
		pstmt = MenuPrincipal.conn.prepareStatement(sql);
		pstmt.setString(1, departamento.getId().toString());
		pstmt.setString(2, empleadoID);
		return pstmt.executeUpdate() > 0;
	}

}
