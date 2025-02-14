package ar.edu.utn.frbb.tup.model;

public class Cuota {
    private int numeroCuota;
    private double monto;

    public Cuota(int cuotaNro, double monto){
        this.numeroCuota = cuotaNro;
        this.monto = monto;
    }

    public int getNumeroCuota() {
        return numeroCuota;
    }
    public void setNumeroCuota(int numeroCuota) {
        this.numeroCuota = numeroCuota;
    }

    public double getMonto() {
        return monto;
    }
    public void setMonto(double monto) {
        this.monto = monto;
    }

}
