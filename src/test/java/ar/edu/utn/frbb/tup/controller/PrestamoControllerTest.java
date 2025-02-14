package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.controller.validator.ClienteValidator;
import ar.edu.utn.frbb.tup.controller.validator.PrestamoValidator;
import ar.edu.utn.frbb.tup.model.Cuota;
import ar.edu.utn.frbb.tup.model.PrestamoConsultaCliente;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.Prestamo;
import ar.edu.utn.frbb.tup.model.exception.PrestamoNoOtorgadoException;
import ar.edu.utn.frbb.tup.service.ClienteService;
import ar.edu.utn.frbb.tup.service.PrestamoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

import java.util.ArrayList;
import java.util.List;


@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PrestamoControllerTest {
    @Mock
    private PrestamoService prestamoService;

    @Mock
    private PrestamoValidator prestamoValidator;

    @InjectMocks
    private PrestamoController prestamoController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSolicitarPrestamoSuccess() throws PrestamoNoOtorgadoException {
        PrestamoDto prestamoDto = new PrestamoDto(getPrestamo());
        PrestamoOutputDto prestamoOutputDto = getPrestamoOutputDto();

        when(prestamoService.pedirPrestamo(prestamoDto)).thenReturn(prestamoOutputDto);

        PrestamoOutputDto resultado = prestamoController.solicitarPrestamo(prestamoDto);

        assertEquals(prestamoOutputDto, resultado);
        verify(prestamoService, times(1)).pedirPrestamo(prestamoDto);
    }

    @Test
    public void testSolicitarPrestamoSolicitudNula() throws PrestamoNoOtorgadoException {
        PrestamoDto prestamoDto = new PrestamoDto();

        assertThrows(IllegalArgumentException.class, () -> prestamoController.solicitarPrestamo(prestamoDto));
        verify(prestamoService, times(0)).pedirPrestamo(prestamoDto);
    }

    @Test
    public void testSolicitarPrestamoFail() throws PrestamoNoOtorgadoException {
        PrestamoDto prestamoDto = new PrestamoDto(getPrestamo());

        doThrow(new PrestamoNoOtorgadoException("El préstamo no pudo ser otorgado")).when(prestamoService).pedirPrestamo(prestamoDto);

        assertThrows(PrestamoNoOtorgadoException.class, () -> prestamoController.solicitarPrestamo(prestamoDto));
        verify(prestamoService, times(1)).pedirPrestamo(prestamoDto);
    }

    @Test
    public void testRetornarPrestamosClienteSuccess() {
        long dni = 12345678L;
        PrestamoConsultaDto prestamoConsultaDto = getPrestamoConsultaDto();

        when(prestamoService.pedirConsultaPrestamos(dni)).thenReturn(prestamoConsultaDto);

        PrestamoConsultaDto resultado = prestamoController.retonarPrestamosCliente(dni);

        assertEquals(prestamoConsultaDto, resultado);
        verify(prestamoService, times(1)).pedirConsultaPrestamos(dni);
    }

    private PrestamoOutputDto getPrestamoOutputDto() {
        PrestamoOutputDto prestamoOutputDto = new PrestamoOutputDto();
        prestamoOutputDto.setNumeroCliente(12345678L);
        prestamoOutputDto.setNumeroPrestamo(1);
        prestamoOutputDto.setEstado("APROBADO");
        prestamoOutputDto.setMensaje("El monto del prestamo fue acreditado a su cuenta");
        List<Cuota> planPagos = new ArrayList<>();
        Cuota cuota = new Cuota(1, 437.5);
        planPagos.add(cuota);
        prestamoOutputDto.setPlanPagos(planPagos);
        return prestamoOutputDto;
    }

    private Prestamo getPrestamo() {
        Prestamo prestamo = new Prestamo();
        prestamo.setNumeroCliente(12345678L);
        prestamo.setNumeroPrestamo(1);
        prestamo.setPlazoMeses(12);
        prestamo.setMontoPrestamo(5000.0);
        prestamo.setMoneda(TipoMoneda.PESOS);
        prestamo.setInteresTotal(0.416);
        return prestamo;
    }

    private PrestamoConsultaDto getPrestamoConsultaDto() {
        PrestamoConsultaDto prestamoConsultaDto = new PrestamoConsultaDto(12345678L);
        List<PrestamoConsultaCliente> prestamos = new ArrayList<PrestamoConsultaCliente>();
        Prestamo prestamo = getPrestamo();
        PrestamoConsultaCliente prestamoConsultaCliente = new PrestamoConsultaCliente(prestamo);
        prestamoConsultaCliente.setPagosRealizados(1);
        prestamoConsultaCliente.setSaldoRestante(4812.5);
        prestamos.add(prestamoConsultaCliente);
        prestamoConsultaDto.setPrestamos(prestamos);
        return prestamoConsultaDto;
    }
}

