package ar.edu.utn.frbb.tup.controller.validator;

import ar.edu.utn.frbb.tup.controller.ClienteDto;
import ar.edu.utn.frbb.tup.controller.PrestamoDto;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.TipoPersona;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.persistence.ClienteDao;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PrestamoValidatorTest {

    @Mock
    private PrestamoDto prestamoDto;

    @InjectMocks
    private PrestamoValidator prestamoValidator;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testPrestamoExito() {
        PrestamoDto prestamoDto = new PrestamoDto();
        prestamoDto.setMoneda("PESOS");
        prestamoDto.setMontoPrestamo(100);
        assertDoesNotThrow(() -> prestamoValidator.validate(prestamoDto));
    }

    @Test
    public void testPrestamoMontoErroneo() {
        PrestamoDto prestamoDto = new PrestamoDto();
        prestamoDto.setMoneda("PESOS");
        prestamoDto.setMontoPrestamo(-12);
        assertThrows(IllegalArgumentException.class,() -> prestamoValidator.validate(prestamoDto));
    }

    @Test
    public void testPrestamoMonedaErronea() {
        PrestamoDto prestamoDto = new PrestamoDto();
        prestamoDto.setMoneda("DOLARES");
        prestamoDto.setMontoPrestamo(100);
        assertThrows(IllegalArgumentException.class,() -> prestamoValidator.validate(prestamoDto));
    }
}