package dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import model.Departamento;
import view.MenuPrincipal;

public class DaoDepartamento implements DaoInterface<Departamento>{
	
	@Override
	public String listar() {
		
		return null;
	}

	@Override
	public int insert(Departamento departamento) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public int update(Departamento departamento) {
		
		int filasAfectadas = 0;
        PreparedStatement stmt = null;

        try {
            String sql = "UPDATE departamento SET ";

            if (departamento.getNombre().equals("")) {
                sql += "nombre = ?, ";
            }
            if (departamento.getJefe().getId().toString().equals("")) {
                sql += "jefe = ?, ";
            }
            
            // Elimina la coma final y agrega la condición WHERE
            sql = sql.substring(0, sql.length() - 2) + " WHERE id = ?";

            stmt = MenuPrincipal.conn.prepareStatement(sql);

            int parameterIndex = 1;

            if (!departamento.getNombre().equals("")) {
                stmt.setString(parameterIndex++, departamento.getNombre());
            }
            if (!departamento.getJefe().getId().toString().equals("")) {
                stmt.setString(parameterIndex++, departamento.getJefe().getId().toString());
            }

            // Establece el ID del empleado a actualizar
            stmt.setString(parameterIndex, departamento.getId().toString());

            // Ejecuta la consulta SQL con los valores actualizados y el ID
            filasAfectadas = stmt.executeUpdate();

        } catch (SQLException e) {
            // Maneja excepciones
        } 
        
        return filasAfectadas;
    }

	@Override
	public int delete(Departamento t) {
		// TODO Auto-generated method stub
		return 0;
	}

}
