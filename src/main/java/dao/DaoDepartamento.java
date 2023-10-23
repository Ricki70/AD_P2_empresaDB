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

	@Override
	public Boolean insert(Departamento departamento) {
		PreparedStatement pstmt;
		try {
			// Comprobamos si se ha introducido un jefe para el departamento
			String empleadoID = (departamento.getJefe().getId() == null) ? null : departamento.getJefe().getId().toString();
			
			// Si el jefe es null o existe ese empleado, insertamos el nuevo departamento
			if (empleadoID == null || DaoInterface.existeEmpleado(empleadoID)) {
				// PASO 1 -> insertamos el nuevo departamento
				// Creamos la consulta para insertar el registro en la tabla departamento
				String sql = "INSERT INTO departamento (id, nombre, jefe) VALUES (?, ?, ?)";
				pstmt = MenuPrincipal.conn.prepareStatement(sql);
	
				// Asignamos los parámetros de la consulta
				pstmt.setString(1, departamento.getId().toString());
				pstmt.setString(2, departamento.getNombre());
				pstmt.setString(3, empleadoID);
				
				// PASO 2 -> actualizamos el departamento que tuviese como jefe al jefe introducido para resetearlo a null
				DaoInterface.actualizarJefes(empleadoID);
				
				// PASO 3 -> actualizamos el empleado que se ha establecido como jefe y le actualizamos el departamento al nuevo departamento insertado
				actualizarEmpleado(empleadoID, departamento);
				
				// Devolvemos el número de filas afectadas por la actalización (true si hay filas afectadas, false si no)
				return pstmt.executeUpdate() > 0; 
			}
		} catch (SQLException e) {
			return false;  // ha habido problemas, no se ha insertado
		}
		return false; // ha habido problemas, no se ha insertado
	}

	@Override
	public Boolean update(Departamento departamento) {
		PreparedStatement pstmt;
		try {
			// Comprobamos si se ha introducido un jefe para el departamento
			String empleadoID = (departamento.getJefe().getId() == null) ? null : departamento.getJefe().getId().toString();
			
			// Si el jefe es null o existe ese empleado, modificamos el departamento
			if(empleadoID == null || DaoInterface.existeEmpleado(empleadoID)) {
				// PASO 1 -> modificamos el departamento
				// Creamos la sentencia SQL, que será una concatenación en función de los campos que se quieren modificar
				StringBuffer sql = new StringBuffer();
				sql.append("UPDATE departamento SET ");
	
				if (!departamento.getNombre().equals(""))  
					sql.append("nombre = ?, ");  // modifica nombre
				if (departamento.getJefe().getId() != null)
					sql.append("jefe = ?, ");  // modifica jefe
	
				// Eliminamos la coma final y agregamos la condición WHERE
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
	
				// Establecemos el ID del departamento a actualizar
				pstmt.setString(parameterIndex, departamento.getId().toString());
				
				// PASO 2 -> actualizamos el departamento que tuviese como jefe al jefe introducido para resetearlo a null
				DaoInterface.actualizarJefes(empleadoID);
				
				// PASO 3 -> actualizamos el empleado que se ha establecido como jefe y le actualizamos el departamento al nuevo departamento insertado
				actualizarEmpleado(empleadoID, departamento);
				
				// Ejecutamos la consulta SQL y comprobamos si se ha modificado algún registro o no
				return pstmt.executeUpdate() > 0;
			}
		} catch (SQLException e) {
		}
		return false; // ha habido problemas, no se ha modificado
	}

	@Override
	public Boolean delete(Departamento departamento) {
		PreparedStatement pstmt;
		try {
			// Comprobamos si existe el departamento que se quiere eliminar
			if(DaoInterface.existeDepartamento(departamento.getId().toString())) {
				// Primero, actualizamos los empleados con ese departamento para resetearles el campo "departamento" a NULL
				try {
					String updateEmpleadosSQL = "UPDATE empleado SET departamento = NULL WHERE departamento = ?";
					pstmt = MenuPrincipal.conn.prepareStatement(updateEmpleadosSQL);
					pstmt.setString(1, departamento.getId().toString());
					pstmt.execute();
				} catch (SQLException e) {
				}
			
				// Después, eliminamos el departamento
				String sql = "DELETE FROM departamento WHERE id = ?";
				pstmt = MenuPrincipal.conn.prepareStatement(sql);
				pstmt.setString(1, departamento.getId().toString());
				pstmt.execute();

				return true;
			}
		} catch (SQLException e) {
		}
		return false;
	}
	
	/**
	 * Método privado para modificar el departamento de un empleado
	 * 
	 * @param empleadoID id del empleado que se quiere modificar
	 * @param departamento que se quiere asignar al empleado
	 * @return true si se ha podido modificar, false en caso contrario
	 * @throws SQLException excepción tratada en los métodos que llaman a este método
	 */
	private static Boolean actualizarEmpleado(String empleadoID, Departamento departamento) throws SQLException {
		String sql = "UPDATE empleado SET departamento = ? WHERE id = ?;";
		PreparedStatement pstmt = MenuPrincipal.conn.prepareStatement(sql);
		pstmt.setString(1, departamento.getId().toString());
		pstmt.setString(2, empleadoID);
		return pstmt.executeUpdate() > 0;  // devuelve si hay filas afectadas o no
	}

}
