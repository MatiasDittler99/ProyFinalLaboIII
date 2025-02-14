// Separar en dos clases PrestamoCliente PrestamoCuotas Comentario de Javier
package ar.edu.utn.frbb.tup.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import ar.edu.utn.frbb.tup.controller.PrestamoOutputDto;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PrestamoOutput {
    private Long numeroCliente;
    private int numeroPrestamo;
    private String estado;
    private String mensaje;
    private List<Cuota> planPagos = new ArrayList<>();

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double interesTotal;

    public PrestamoOutput(){}
     public PrestamoOutput(PrestamoOutputDto prestamoOutputDto) {
        this.numeroCliente = prestamoOutputDto.getNumeroCliente();
        this.numeroPrestamo = prestamoOutputDto.getNumeroPrestamo();
        this.estado = prestamoOutputDto.getEstado();
        this.mensaje = prestamoOutputDto.getMensaje();
        this.planPagos = prestamoOutputDto.getPlanPagos();
    }

    public long getNumeroCliente() {
        return numeroCliente;
    }
    public void setNumeroCliente(Long numeroCliente) {
        this.numeroCliente = numeroCliente;
    }

    
    public int getNumeroPrestamo() {
        return numeroPrestamo;
    }
    public void setNumeroPrestamo(int numeroPrestamo) {
        this.numeroPrestamo = numeroPrestamo;
    }

    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }


    public String getMensaje() {
        return mensaje;
    }
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public List<Cuota> getPlanPagos() {
        return planPagos;
    }
    public void setPlanPagos(List<Cuota> planPagos) {
        this.planPagos = planPagos;
    }
    public void addCuota(Cuota cuota) {
        planPagos.add(cuota);
    }

}
