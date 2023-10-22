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
	
	public Departamento(UUID id, String nombre) {
		setId(id);
		setNombre(nombre);
	}
	
	public Departamento(UUID id) {
		setId(id);
	}
	
	@Override
	public String toString() {
			String format = "[ %-36s ][ %-15s ][ %-10s ]";
		    String departamentoStr = (this.jefe != null) ? this.jefe.getNombre() + " (" + this.jefe.getId().toString() + ")" : "NULL";
		    return String.format(format, this.id.toString(), this.nombre, departamentoStr);
		}
}