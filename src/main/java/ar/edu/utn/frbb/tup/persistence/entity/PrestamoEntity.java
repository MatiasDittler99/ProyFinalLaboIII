package ar.edu.utn.frbb.tup.persistence.entity;

import ar.edu.utn.frbb.tup.model.Prestamo;
import ar.edu.utn.frbb.tup.model.TipoMoneda;

public class PrestamoEntity extends BaseEntity{
    private long numeroCliente;
    private int numeroPrestamo;
    private Integer plazoMeses;
    private Double montoPrestamo;
    private TipoMoneda moneda;
    private Double interesTotal;

    public PrestamoEntity(Prestamo prestamo) {
        super(Long.parseLong(prestamo.getNumeroCliente()+""+prestamo.getNumeroPrestamo()));
        this.numeroCliente = prestamo.getNumeroCliente();
        this.numeroPrestamo = prestamo.getNumeroPrestamo();
        this.plazoMeses = prestamo.getPlazoMeses();
        this.montoPrestamo = prestamo.getMontoPrestamo();
        this.moneda = prestamo.getMoneda();
        this.interesTotal = prestamo.getInteresTotal();
    }

    public Prestamo toPrestamo(){
        Prestamo prestamo = new Prestamo();
        prestamo.setNumeroCliente(this.numeroCliente);
        prestamo.setNumeroPrestamo(this.numeroPrestamo);
        prestamo.setPlazoMeses(this.plazoMeses);
        prestamo.setMontoPrestamo(this.montoPrestamo);
        prestamo.setMoneda(this.moneda);
        prestamo.setInteresTotal(this.interesTotal);
        return prestamo;
    }

    public long getNumeroCliente() {
        return numeroCliente;
    }

}
