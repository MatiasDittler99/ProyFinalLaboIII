package ar.edu.utn.frbb.tup.persistence.entity;

import ar.edu.utn.frbb.tup.model.Cuota;
import ar.edu.utn.frbb.tup.model.PrestamoOutput;

import java.util.ArrayList;
import java.util.List;

public class PrestamoOutputEntity extends BaseEntity{
    private long numeroCliente;
    private int numeroPrestamo;
    private String estado;
    private String mensaje;
    private List<Cuota> planPagos = new ArrayList<>();

    public PrestamoOutputEntity(PrestamoOutput prestamoOutput) {
        super(Long.parseLong(prestamoOutput.getNumeroCliente()+""+prestamoOutput.getNumeroPrestamo()));
        this.numeroCliente = prestamoOutput.getNumeroCliente();
        this.numeroPrestamo = prestamoOutput.getNumeroPrestamo();
        this.estado = prestamoOutput.getEstado();
        this.mensaje = prestamoOutput.getMensaje();
        addCuotasPrestamo(prestamoOutput);
    }

    public PrestamoOutput toPrestamoOutput(){
        PrestamoOutput prestamoOutput = new PrestamoOutput();
        prestamoOutput.setNumeroCliente(this.numeroCliente);
        prestamoOutput.setNumeroPrestamo(this.numeroPrestamo);
        prestamoOutput.setEstado(this.estado);
        prestamoOutput.setMensaje(this.mensaje);
        prestamoOutput.setPlanPagos(this.planPagos);
        return prestamoOutput;
    }

    private void addCuotasPrestamo(PrestamoOutput prestamoOutput){
        if (prestamoOutput.getPlanPagos() != null && !(prestamoOutput.getPlanPagos().isEmpty())){
            planPagos.addAll(prestamoOutput.getPlanPagos());
        }
    }

    public long getNumeroCliente() {
        return numeroCliente;
    }


}
