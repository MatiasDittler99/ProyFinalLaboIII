package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.PrestamoConsultaDto;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Cuota;
import ar.edu.utn.frbb.tup.model.Prestamo;
import ar.edu.utn.frbb.tup.controller.PrestamoDto;
import ar.edu.utn.frbb.tup.model.PrestamoOutput;
import ar.edu.utn.frbb.tup.controller.PrestamoOutputDto;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.exception.PrestamoNoOtorgadoException;
import ar.edu.utn.frbb.tup.model.PrestamoConsultaCliente;
import ar.edu.utn.frbb.tup.persistence.PrestamoDao;
import ar.edu.utn.frbb.tup.persistence.PrestamoOutputDao;

import org.apache.tomcat.util.openssl.pem_password_cb;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PrestamoServiceTest {

    @Mock
    private PrestamoDao prestamoDao;

    @Mock
    private Prestamo prestamo;

    @Mock
    private PrestamoDto prestamoDto;

    @Mock
    private PrestamoOutputDao prestamoOutputDao;

    @Mock
    private ClienteService clienteService;

    @Mock
    private CuentaService cuentaService;

    @Mock
    private ScoreCrediticioService scoreCrediticioService;

    @InjectMocks
    private PrestamoService prestamoService;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPedirPrestamoExito() throws PrestamoNoOtorgadoException {
        PrestamoDto prestamoDto = new PrestamoDto();
        prestamoDto.setMoneda("PESOS");
        prestamoDto.setNumeroCliente(123l);
        prestamoDto.setPlazoMeses(12);
        prestamoDto.setMontoPrestamo(1000);

        Cuenta cuenta = new Cuenta();
        cuenta.setBalance(0);
        cuenta.setMoneda(TipoMoneda.PESOS);
        cuenta.setTipoCuenta(TipoCuenta.CAJA_AHORRO);
        cuenta.setTitular(123l);

        List<Cuenta> cuentasCliente = new ArrayList<Cuenta>();
        cuentasCliente.add(cuenta);

        when(clienteService.getCuentasCliente(123l)).thenReturn(cuentasCliente);

        PrestamoOutputDto prestamoOutputDto = prestamoService.pedirPrestamo(prestamoDto);
        assertEquals(prestamoOutputDto.getNumeroCliente(), cuenta.getTitular());
        assertEquals(prestamoOutputDto.getEstado(), "APROBADO");
        verify(prestamoOutputDao, times(1)).almacenarDatosPrestamoOutput(any(PrestamoOutput.class));
    }

    @Test
    void testPedirPrestamoRechazado() throws PrestamoNoOtorgadoException {
        PrestamoDto prestamoDto = new PrestamoDto();
        prestamoDto.setMoneda("PESOS");
        prestamoDto.setNumeroCliente(124l);
        prestamoDto.setPlazoMeses(12);
        prestamoDto.setMontoPrestamo(1000);

        Cuenta cuenta = new Cuenta();
        cuenta.setBalance(0);
        cuenta.setMoneda(TipoMoneda.PESOS);
        cuenta.setTipoCuenta(TipoCuenta.CAJA_AHORRO);
        cuenta.setTitular(124l);

        List<Cuenta> cuentasCliente = new ArrayList<Cuenta>();
        cuentasCliente.add(cuenta);

        when(clienteService.getCuentasCliente(124l)).thenReturn(cuentasCliente);

        PrestamoOutputDto prestamoOutputDto = prestamoService.pedirPrestamo(prestamoDto);
        assertEquals(prestamoOutputDto.getNumeroCliente(), cuenta.getTitular());
        assertEquals(prestamoOutputDto.getEstado(), "RECHAZADO");
    }

    @Test
    void testPedirPrestamoCuentaNoPermitida() throws PrestamoNoOtorgadoException {
        PrestamoDto prestamoDto = new PrestamoDto();
        prestamoDto.setMoneda("PESOS");
        prestamoDto.setNumeroCliente(123l);
        prestamoDto.setPlazoMeses(12);
        prestamoDto.setMontoPrestamo(1000);

        Cuenta cuenta = new Cuenta();
        cuenta.setBalance(0);
        cuenta.setMoneda(TipoMoneda.DOLARES);
        cuenta.setTipoCuenta(TipoCuenta.CAJA_AHORRO);
        cuenta.setTitular(123l);

        List<Cuenta> cuentasCliente = new ArrayList<Cuenta>();
        cuentasCliente.add(cuenta);

        when(clienteService.getCuentasCliente(123l)).thenReturn(cuentasCliente);

        assertThrows(PrestamoNoOtorgadoException.class, () -> prestamoService.pedirPrestamo(prestamoDto));
    }

    @Test
    void testPedirPrestamoMontoNoPermitido() throws PrestamoNoOtorgadoException {
        PrestamoDto prestamoDto = new PrestamoDto();
        prestamoDto.setMoneda("PESOS");
        prestamoDto.setNumeroCliente(123l);
        prestamoDto.setPlazoMeses(12);
        prestamoDto.setMontoPrestamo(2000000);

        Cuenta cuenta = new Cuenta();
        cuenta.setBalance(0);
        cuenta.setMoneda(TipoMoneda.PESOS);
        cuenta.setTipoCuenta(TipoCuenta.CAJA_AHORRO);
        cuenta.setTitular(123l);

        List<Cuenta> cuentasCliente = new ArrayList<Cuenta>();
        cuentasCliente.add(cuenta);

        when(clienteService.getCuentasCliente(123l)).thenReturn(cuentasCliente);

        assertThrows(PrestamoNoOtorgadoException.class, () -> prestamoService.pedirPrestamo(prestamoDto));
    }

    @Test
    void testPedirPrestamoDeudor() throws PrestamoNoOtorgadoException {
        PrestamoDto prestamoDto = new PrestamoDto();
        prestamoDto.setMoneda("PESOS");
        prestamoDto.setNumeroCliente(123l);
        prestamoDto.setPlazoMeses(12);
        prestamoDto.setMontoPrestamo(1000);

        Cuenta cuenta = new Cuenta();
        cuenta.setBalance(0);
        cuenta.setMoneda(TipoMoneda.PESOS);
        cuenta.setTipoCuenta(TipoCuenta.CAJA_AHORRO);
        cuenta.setTitular(123l);

        List<Cuenta> cuentasCliente = new ArrayList<Cuenta>();
        cuentasCliente.add(cuenta);

        List<Prestamo> prestamosCliente = new ArrayList<Prestamo>();
        Prestamo prestamo1 = new Prestamo();
        Prestamo prestamo2 = new Prestamo();
        Prestamo prestamo3 = new Prestamo();
        Prestamo prestamo4 = new Prestamo();
        prestamosCliente.add(prestamo1);
        prestamosCliente.add(prestamo2);
        prestamosCliente.add(prestamo3);
        prestamosCliente.add(prestamo4);

        when(clienteService.getCuentasCliente(123l)).thenReturn(cuentasCliente);
        when(prestamoDao.getPrestamosByCliente(123l)).thenReturn(prestamosCliente);

        assertThrows(PrestamoNoOtorgadoException.class, () -> prestamoService.pedirPrestamo(prestamoDto));
    }

    @Test
    void testCalculaIntereses(){
        double calculoManual = 1234.5 * ((double) 5 / 12);
        double calculoMetodo = assertDoesNotThrow( () -> prestamoService.calculaIntereses(1234.5, 5));
        assertEquals(calculoManual, calculoMetodo);
    }

    @Test
    void testPedirConsultaPrestamosFallo(){
        List<Prestamo> prestamosCliente = new ArrayList<Prestamo>();

        when(prestamoDao.getPrestamosByCliente(anyLong())).thenReturn(prestamosCliente);
        assertThrows(IllegalArgumentException.class, () -> { prestamoService.pedirConsultaPrestamos(123); });
    }

    @Test
    void testPedirConsultaPrestamosExito(){
        Prestamo prestamo = new Prestamo();
        prestamo.setInteresTotal(500.);
        prestamo.setPlazoMeses(12);
        prestamo.setMontoPrestamo(2500.);
        prestamo.setNumeroPrestamo(1);
        prestamo.setNumeroCliente(123l);
        List<Prestamo> prestamosCliente = new ArrayList<Prestamo>();
        prestamosCliente.add(prestamo);

        Cuota cuota = new Cuota(1, 250);
        List<Cuota> cuotas = new ArrayList<Cuota>();
        cuotas.add(cuota);

        PrestamoOutput prestamoOutput = new PrestamoOutput();
        prestamoOutput.setNumeroPrestamo(1);
        prestamoOutput.setPlanPagos(cuotas);
        prestamo.setNumeroCliente(123l);
        List<PrestamoOutput> prestamosOutputCliente = new ArrayList<PrestamoOutput>();
        prestamosOutputCliente.add(prestamoOutput);

        when(prestamoDao.getPrestamosByCliente(anyLong())).thenReturn(prestamosCliente);
        when(prestamoOutputDao.getPrestamosOutputByCliente(anyLong())).thenReturn(prestamosOutputCliente);

        PrestamoConsultaDto consulta = prestamoService.pedirConsultaPrestamos(123l);
        assertEquals(123l, consulta.getNumeroCliente());
        assertEquals(prestamo.getInteresTotal(), consulta.getPrestamos().get(0).getIntereses());
        assertEquals(prestamo.getMontoPrestamo(), consulta.getPrestamos().get(0).getMonto());
        assertEquals(0, consulta.getPrestamos().get(0).getPagosRealizados());
        assertEquals(prestamo.getPlazoMeses(), consulta.getPrestamos().get(0).getPlazoMeses());
        assertEquals(3000, consulta.getPrestamos().get(0).getSaldoRestante());  
    }

}