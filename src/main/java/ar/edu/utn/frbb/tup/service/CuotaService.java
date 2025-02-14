package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.model.Prestamo;
import ar.edu.utn.frbb.tup.model.PrestamoOutput;
import ar.edu.utn.frbb.tup.model.Cuota;

// import java.util.Random;

public class CuotaService {

    public static void generarCuotas(Prestamo prestamo, PrestamoOutput prestamoOutput){
        double mensualidad = calcularMontoCuota(prestamo);
        int numeroCuota;
        if (prestamoOutput.getPlanPagos().isEmpty()){
            numeroCuota = 1;
        }
        else {
            numeroCuota = prestamoOutput.getPlanPagos().size()+1;
        }
        Cuota cuota = new Cuota(numeroCuota, mensualidad);
        prestamoOutput.addCuota(cuota);
    }

    static double calcularMontoCuota(Prestamo prestamo){
        return ( prestamo.getMontoPrestamo() + prestamo.getInteresTotal() ) / prestamo.getPlazoMeses();
    }
}
