package dao;

/**
 * Interfaz con los métodos necesarios para realizar operaciones CRUD sobre la base de datos.
 * @param <T> tipo de objeto genérico (representa Empleado o Departamento)
 */
public interface DaoInterface<T> {
	/**
	 * Método para listar los registros de tabla.
	 */
	public void listar();
	
	/**
	 * Método para insertar un registro en la tabla.
	 * @return número de filas afectadas
	 */
	public int insert();
	
	/**
	 * Método para actualizar un registro de la tabla.
	 * @param t registro que se quiere modificar
	 * @return número de filas afectadas
	 */
	public int update(T t);
	
	/**
	 * Método para eliminar un registro de la tabla.
	 * @param t registro que se quiere eliminar
	 * @return número de filas afectadas
	 */
	public int delete(T t);
}
