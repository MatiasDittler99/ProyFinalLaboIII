package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.PrestamoConsultaDto;
import ar.edu.utn.frbb.tup.controller.PrestamoOutputDto;
import ar.edu.utn.frbb.tup.model.*;
import ar.edu.utn.frbb.tup.controller.PrestamoDto;
import ar.edu.utn.frbb.tup.model.exception.PrestamoNoOtorgadoException;
import ar.edu.utn.frbb.tup.persistence.PrestamoDao;
import ar.edu.utn.frbb.tup.persistence.PrestamoOutputDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PrestamoService {

    @Autowired
    ClienteService clienteService;

    @Autowired
    CuentaService cuentaService;

    @Autowired
    PrestamoDao prestamoDao;

    @Autowired
    PrestamoOutputDao prestamoOutputDao;

    public PrestamoOutputDto pedirPrestamo(PrestamoDto prestamoDto) throws PrestamoNoOtorgadoException {
        Prestamo prestamo = new Prestamo(prestamoDto);
        PrestamoOutput prestamoOutput = new PrestamoOutput();
        prestamoOutput.setNumeroCliente(prestamo.getNumeroCliente());
        validator(prestamo);
        prestamoOutput.setEstado(calcularScoring(prestamo.getNumeroCliente()));
        establecerMensajeScoring(prestamoOutput);
        if(prestamoOutput.getEstado().equals("RECHAZADO")){
            return output(prestamoOutput);
        }

        establecerNumeroPrestamo(prestamo, prestamoOutput);
        prestamo.setInteresTotal(calculaIntereses(prestamo.getMontoPrestamo(), 5));
        CuotaService.generarCuotas(prestamo, prestamoOutput);
        prestamoDao.almacenarDatosPrestamo(prestamo);
        prestamoOutputDao.almacenarDatosPrestamoOutput(prestamoOutput);
        cuentaService.actualizarCuentaCliente(prestamo);

        return output(prestamoOutput);
    }

    double calculaIntereses(double monto, int valorInteres){
        return monto * ((double) valorInteres /12);
    }

    private String calcularScoring(Long dni){
        ScoreCrediticioService scoreCrediticioService = new ScoreCrediticioService();
        boolean valido = scoreCrediticioService.verificarScore(dni);
        if (!valido){
            return "RECHAZADO";
        }
        return "APROBADO";
    }
    private void establecerMensajeScoring(PrestamoOutput prestamoOutput){
        if (prestamoOutput.getEstado().equals("RECHAZADO")){
            prestamoOutput.setMensaje("No posee el scoring suficiente para pedir un prestamo de este tipo");
        }
        if(prestamoOutput.getEstado().equals("APROBADO")){
            prestamoOutput.setMensaje("El monto del prestamo fue acreditado a su cuenta");
        }
    }
    private PrestamoOutputDto output(PrestamoOutput prestamoOutput){
        return new PrestamoOutputDto(prestamoOutput);
    }

    private Cuenta getCuentaPermitida(Prestamo prestamo){
        List<Cuenta> cuentas = clienteService.getCuentasCliente(prestamo.getNumeroCliente());
        for (Cuenta c : cuentas){
            if (c.getMoneda().equals(prestamo.getMoneda())){
                return c;
            }
        }
        return null;
    }
    public void validator(Prestamo prestamo) throws PrestamoNoOtorgadoException {
        if ((getCuentaPermitida(prestamo)) == null){
            throw new PrestamoNoOtorgadoException("No posee una cuenta en esa moneda");
        }

        if (prestamo.getMontoPrestamo() >= 1000000){
            throw new PrestamoNoOtorgadoException("El monto a solicitar es mayor al que se le puede ofrecer en este momento");
        }

        if((getPrestamosCliente((prestamo.getNumeroCliente())).size() > 3 )){
            throw new PrestamoNoOtorgadoException("Es deudor de 3 prestamos. Finalice el pago de los mencionados antes de solicitar otro prestamo");
        }

        try {
            Cliente cliente = clienteService.buscarClientePorDni(prestamo.getNumeroCliente());
        } catch (IllegalArgumentException e) {
            throw new PrestamoNoOtorgadoException(e.getMessage());
        }

    }

    public PrestamoConsultaDto pedirConsultaPrestamos(long dni) {
        PrestamoConsultaDto consulta = new PrestamoConsultaDto(dni);

        List<Prestamo> prestamosCliente = getPrestamosCliente(dni);
        if(prestamosCliente.isEmpty()){
            throw new IllegalArgumentException("El cliente "+dni+" no ha pedido prestamos");
        }

        List<PrestamoConsultaCliente> prestamos = new ArrayList<PrestamoConsultaCliente>();
        for (Prestamo p : prestamosCliente) {
            PrestamoConsultaCliente prestamoCliente = new PrestamoConsultaCliente(p);
            PrestamoOutput prestamoOutput = getPrestamosOutput(p);
            prestamoCliente.setPagosRealizados(prestamoOutput.getPlanPagos().size()-1);
            prestamoCliente.setSaldoRestante(calcularSaldoRestante(p, prestamoOutput, prestamoCliente.getPagosRealizados()));
            prestamos.add(prestamoCliente);
        }
        consulta.setPrestamos(prestamos);

        return consulta;
    }

    List<Prestamo> getPrestamosCliente(long dni){
        return prestamoDao.getPrestamosByCliente(dni);
    }

    List<PrestamoOutput> getPrestamosOutputCliente(long dni){
        return prestamoOutputDao.getPrestamosOutputByCliente(dni);
    }

    PrestamoOutput getPrestamosOutput(Prestamo prestamo){
        List<PrestamoOutput> prestamosOutput = prestamoOutputDao.getPrestamosOutputByCliente(prestamo.getNumeroCliente());
        for (PrestamoOutput pOutput : prestamosOutput){
            if (prestamo.getNumeroPrestamo()==pOutput.getNumeroPrestamo()){
                return pOutput;
            }
        }
        throw new IllegalStateException("No se encontró un PrestamoOutput válido");
    }

    private double calcularSaldoRestante(Prestamo prestamo, PrestamoOutput prestamoOutput, int pagosRealizados) {
        double saldoTotal = prestamo.getMontoPrestamo() + prestamo.getInteresTotal();
        double saldoActual = (prestamoOutput.getPlanPagos().get(0).getMonto()) * pagosRealizados;

        return saldoTotal - saldoActual;
    }

    private void establecerNumeroPrestamo(Prestamo prestamo, PrestamoOutput prestamoOutput){
        List<Prestamo> prestamos = getPrestamosCliente(prestamo.getNumeroCliente());
        int numeroPrestamo;
        if (prestamos.isEmpty()){
            numeroPrestamo = 1;
        }
        else {
            numeroPrestamo = prestamos.size()+1;
        }
        prestamo.setNumeroPrestamo(numeroPrestamo);
        prestamoOutput.setNumeroPrestamo(numeroPrestamo);
    }

}
