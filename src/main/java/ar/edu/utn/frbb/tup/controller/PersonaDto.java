package ar.edu.utn.frbb.tup.controller;

import java.time.LocalDate;
import java.time.Period;

public class PersonaDto {
    private String nombre;
    private String apellido;
    private long dni;
    private String fechaNacimiento;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public long getDni() {
        return dni;
    }

    public void setDni(long dni) {
        this.dni = dni;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

     public int getEdad() {
        LocalDate currentDate = LocalDate.now();
        Period agePeriod = Period.between(LocalDate.parse(fechaNacimiento), currentDate);
        return agePeriod.getYears();
    }

}
