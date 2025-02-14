package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.CuentaController;
import ar.edu.utn.frbb.tup.controller.CuentaDto;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Prestamo;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaNoSoportadaException;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class CuentaService {

    CuentaDao cuentaDao = new CuentaDao();

    @Autowired
    ClienteService clienteService;

    public Cuenta darDeAltaCuenta(CuentaDto cuentaDto) throws CuentaAlreadyExistsException, TipoCuentaAlreadyExistsException, TipoCuentaNoSoportadaException {
        Cuenta cuenta = new Cuenta(cuentaDto);
        if(cuentaDao.find(cuenta.getNumeroCuenta()) != null) {
            throw new CuentaAlreadyExistsException("La cuenta " + cuenta.getNumeroCuenta() + " ya existe.");
        }

       if (!tipoCuentaEstaSoportada(cuenta)) {
            throw new TipoCuentaNoSoportadaException("El tipo de cuenta " + cuenta.getTipoCuenta() + " no esta soportada.");
        }

        clienteService.agregarCuenta(cuenta);
        cuentaDao.save(cuenta);
        return cuenta;
    }

    public boolean tipoCuentaEstaSoportada(Cuenta cuenta) {
        return (cuenta.getTipoCuenta() == TipoCuenta.CUENTA_CORRIENTE && cuenta.getMoneda() == TipoMoneda.PESOS) || (cuenta.getTipoCuenta() == TipoCuenta.CAJA_AHORRO && (cuenta.getMoneda() == TipoMoneda.PESOS || cuenta.getMoneda() == TipoMoneda.DOLARES));
    }

    public Cuenta find(long id) {
        return cuentaDao.find(id);
    }

    public void actualizarCuentaCliente(Prestamo prestamo){
        List<Cuenta> cuentas = clienteService.getCuentasCliente(prestamo.getNumeroCliente());
        boolean prestamoOtorgado=false;
        for (Cuenta c : cuentas){
            if (c.getMoneda().equals(prestamo.getMoneda())&&!prestamoOtorgado){
                double balanceActualizado = c.getBalance()+ prestamo.getMontoPrestamo();
                c.setBalance(balanceActualizado);
                cuentaDao.save(c);
                prestamoOtorgado=true;
            }
        }
    }

    public List<Cuenta> getCuentas(){
        return cuentaDao.getCuentas();
    }

    public Cuenta eliminarCuenta(long numeroCuenta){
        Cuenta cuenta = find(numeroCuenta);
        cuentaDao.delete(numeroCuenta);
        return cuenta;
    }
}
