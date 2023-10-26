package view;

import java.util.List;
import java.util.UUID;

import constantes.color.Colores;
import dao.DaoDepartamento;
import io.IO;
import model.Departamento;
import model.Empleado;

public class MenuDepartamentos {
	
	/**
	 * Método para mostrar el menú de departamento y gestionar la opción elegida.
	 */
	public static void mostrarMenu() {
		DaoDepartamento daoDepartamento = new DaoDepartamento();

		List<String> opciones = List.of(
				"\n ======|MENU DEPARTAMENTOS|=====\n", 
				"| 1.- Listar Departamentos	|\n",
				"| 2.- Agregar Departamento	|\n", 
				"| 3.- Modificar Departamento    |\n",
				"| 4.- Eliminar Departamento     |\n", 
				"| 5.- Volver al menu principal  |\n",
				" ===============================\n");

		while (true) {
			opciones.stream().forEach(System.out::print);
			switch (IO.readInt("\nIntroduce tu elección: ")) {
				case 1:
					listarDepartamentos(daoDepartamento);
					break;
				case 2:
					insertDepartamento(daoDepartamento);
					break;
				case 3:
					updateDepartamento(daoDepartamento);
					break;
				case 4:
					deleteDepartamento(daoDepartamento);
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
	 * Método para listar los departamentos al usuario.
	 * 
	 * @param daoDepartamento
	 */
	private static void listarDepartamentos(DaoDepartamento daoDepartamento) {
		IO.print(daoDepartamento.listar());
	}

	/**
	 * Método para solicitar campos de un departamento e insertarlo en la base de datos.
	 * 
	 * @param daoDepartamento
	 */
	private static void insertDepartamento(DaoDepartamento daoDepartamento) {
		// Obtenemos los datos del departamento que se quiere insertar
		String nombre = IO.readString("Nombre ? ");
		UUID jefe = IO.readUUIDOptional("Jefe ? ");

		// Creamos el departamento y lo insertamos
		Departamento departamento = new Departamento(nombre, new Empleado(jefe));
		
		// Comprobamos si se ha insertado el registro y damos feedback
		IO.println(daoDepartamento.insert(departamento) ? "Insertado correctamente" :
				Colores.ROJO 
				+ "No se ha encontrado un empleado con el ID introducido" 
				+ Colores.RESET);
	}

	/**
	 * Método para solicitar nuevos campos de un departamento y actualizarlo en la base de datos.
	 * 
	 * @param daoDepartamento
	 */
	private static void updateDepartamento(DaoDepartamento daoDepartamento) {
		// Obtenemos los datos del departamento que se quiere modificar
		UUID id = IO.readUUID("ID ? ");
		String nombre = IO.readStringOptional("Nombre ? ");
		UUID jefe = IO.readUUIDOptional("Jefe ? ");

		// Creamos el departamento y lo modificamos
		Departamento departamento = new Departamento(id, nombre, new Empleado(jefe));
		IO.println(daoDepartamento.update(departamento) ? "Actualizado correctamente"
				: Colores.ROJO 
				+ "\nRegistro no encontrado o Información no válida\n" 
				+ "Asegúrese de:\n"
				+ "- Haber rellenado al menos 1 campo\n"
				+ "- Que el ID del empleado a modificar exista en la tabla empleado\n"
				+ "- Que el ID del departamento exista en la tabla departamento" 
				+ Colores.RESET);
	}

	/**
	 * Método para eliminar un departamento de la base de datos dado su ID.
	 * 
	 * @param daoDepartamento
	 */
	private static void deleteDepartamento(DaoDepartamento daoDepartamento) {
		// Obtenemos el ID del departamento que se quiere eliminar
		UUID id = IO.readUUID("ID ? ");

		// Creamos el departamento y lo eliminamos
		Departamento departamento = new Departamento(id);
		IO.println(daoDepartamento.delete(departamento) ? "Eliminado correctamente" :
				Colores.ROJO 
				+ "Registro no encontrado o información no válida" 
				+ Colores.RESET);
	}
}