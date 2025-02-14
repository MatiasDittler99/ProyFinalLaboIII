package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.model.Cuota;
import ar.edu.utn.frbb.tup.model.PrestamoOutput;

import java.util.ArrayList;
import java.util.List;

public class PrestamoOutputDto {
    private Long numeroCliente;
    private int numeroPrestamo;
    private String estado;
    private String mensaje;
    private List<Cuota> planPagos = new ArrayList<>();

    public PrestamoOutputDto() {}
    public PrestamoOutputDto(PrestamoOutput prestamoOutput){
        this.setNumeroCliente(prestamoOutput.getNumeroCliente());
        this.setNumeroPrestamo(prestamoOutput.getNumeroPrestamo());
        this.setEstado(prestamoOutput.getEstado());
        this.setMensaje(prestamoOutput.getMensaje());
        this.setPlanPagos(prestamoOutput.getPlanPagos());
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


}
