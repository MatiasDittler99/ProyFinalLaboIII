package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.controller.validator.CuentaValidator;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaNoSoportadaException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.service.CuentaService;
import ar.edu.utn.frbb.tup.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static ar.edu.utn.frbb.tup.controller.validator.CuentaValidator.validate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CuentaControllerTest {
@Mock
    private CuentaService cuentaService;

    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private CuentaController cuentaController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCrearCuentaSuccess() throws TipoCuentaNoSoportadaException, TipoCuentaAlreadyExistsException, CuentaAlreadyExistsException {
        CuentaDto cuentaDto = getCuentaDto();
        Cuenta cuenta = getCuenta();

        when(cuentaService.darDeAltaCuenta(cuentaDto)).thenReturn(cuenta);

        Cuenta resultado = cuentaController.crearCuenta(cuentaDto);

        assertEquals(cuenta, resultado);
        verify(cuentaService, times(1)).darDeAltaCuenta(cuentaDto);
    }

    @Test
    public void testCrearCuentaFail() throws TipoCuentaNoSoportadaException, TipoCuentaAlreadyExistsException, CuentaAlreadyExistsException {
        CuentaDto cuentaDto = getCuentaDto();

        doThrow(new CuentaAlreadyExistsException("La cuenta ya existe"))
                .when(cuentaService).darDeAltaCuenta(cuentaDto);

        assertThrows(CuentaAlreadyExistsException.class, () -> cuentaController.crearCuenta(cuentaDto));
        verify(cuentaService, times(1)).darDeAltaCuenta(cuentaDto);
    }

    @Test
    public void testRetornarCuentasSuccess() {
        List<Cuenta> cuentas = new ArrayList<>(List.of(getCuenta(), getCuenta()));

        when(cuentaService.getCuentas()).thenReturn(cuentas);

        List<Cuenta> resultado = cuentaController.retonarCuentas();

        assertEquals(cuentas, resultado);
        verify(cuentaService, times(1)).getCuentas();
    }

    @Test
    public void testRetornarCuentasClienteSuccess() {
        long dni = 12345678L;
        List<Cuenta> cuentas = new ArrayList<>(List.of(getCuenta(), getCuenta()));

        when(clienteService.getCuentasCliente(dni)).thenReturn(cuentas);

        List<Cuenta> resultado = cuentaController.retonarCuentasCliente(dni);

        assertEquals(cuentas, resultado);
        verify(clienteService, times(1)).getCuentasCliente(dni);
    }

    @Test
    public void testEliminarCuentaSuccess() {
        long numeroCuenta = 987654321L;
        Cuenta cuenta = getCuenta();

        when(cuentaService.eliminarCuenta(numeroCuenta)).thenReturn(cuenta);

        Cuenta resultado = cuentaController.eliminarCuenta(numeroCuenta);

        assertEquals(cuenta, resultado);
        verify(cuentaService, times(1)).eliminarCuenta(numeroCuenta);
    }

    @Test
    public void testEliminarCuentaFail() {
        long numeroCuenta = 987654321L;

        doThrow(new IllegalArgumentException("La cuenta no existe"))
                .when(cuentaService).eliminarCuenta(numeroCuenta);

        assertThrows(IllegalArgumentException.class, () -> cuentaController.eliminarCuenta(numeroCuenta));
        verify(cuentaService, times(1)).eliminarCuenta(numeroCuenta);
    }

    private CuentaDto getCuentaDto() {
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setDniTitular(123456789l);
        cuentaDto.setTipoCuenta("CAJA_AHORRO");
        cuentaDto.setMoneda("PESOS");
        return cuentaDto;
    }

    private Cuenta getCuenta() {
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(123l);
        cuenta.setTipoCuenta(TipoCuenta.CAJA_AHORRO);
        cuenta.setMoneda(TipoMoneda.PESOS);
        cuenta.setBalance(5000.0);
        cuenta.setTitular(123456789l);
        cuenta.setFechaCreacion(LocalDateTime.now());
        return cuenta;
    }
}
