package view;

import java.util.List;
import java.util.UUID;

import constantes.color.Colores;
import dao.DaoDepartamento;
import io.IO;
import model.Departamento;
import model.Empleado;

public class MenuDepartamentos {
	public static void mostrarMenu() {
		DaoDepartamento daoDepartamento = new DaoDepartamento();

		List<String> opciones = List.of(
				"\n\n ======|MENU DEPARTAMENTOS|=====\n", 
				"| 1.- Listar Departamentos	|\n",
				"| 2.- Agregar Departamento	|\n", 
				"| 3.- Modificar Departamento    |\n",
				"| 4.- Eliminar Departamento     |\n", 
				"| 5.- Volver al menu principal  |\n",
				" ===============================\n");

		while (true) {
			opciones.stream().forEach(System.out::print);
			IO.print("\nIntroduce tu eleccion: ");
			switch (IO.readInt()) {
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
			}
		}
	}

	private static void listarDepartamentos(DaoDepartamento daoDepartamento) {
		IO.print(daoDepartamento.listar());
	}

	private static void insertDepartamento(DaoDepartamento daoDepartamento) {
		// Obtenemos los datos del departamento que se quiere insertar
		IO.print("Nombre ? ");
		String nombre = IO.readStringNoEmpty();
		IO.print("Jefe ? ");
		UUID jefe = IO.readUUID(); 

		// Creamos el departamento y lo insertamos
		Departamento departamento = new Departamento(nombre, new Empleado(jefe));
		
		// Comprobamos si se ha insertado el registro
		boolean insertado = daoDepartamento.insert(departamento);
		IO.print(insertado ? "Insertado correctamente" : Colores.ROJO + "No se ha podido insertar el departamento" + Colores.RESET);
	}

	private static void updateDepartamento(DaoDepartamento daoDepartamento) {
		// Obtenemos los datos del departamento que se quiere modificar
		IO.print("ID ? ");
		UUID id = IO.readUUID();
		IO.print("Nombre ? ");
		String nombre = IO.readString();
		IO.print("Jefe ? ");
		UUID jefe = IO.readUUIDOpcional(); 

		// Creamos el departamento y lo actualizamos
		Departamento departamento = new Departamento(id, nombre, new Empleado(jefe));
		IO.println(daoDepartamento.update(departamento) ? "Actualizado Correctamente" : Colores.ROJO + "Registro no encontrado o Informacion no valida" + Colores.RESET);
	}

	
	private static void deleteDepartamento(DaoDepartamento daoDepartamento) {
		

	}
}