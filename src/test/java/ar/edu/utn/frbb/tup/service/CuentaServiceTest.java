package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.model.*;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaNoSoportadaException;
import ar.edu.utn.frbb.tup.persistence.ClienteDao;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import ar.edu.utn.frbb.tup.controller.CuentaDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CuentaServiceTest {

    @Mock
    private ClienteDao clienteDao;

    @Mock
    private CuentaDao cuentaDao;

    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private CuentaService cuentaService;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void actualizarSaldoCliente(){
        Prestamo prestamo = new Prestamo();
        prestamo.setMoneda(TipoMoneda.PESOS);
        prestamo.setMontoPrestamo(100.);
        prestamo.setNumeroCliente(123l);

        Cuenta cuenta = new Cuenta();
        cuenta.setBalance(0.);
        cuenta.setMoneda(TipoMoneda.PESOS);

        List<Cuenta> cuentas = new ArrayList<>();
        cuentas.add(cuenta);

        when(clienteService.getCuentasCliente(prestamo.getNumeroCliente())).thenReturn(cuentas);
        cuentaService.actualizarCuentaCliente(prestamo);

        assertEquals(100.0, cuenta.getBalance());
        verify(clienteService).getCuentasCliente(prestamo.getNumeroCliente());
    }

    @Test
    public void testCuentaExistente()
            throws CuentaAlreadyExistsException, TipoCuentaAlreadyExistsException, TipoCuentaNoSoportadaException {
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setDniTitular(123l);
        cuentaDto.setMoneda("PESOS");
        cuentaDto.setTipoCuenta("CAJA_AHORRO");

        Cuenta cuentaExistente = new Cuenta();
        cuentaExistente.setNumeroCuenta(1);

        when(cuentaDao.find(anyLong())).thenReturn(cuentaExistente);
        assertThrows(CuentaAlreadyExistsException.class,() -> cuentaService.darDeAltaCuenta(cuentaDto));
    }

    @Test
    public void testTipoCuentaNoSoportada() {
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE);
        cuentaDto.setMoneda(TipoMoneda.DOLARES);
        cuentaDto.setDniTitular(123l);

        doReturn(null).when(cuentaDao).find(anyLong());

        assertThrows(TipoCuentaNoSoportadaException.class, () -> {cuentaService.darDeAltaCuenta(cuentaDto);});
    }

    @Test
    public void testClienteYaTieneCuentaDeEseTipo() throws CuentaAlreadyExistsException, TipoCuentaAlreadyExistsException, TipoCuentaNoSoportadaException {
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE);
        cuentaDto.setMoneda(TipoMoneda.PESOS);

        when(cuentaDao.find(anyLong())).thenReturn(null);
        doThrow(TipoCuentaAlreadyExistsException.class).when(clienteService).agregarCuenta(any(Cuenta.class));
        assertThrows(TipoCuentaAlreadyExistsException.class, () -> cuentaService.darDeAltaCuenta(cuentaDto));
    }

    @Test
    public void testDarDeAltaCuentaTipoNuevo() throws CuentaAlreadyExistsException, TipoCuentaAlreadyExistsException, TipoCuentaNoSoportadaException, ClienteAlreadyExistsException {
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setTipoCuenta(TipoCuenta.CAJA_AHORRO);
        cuentaDto.setMoneda(TipoMoneda.PESOS);

        when(cuentaDao.find(anyLong())).thenReturn(null);
        cuentaService.darDeAltaCuenta(cuentaDto);

        verify(cuentaDao, times(1)).save(any(Cuenta.class));
    }
}