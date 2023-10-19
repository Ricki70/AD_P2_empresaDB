package model;

import java.util.UUID;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Departamento {
	private UUID id;
	private String nombre;
	private Empleado jefe;

	public Departamento(String nombre, Empleado jefe) {
		setId(UUID.randomUUID());
		setNombre(nombre);
		setJefe(jefe);
	}
	
	public Departamento(UUID id) {
		setId(id);
	}
	
	@Override
	public String toString() {
		return this.id + " | " + this.nombre + " | Jefe: " + this.jefe.getNombre() + " (" + this.jefe.getId() + ")"; 
	}
}