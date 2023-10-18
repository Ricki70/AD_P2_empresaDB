package view;

import java.sql.Connection;
import java.util.List;

import dao.SingletonConexion;
import io.IO;

public class MenuPrincipal {

	public static Connection conn = SingletonConexion.getConnection();
	
	public static void main(String[] args) {
		List<String> opciones = List.of( 
				" =======|MENU PRINCIPAL|========\n",
				"| 1.- Gestionar Departamentos	 |\n", 
				"| 2.- Gestionar Empleados        |\n", 
				"| 3.- Salir			 |\n",
				" ===============================\n"		
				);

		while (true) {
			opciones.stream().forEach(System.out :: print);
			System.out.print("\nIntroduce tu eleccion: ");
			switch (IO.readInt()) {
				case 1:  // menú departamentos
					MenuDepartamentos.mostrarMenu();
					break;
				case 2:  // menú empleados
					MenuEmpleados.mostrarMenu();
					break;
				case 3:  // salir del menú
					System.out.println("\nHas salido del menú");
					SingletonConexion.closeConnection();
					return;
				default:
					System.out.println("Opción no válida");
			}
		}

	}

}
