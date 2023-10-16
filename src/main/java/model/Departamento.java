package model;

import java.util.UUID;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Departamento {
	private UUID id;
	private String nombre;
	private String jefe;
	
	public Departamento(String nombre, String jefe) {
			setId(UUID.randomUUID());
			setNombre(nombre);
			setJefe(jefe);
	}
}