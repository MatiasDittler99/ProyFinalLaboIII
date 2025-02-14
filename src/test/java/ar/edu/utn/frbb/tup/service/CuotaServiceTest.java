package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.model.Prestamo;
import ar.edu.utn.frbb.tup.model.PrestamoOutput;
import ar.edu.utn.frbb.tup.model.Cuota;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CuotaServiceTest {

    private Prestamo prestamo = new Prestamo();
    private PrestamoOutput prestamoOutput = new PrestamoOutput();

    @BeforeEach
    public void prestamoCongif(){
        prestamo = new Prestamo();
        prestamoOutput = new PrestamoOutput();

        prestamo.setInteresTotal(1234.0);
        prestamo.setMontoPrestamo(5678.9);
        prestamo.setPlazoMeses(12);
    }

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerarCuotas(){
        assertDoesNotThrow( () -> CuotaService.generarCuotas(prestamo, prestamoOutput));
        assertFalse(prestamoOutput.getPlanPagos().isEmpty());
        List<Cuota> cuotas = prestamoOutput.getPlanPagos();
        Cuota ultimaCuota = cuotas.get(cuotas.size()-1);
        assertEquals(ultimaCuota.getNumeroCuota(), 1);

        assertDoesNotThrow( () -> CuotaService.generarCuotas(prestamo, prestamoOutput));
        cuotas = prestamoOutput.getPlanPagos();
        ultimaCuota = cuotas.get(cuotas.size()-1);
        assertEquals(ultimaCuota.getNumeroCuota(), 2);
        assertEquals(cuotas.size(), 2);
    }

    @Test
    void calcularMontoCuotaTest(){
        double calculoTesteo = (prestamo.getMontoPrestamo() + prestamo.getInteresTotal()) / prestamo.getPlazoMeses();
        double resultadoCalculo = assertDoesNotThrow( () -> CuotaService.calcularMontoCuota(prestamo) );
        assertEquals(calculoTesteo, resultadoCalculo);
    }

}