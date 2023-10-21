package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
	
	public Empleado(UUID id, String nombre) {
		setId(id);
		setNombre(nombre);
	}

	public Empleado(UUID id) {
		setId(id);
	}
	
	@Override
	public String toString() {
		String format = "[ %-36s ][ %-15s ][ %-8s ][ %-10s ][ %-10s ]";
	    String departamentoStr = (this.departamento != null) ? this.departamento.getNombre() + " (" + this.departamento.getId().toString() + ")" : "NULL";
	    return String.format(format, this.id.toString(), this.nombre, this.salario.toString(), this.getNacido().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")), departamentoStr);
	}
}
