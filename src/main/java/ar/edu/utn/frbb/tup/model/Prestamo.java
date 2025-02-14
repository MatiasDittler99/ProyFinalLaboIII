package ar.edu.utn.frbb.tup.model;

import ar.edu.utn.frbb.tup.controller.PrestamoDto;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Prestamo {
    private Long numeroCliente;
    private int numeroPrestamo;
    private Integer plazoMeses;
    private Double montoPrestamo;
    private TipoMoneda moneda;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double interesTotal;

    public Prestamo(){}
    
    public Prestamo(PrestamoDto prestamoDto) {
        this.numeroCliente = prestamoDto.getNumeroCliente();
        this.numeroPrestamo = prestamoDto.getNumeroPrestamo();
        this.plazoMeses = prestamoDto.getPlazoMeses();
        this.montoPrestamo = prestamoDto.getMontoPrestamo();
        this.moneda = prestamoDto.getMoneda();
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
    public void setPlazoMeses(Integer plazoMeses) {
        this.plazoMeses = plazoMeses;
    }

    public double getMontoPrestamo() {
        return montoPrestamo;
    }
    public void setMontoPrestamo(Double montoPrestamo) {
        this.montoPrestamo = montoPrestamo;
    }

    public TipoMoneda getMoneda() {
        return moneda;
    }
    public void setMoneda(TipoMoneda moneda) {
        this.moneda = moneda;
    }
    public void setMoneda(String moneda){
        if (moneda.equalsIgnoreCase("PESOS")){
            this.moneda=TipoMoneda.PESOS;
        }
        if (moneda.equalsIgnoreCase("DOLARES")){
            this.moneda=TipoMoneda.DOLARES;
        }
    }

    public double getInteresTotal() {
        return interesTotal;
    }
    public void setInteresTotal(Double interesTotal) {
        this.interesTotal = interesTotal;
    }

    @Override
    public String toString() {
        return "Prestamo{" +
                "numeroCliente=" + numeroCliente +
                ", numeroPrestamo=" + numeroPrestamo +
                ", plazoMeses=" + plazoMeses +
                ", montoPrestamo=" + montoPrestamo +
                ", moneda=" + moneda +
                '}';
    }
}
