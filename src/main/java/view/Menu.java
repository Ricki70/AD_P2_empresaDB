package view;

import java.util.List;

import dao.AccesoSQLite;
import io.IO;
import model.Contacto;

public class Menu {
	
	public static void main(String[] args) {
		AccesoSQLite accesoSQLite = new AccesoSQLite();
//		agenda.drop();
		
		List<String> opciones = List.of( 
				"1.- buscar por Código\n", 
				"2.- buscar por Nombre\n", 
				"3.- Mostrar la agenda\n", 
				"4.- Añadir un contacto\n", 
				"5.- Eliminar un contacto\n",
				"6.- Pack\n",
				"7.- Salir\n");
		
		while (true) {
			System.out.println(opciones);
			switch (IO.readString().toUpperCase().charAt(0)) {
			case '1':
				buscarPorCodigo(accesoSQLite);
				break;
			case '2':
				buscarPorInicioDelNombre(accesoSQLite);
				break;
			case '3':
				mostrar(accesoSQLite);
				break;
			case '4':
				anadirContacto(accesoSQLite);
				break;
			case '5':
				borrarContacto(accesoSQLite);
				break;
//			case '6': // Quitar borrados
//				accesoSQLite.pack();
//				break;
			case '7':
				cerrarAgenda(accesoSQLite);
				return;
			default:
			}
		}		
		
	}

	private static void cerrarAgenda(AccesoSQLite accesoSQLite) {
		accesoSQLite.cerrar();
		System.out.println("Agenda Cerrada");
	}

	private static void borrarContacto(AccesoSQLite AccesoSQLite) {
		IO.print("Código ? ");
		String id = IO.readString();
		boolean borrado = AccesoSQLite.delete(id);
		IO.println(borrado ? "Borrado" : "No se ha podido borrar");
	}

	private static void anadirContacto(AccesoSQLite accesoSQLite) {
		IO.print("Nombre ? ");
		String nombre = IO.readString();
		IO.print("Teléfono ? ");
		String telefono = IO.readString();
		IO.print("Edad ? ");
		int edad = IO.readInt();
		boolean anadido = accesoSQLite.add(new Contacto(nombre, telefono, edad));
		IO.println(anadido ? "Añadido" : "No se ha podido añadir");
	}

	private static void mostrar(AccesoSQLite accesoSQLite) {
		System.out.println(accesoSQLite.show());
	}

	private static void buscarPorInicioDelNombre(AccesoSQLite accesoSQLite) {
		IO.print("El nombre empieza por ? ");
		String inicio = IO.readString();
		List<?> contactos = accesoSQLite.buscarPorNombre(inicio);
		IO.println(contactos);
	}

	private static void buscarPorCodigo(AccesoSQLite accesoSQLite) {
		IO.print("Código ? ");
		String id = IO.readString();
		Contacto contacto = accesoSQLite.buscarPorCodigo(id);
		IO.println(contacto);
	}

}
