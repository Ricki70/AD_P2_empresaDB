package view;

import java.util.List;

import dao.GestionEmpleados;
import io.IO;

public class MenuGestionEmpleados {
	public static void mostrarMenu() {
		GestionEmpleados gEmpleado = new GestionEmpleados();
		
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
					
					break;
				case 2:
					
					break;
				case 3:
					
					break;
				case 4:
					
					break;
				case 5:
					MenuPrincipal.main(null);
					break;
				default:
			}
		}
    }
}
