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
				UUID uuid = UUID.fromString(rs.getString(1));
				String nombre = rs.getString(2);
				Double salario = rs.getDouble(3);
				LocalDate nacido = LocalDate.parse(rs.getString(4), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
				Departamento idDepartamento = (rs.getString(5) == null) ? null : new Departamento(UUID.fromString(rs.getString(5)), rs.getString(6));
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
			// Comprobamos si se ha introducido un departamento para el empleado
			String departamentoID = (empleado.getDepartamento().getId() == null) ? null : empleado.getDepartamento().getId().toString();
			
			// Si el departamento es null o existe ese departamento, insertamos el nuevo empleado
			if(departamentoID == null || DaoInterface.existeDepartamento(departamentoID)) {
				// Creamos la consulta para insertar un registro en la tabla empleado
				String sql = "INSERT INTO empleado (id, nombre, salario, nacido, departamento) VALUES (?, ?, ?, ?, ?)";
				PreparedStatement pstmt = MenuPrincipal.conn.prepareStatement(sql);

				// Asignamos los parámetros de la consulta
				pstmt.setString(1, empleado.getId().toString());
				pstmt.setString(2, empleado.getNombre());
				pstmt.setDouble(3, empleado.getSalario());
				pstmt.setString(4, empleado.getNacido().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
				pstmt.setString(5, departamentoID);

				return pstmt.executeUpdate() > 0;  // true si hay filas afectadas, false si no
			}
		} catch (SQLException e) {
			return false;  // ha habido problemas, no se ha insertado
		}
		return false;  // ha habido problemas, no se ha insertado
	}

	@Override
	public Boolean update(Empleado empleado) {
		PreparedStatement pstmt = null;
		try {
			// Comprobamos si se ha introducido un departamento para el empleado
			String departamentoID = (empleado.getDepartamento().getId() == null) ? null : empleado.getDepartamento().getId().toString();
					
			// Si el departamento es null o existe ese departamento, modificamos el empleado
			if(departamentoID == null || DaoInterface.existeDepartamento(departamentoID)) {
				// PASO 1 -> modificamos el empleado
				// Creamos la sentencia SQL, que será una concatenación en función de los campos que se quieren modificar
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
	
				pstmt = MenuPrincipal.conn.prepareStatement(sql.toString());

				// Asignamos los parámetros de la consulta
				int parameterIndex = 1;
				if (!empleado.getNombre().equals(""))
					pstmt.setString(parameterIndex++, empleado.getNombre());
				if (empleado.getSalario() != null)
					pstmt.setDouble(parameterIndex++, empleado.getSalario());
				if (empleado.getNacido() != null)
					pstmt.setString(parameterIndex++, empleado.getNacido().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
				if (empleado.getDepartamento().getId() != null)
					pstmt.setString(parameterIndex++, empleado.getDepartamento().getId().toString());
	
				// Establecemos el ID del empleado a actualizar
				pstmt.setString(parameterIndex, empleado.getId().toString());

				// PASO 2 -> si se ha introducido un nuevo departamento y este empleado era jefe de otro, 
				// se debe actualizar el jefe del departamento antiguo a null
				if(departamentoID != null) DaoInterface.actualizarJefes(empleado.getId().toString());
				
				// Ejecutamos la consulta SQL y comprobamos si se ha modificado algún registro o no
				return pstmt.executeUpdate() > 0;
			}
		} catch (SQLException e) {
		}
		return false;  // ha habido problemas, no se ha modificado
	}

	@Override
	public Boolean delete(Empleado empleado) {
		PreparedStatement pstmt;
		try {
			// Comprobamos si existe el empleado que se quiere eliminar
			if(DaoInterface.existeEmpleado(empleado.getId().toString())) {
				// Actualizamos el departamento que tuviese a ese empleado como jefe para establecerlo a null
				DaoInterface.actualizarJefes(empleado.getId().toString());
				
				// Eliminamos el empleado
				pstmt = MenuPrincipal.conn.prepareStatement("DELETE FROM empleado WHERE id = ?");
				pstmt.setString(1, empleado.getId().toString());
				pstmt.execute();

				return true;
			}
		} catch (SQLException e) {
		}
		return false;
	}
}
