package model;

import java.time.LocalDate;
import java.util.UUID;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Empleado {
	private UUID id;
	private String nombre;
	private Double salario;
	private LocalDate nacido;
	private Departamento departamento;

	public Empleado(String nombre, Double salario, LocalDate nacido, Departamento departamento) {
		setId(UUID.randomUUID());
		setNombre(nombre);
		setSalario(salario);
		setNacido(nacido);
		setDepartamento(departamento);
	}
	
		public Empleado(UUID id) {
			setId(id);
		}
}
