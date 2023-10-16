package view;

import java.util.List;

import io.IO;

public class MenuPrincipal {

	public static void main(String[] args) {
		List<String> opciones = List.of( 
				" =======|MENU PRINCIPAL|========\n",
				"| 1.- Gestionar Departamentos	|\n", 
				"| 2.- Gestionar Empleados	|\n", 
				"| 3.- Salir			|\n",
				"===============================\n"		
				);
		
		while (true) {
			System.out.print(opciones + "\nIntroduce tu eleccion: ");
			switch (IO.readString().toUpperCase().charAt(0)) {
			case '1':
				MenuGestionDepartamentos.mostrarMenu();
				break;
			case '2':
				MenuGestionEmpleados.mostrarMenu();
				break;
			default:
			}
		}

	}

}
