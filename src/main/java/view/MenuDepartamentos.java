package view;

import java.util.List;

import dao.DaoDepartamento;
import io.IO;

public class MenuDepartamentos {
	public static void mostrarMenu() {
		DaoDepartamento departamento = new DaoDepartamento();

		List<String> opciones = List.of(
				" ======|MENU DEPARTAMENTOS|=====\n", 
				"| 1.- Listar Departamentos	|\n",
				"| 2.- Agregar Departamento	|\n", 
				"| 3.- Modificar Departamento  |\n",
				"| 4.- Eliminar Departamento   |\n", 
				"| 5.- Volver al menu principal|\n",
				"===============================\n");

		while (true) {
			opciones.stream().forEach(System.out::print);
			System.out.print("\nIntroduce tu eleccion: ");
			switch (IO.readInt()) {
			case 1:
				listarDepartamentos(departamento);
				break;
			case 2:
				insertDepartamento(departamento);
				break;
			case 3:
				updateDepartamento(departamento);
				break;
			case 4:
				deleteDepartamento(departamento);
				break;
			case 5:
				MenuPrincipal.main(null);
				break;
			default:
			}
		}
	}

	private static void listarDepartamentos(DaoDepartamento departamento) {

	}
	
	private static void insertDepartamento(DaoDepartamento departamento) {
		

	}
	
	private static void updateDepartamento(DaoDepartamento departamento) {
		

	}
	
	private static void deleteDepartamento(DaoDepartamento departamento) {
		

	}
}