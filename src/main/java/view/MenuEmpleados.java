package view;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import constantes.color.Colores;
import dao.DaoEmpleado;
import io.IO;
import model.Departamento;
import model.Empleado;

public class MenuEmpleados {
	
	/**
	 * Método para mostrar el menú de empleado y gestionar la opción elegida.
	 */
	public static void mostrarMenu() {
		DaoEmpleado daoEmpleado = new DaoEmpleado();

		List<String> opciones = List.of(
				"\n =======|MENU EMPLEADOS|========\n", 
				"| 1.- Listar Empleados	        |\n",
				"| 2.- Agregar Empleado	        |\n",
				"| 3.- Modificar Empleado 	|\n",
				"| 4.- Eliminar Empleado	     	|\n",
				"| 5.- Volver al menu principal  |\n", 
				" ===============================\n");

		while (true) {
			opciones.stream().forEach(System.out::print);
			switch (IO.readInt("\nIntroduce tu elección: ")) {
				case 1:
					listarEmpleados(daoEmpleado);
					break;
				case 2:
					insertEmpleado(daoEmpleado);
					break;
				case 3:
					updateEmpleado(daoEmpleado);
					break;
				case 4:
					deleteEmpleado(daoEmpleado);
					break;
				case 5:
					MenuPrincipal.main(null);
					break;
				default:
					IO.println(Colores.ROJO + "Opción no válida" + Colores.RESET);
			}
		}
	}

	/**
	 * Método para listar los empleados al usuario.
	 * 
	 * @param daoEmpleado
	 */
	private static void listarEmpleados(DaoEmpleado daoEmpleado) {
		IO.print(daoEmpleado.listar());
	}

	/**
	 * Método para solicitar campos de un empleado e insertarlo en la base de datos.
	 * 
	 * @param daoEmpleado
	 */
	private static void insertEmpleado(DaoEmpleado daoEmpleado) {
		// Obtenemos los datos del empleado que se quiere insertar
		String nombre = IO.readString("Nombre ? ");
		Double salario = IO.readDouble("Salario ? ");
		LocalDate nacido = IO.readLocalDate("Fecha de nacimiento ? "); 
		UUID departamento = IO.readUUIDOptional("ID del departamento ? "); 

		// Creamos el empleado y lo insertamos
		Empleado empleado = new Empleado(nombre, salario, nacido, new Departamento(departamento));
		
		// Comprobamos si se ha insertado el registro y damos feedback
		IO.println(daoEmpleado.insert(empleado) ? "Insertado correctamente" : 
			Colores.ROJO 
			+ "\nNo se ha podido insertar el empleado\n" 
			+ Colores.RESET);
	}

	/**
	 * Método para solicitar nuevos campos de un empleado y actualizarlo en la base de datos.
	 * 
	 * @param daoEmpleado
	 */
	private static void updateEmpleado(DaoEmpleado daoEmpleado) {
		// Obtenemos los datos del empleado que se quiere modificar
		UUID id = IO.readUUID("ID ? ");  
		String nombre = IO.readStringOptional("Nombre ? ");
		Double salario = IO.readDoubleOptional("Salario ? ");
		LocalDate nacido = IO.readLocalDateOptional("Fecha de nacimiento ? ");
		UUID departamento = IO.readUUIDOptional("ID del departamento ? ");  

		// Creamos el empleado y lo modificamos
		Empleado empleado = new Empleado(id, nombre, salario, nacido, new Departamento(departamento));
		IO.println(daoEmpleado.update(empleado) ? "Actualizado correctamente" : 
				Colores.ROJO + 
				"\nRegistro no encontrado o informacion no valida\n"
				+ "Asegúrese de:\n"
				+ "- Haber rellenado al menos 1 campo\n"
				+ "- Que el ID del empleado a modificar exista en la tabla empleado\n"
				+ "- Que el ID del departamento exista en la tabla departamento" 
				+ Colores.RESET);
	}

	/**
	 * Método para eliminar un empleado de la base de datos dado su ID.
	 * 
	 * @param daoEmpleado
	 */
	private static void deleteEmpleado(DaoEmpleado daoEmpleado) {
		// Obtenemos el ID del empleado que se quiere eliminar
		UUID id = IO.readUUID("ID ? ");

		// Creamos el empleado y lo eliminamos
		Empleado empleado = new Empleado(id);
		IO.println(daoEmpleado.delete(empleado) ? "Eliminado correctamente" : 
			Colores.ROJO 
			+ "Registro no encontrado o informacion no valida\n" 
			+ Colores.RESET);
	}
}
