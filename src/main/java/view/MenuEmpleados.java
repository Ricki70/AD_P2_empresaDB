package view;

import java.util.List;

import dao.DaoEmpleado;
import io.IO;

public class MenuEmpleados {
	public static void mostrarMenu() {
		DaoEmpleado empleado = new DaoEmpleado();
		
		List<String> opciones = List.of( 
				" =======|MENU EMPLEADOS|========\n",
				"| 1.- Listar Empleados	|\n", 
				"| 2.- Agregar Empleado	|\n", 
				"| 3.- Modificar Empleado 	|\n",
				"| 4.- Eliminar Empleado	|\n", 
				"| 5.- Volver al menu principal|\n", 
				"===============================\n"		
				);
		
		while (true) {
			opciones.stream().forEach(System.out :: print);
			System.out.print("\nIntroduce tu eleccion: ");
			switch (IO.readInt()) {
				case 1:
					listarEmpleados(empleado);
					break;
				case 2:
					insertEmpleado(empleado);
					break;
				case 3:
					updateEmpleado(empleado);
					break;
				case 4:
					deleteEmpleado(empleado);
					break;
				case 5:
					MenuPrincipal.main(null);
					break;
				default:
			}
		}
    }

	private static void listarEmpleados(DaoEmpleado empleado) {
		
	}

	private static void insertEmpleado(DaoEmpleado empleado) {
		
		
	}

	private static void updateEmpleado(DaoEmpleado empleado) {
		
		
	}
	
	private static void deleteEmpleado(DaoEmpleado empleado) {
		
		
	}
}
