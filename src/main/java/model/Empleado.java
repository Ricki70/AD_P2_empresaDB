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
	
	@Override
	public String toString() {
//		PRIMER INTENTO DE ESTRUCTURAR LOS DATOS
//		String objDepartamento = (this.departamento == null) ? " | NULL | " : " | " + this.nacido + " | " + this.departamento.getNombre() + " (" + this.departamento.getId().toString() + ") ";
//		return this.id + " | " + this.nombre + " | " + this.salario + objDepartamento; 
		
		String format = "[ %-36s ][ %-25s ][ %-8s ][ %-10s ][ %-30s ]%n";
	    String departamentoStr = (this.departamento != null) ?  " ][ " + this.departamento.getNombre() + " (" + this.departamento.getId().toString() + ")" : "NULL";
	    
	    return String.format(format, this.id.toString(), this.nombre, this.salario.toString(), this.nacido.toString(), departamentoStr);
	}
}
