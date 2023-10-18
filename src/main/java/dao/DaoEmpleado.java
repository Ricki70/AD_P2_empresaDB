package dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import model.Empleado;
import view.MenuPrincipal;

public class DaoEmpleado implements DaoInterface<Empleado>{
	
	@Override
	public String listar() {
		return null;
		
	}

	@Override
	public int insert(Empleado empleado) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public int update(Empleado empleado) {
		int filasAfectadas = 0;
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
            filasAfectadas = stmt.executeUpdate();

        } catch (SQLException e) {
            // Maneja excepciones
        }
		return filasAfectadas;
    }

	@Override
	public int delete(Empleado empleado) {
		// TODO Auto-generated method stub
		return 0;
	}
}
