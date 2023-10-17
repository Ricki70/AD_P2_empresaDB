package view;

import java.util.List;

import io.IO;

public class MenuGestionDepartamentos {
	public static void mostrarMenu() {
		List<String> opciones = List.of( 
				" ======|MENU DEPARTAMENTOS|=====\n",
				"| 1.- Listar Departamentos	|\n", 
				"| 2.- Agregar Departamento	|\n", 
				"| 3.- Modificar Departamento  |\n",
				"| 4.- Eliminar Departamento   |\n", 
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
