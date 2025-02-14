package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.model.Prestamo;
import ar.edu.utn.frbb.tup.model.TipoMoneda;

import jakarta.validation.constraints.NotNull;

public class PrestamoDto {
    @NotNull(message = "Debe ingresar el numero del cliente")
    private long numeroCliente;
    private int numeroPrestamo;
    @NotNull(message = "Debe ingresar el plazo en el que desea pagar")
    private int plazoMeses;
    @NotNull(message = "Debe establecer el monto a solicitar")
    private double montoPrestamo;
    @NotNull(message = "Debe establecer la moneda del prestamo")
    private String moneda;

    public PrestamoDto() {}
     public PrestamoDto(Prestamo prestamo){
        this.setNumeroCliente(prestamo.getNumeroCliente());
        this.setNumeroPrestamo(prestamo.getNumeroPrestamo());
        this.setPlazoMeses(prestamo.getPlazoMeses());
        this.setMontoPrestamo(prestamo.getMontoPrestamo());
        this.setMoneda(prestamo.getMoneda());
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

    public int getPlazoMeses() {
        return plazoMeses;
    }
    public void setPlazoMeses(int plazoMeses) {
        this.plazoMeses = plazoMeses;
    }

    public String getMonedaString() {
        return moneda;
    }
    public TipoMoneda getMoneda() {
        TipoMoneda monedaTipo = null;
        if (this.moneda.equals("PESOS")){
            monedaTipo=TipoMoneda.PESOS;
        }
        if (this.moneda.equals("DOLARES")){
            monedaTipo=TipoMoneda.DOLARES;
        }
        return monedaTipo;
    }
    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }
    public void setMoneda(TipoMoneda moneda){
        if (moneda==TipoMoneda.PESOS){
            this.moneda="PESOS";
        }
        if (moneda==TipoMoneda.DOLARES){
            this.moneda="DOLARES";
        }
    }

    public double getMontoPrestamo() {
        return montoPrestamo;
    }
    public void setMontoPrestamo(double montoPrestamo) {
        this.montoPrestamo = montoPrestamo;
    }
}
