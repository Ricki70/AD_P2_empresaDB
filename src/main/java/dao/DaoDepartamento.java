package dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import model.Empleado;
import view.MenuPrincipal;

public class DaoDepartamento {
	
	public void update(Empleado empleado) {
        PreparedStatement stmt = null;

        try {
            String sql = "UPDATE empleado SET ";

            if (empleado.getNombre() != null) {
                sql += "nombre = ?, ";
            }
            if (empleado.getSalario() != 0.0) {
                sql += "jefe = ?, ";
            }
            

            // Elimina la coma final y agrega la condición WHERE
            sql = sql.substring(0, sql.length() - 2) + " WHERE id = ?";

            stmt = MenuPrincipal.conn.prepareStatement(sql);

            int parameterIndex = 1;

            if (!empleado.getNombre().equals("")) {
                stmt.setString(parameterIndex++, empleado.getNombre());
            }
            if (empleado.getSalario() != 0.0) {
                stmt.setDouble(parameterIndex++, empleado.getSalario());
            }

            // Establece el ID del empleado a actualizar
            stmt.setString(parameterIndex, empleado.getId().toString());

            // Ejecuta la consulta SQL con los valores actualizados y el ID
            stmt.executeUpdate();

        } catch (SQLException e) {
            // Maneja excepciones
        } finally {
            // Cierra la conexión y el PreparedStatement
        }
    }

}
