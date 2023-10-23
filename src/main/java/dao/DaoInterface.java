package dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import view.MenuPrincipal;

/**
 * Interfaz con los métodos necesarios para realizar operaciones CRUD sobre la base de datos.
 * @param <T> tipo de objeto genérico (representa Empleado o Departamento)
 */
public interface DaoInterface<T> {
	/**
	 * Método para listar los registros de tabla.
	 * 
	 * @return 
	 */
	public String listar();
	
	/**
	 * Método para insertar un registro en la tabla.
	 * 
	 * @param t registro que se quiere insertar
	 * @return número de filas afectadas
	 */
	public Boolean insert(T t);
	
	/**
	 * Método para actualizar un registro de la tabla.
	 * 
	 * @param t registro que se quiere modificar
	 * @return número de filas afectadas
	 */
	public Boolean update(T t);
	
	/**
	 * Método para eliminar un registro de la tabla.
	 * 
	 * @param t registro que se quiere eliminar
	 * @return número de filas afectadas
	 */
	public Boolean delete(T t);
	
	/**
	 * Método que comprueba si existe un empleado dado su ID.
	 * 
	 * @param empleadoID id por el que buscamos al empleado
	 * @return true si existe el empleado, false en caso contrario
	 * @throws SQLException excepción tratada en los métodos que llaman a este método
	 */
	public static Boolean existeEmpleado(String empleadoID) throws SQLException {
		String sql = "SELECT * FROM empleado WHERE id = ?";
		PreparedStatement pstmt = MenuPrincipal.conn.prepareStatement(sql);
		pstmt.setString(1, empleadoID);
		return pstmt.executeQuery().next();  // devuelve si hay registros o no
	}
	
	/**
	 * Método que comprueba si existe un departamento dado su ID.
	 * 
	 * @param departamentoID id por el que buscamos el departamento
	 * @return true si existe el departamento, false en caso contrario
	 * @throws SQLException excepción tratada en los métodos que llaman a este método
	 */
	public static Boolean existeDepartamento(String departamentoID) throws SQLException {
		String sql = "SELECT * FROM departamento WHERE id = ?";
		PreparedStatement pstmt = MenuPrincipal.conn.prepareStatement(sql);
		pstmt.setString(1, departamentoID);
		return pstmt.executeQuery().next();  // devuelve si hay registros o no
	}
	
	/**
	 * Método privado para resetear a NULL el jefe de un departamento
	 * 
	 * @param empleadoID id del jefe del departamento
	 * @return true si se ha podido modificar, false en caso contrario
	 * @throws SQLException excepción tratada en los métodos que llaman a este método
	 */
	public static Boolean actualizarJefes(String empleadoID) throws SQLException {
		String sql = "UPDATE departamento SET jefe = NULL WHERE jefe = ?";
		PreparedStatement pstmt = MenuPrincipal.conn.prepareStatement(sql);
		pstmt.setString(1, empleadoID);
		return pstmt.executeUpdate() > 0;  // devuelve si hay filas afectadas o no
	}
}
