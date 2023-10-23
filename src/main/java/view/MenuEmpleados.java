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
			IO.print("\nIntroduce tu eleccion: ");
			switch (IO.readInt()) {
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
			}
		}
	}

	private static void listarEmpleados(DaoEmpleado daoEmpleado) {
		IO.print(daoEmpleado.listar());
	}

	private static void insertEmpleado(DaoEmpleado daoEmpleado) {
		// Obtenemos los datos del empleado que se quiere insertar
		IO.print("Nombre ? ");
		String nombre = IO.readString();
		IO.print("Salario ? ");
		Double salario = IO.readDouble();
		IO.print("Fecha de nacimiento ? ");
		LocalDate nacido = IO.readLocalDate(); 
		IO.print("ID del departamento ? ");
		UUID departamento = IO.readUUIDOptional(); 

		// Creamos el empleado y lo insertamos
		Empleado empleado = new Empleado(nombre, salario, nacido, new Departamento(departamento));
		
		// Comprobamos si se ha insertado el registro y damos feedback
		IO.print(daoEmpleado.insert(empleado) ? "Insertado correctamente\n" : Colores.ROJO + "No se ha podido insertar el empleado" + Colores.RESET);
	}

	private static void updateEmpleado(DaoEmpleado daoEmpleado) {
		// Obtenemos los datos del empleado que se quiere modificar
		IO.print("ID ? ");
		UUID id = IO.readUUID();  
		IO.print("Nombre ? ");
		String nombre = IO.readStringOptional();
		IO.print("Salario ? ");
		Double salario = IO.readDoubleOptional();
		IO.print("Fecha de nacimiento ? ");
		LocalDate nacido = IO.readLocalDateOptional();
		IO.print("ID del departamento ? ");
		UUID departamento = IO.readUUIDOptional();  

		// Creamos el empleado y lo actualizamos
		Empleado empleado = new Empleado(id, nombre, salario, nacido, new Departamento(departamento));

		//Comprobamos que se actualice y damos feedback
		IO.println(daoEmpleado.update(empleado) ? 
				"\nActualizado Correctamente" 
				: 
				Colores.ROJO + 
				"\nRegistro no encontrado o Informacion no valida\n"
				+ "Asegúrese de:\n"
				+ "- Haber rellenado al menos 1 campo\n"
				+ "- Que el ID del empleado a modificar exista en la tabla empleado\n"
				+ "- Que el ID del departamento exista en la tabla departamento" 
				+ Colores.RESET);
	}

	private static void deleteEmpleado(DaoEmpleado daoEmpleado) {
		// Obtenemos los datos del empleado que se quiere modificar
		IO.print("ID ? ");
		UUID id = IO.readUUID();

		// Creamos el empleado y lo eliminamos
		Empleado empleado = new Empleado(id);
		IO.println(daoEmpleado.delete(empleado) ? "Eliminado Correctamente" : Colores.ROJO + "Registro no encontrado o Informacion no valida" + Colores.RESET);
	}
}
