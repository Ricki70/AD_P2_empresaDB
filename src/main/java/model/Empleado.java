package model;

import java.util.Date;
import java.util.UUID;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Empleado {
	private UUID id;
	private String nombre;
	private Double salario;
	private Date fecha_nacimiento;
	private String departamento;
	
	public Empleado(String nombre, Double salario, Date fecha_nacimiento, String departamento) {
			setId(UUID.randomUUID());
			setNombre(nombre);
			setSalario(salario);
			setFecha_nacimiento(fecha_nacimiento);
			setDepartamento(departamento);
	}
}
