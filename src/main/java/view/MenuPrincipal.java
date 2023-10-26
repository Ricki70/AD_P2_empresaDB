package view;

import java.sql.Connection;
import java.util.List;

import constantes.color.Colores;
import dao.SingletonConexion;
import io.IO;

public class MenuPrincipal {
	/**
	 * Conexión a la base de datos
	 */
	public static Connection conn = SingletonConexion.getConnection();
	
	/**
	 * Método principal que muestra el menú principal.
	 * @param args
	 */
	public static void main(String[] args) {	
		List<String> opciones = List.of( 
				"\n =======|MENU PRINCIPAL|========\n",
				"| 1.- Gestionar Departamentos	 |\n", 
				"| 2.- Gestionar Empleados        |\n", 
				"| 3.- Salir			 |\n",
				" ===============================\n"		
				);

		while (true) {
			opciones.stream().forEach(System.out :: print);
			switch (IO.readInt("\nIntroduce tu elección: ")) {
				case 1:  // menú departamentos
					MenuDepartamentos.mostrarMenu();
					break;
				case 2:  // menú empleados
					MenuEmpleados.mostrarMenu();
					break;
				case 3:  // salir del menú
					IO.println("\nHas salido del menú");
					SingletonConexion.closeConnection();  // cerramos conexión a la base de datos
					System.exit(1);
				default:
					IO.println(Colores.ROJO + "Opción no válida" + Colores.RESET);
			}
		}

	}

}
