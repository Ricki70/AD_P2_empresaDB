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
	private LocalDate fecha_nacimiento;
	private Departamento departamento;

	public Empleado(String nombre, Double salario, LocalDate fecha_nacimiento, Departamento departamento) {
		setId(UUID.randomUUID());
		setNombre(nombre);
		setSalario(salario);
		setFecha_nacimiento(fecha_nacimiento);
		setDepartamento(departamento);
	}
}
