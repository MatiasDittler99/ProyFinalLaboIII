package ar.edu.utn.frbb.tup.controller.validator;

import ar.edu.utn.frbb.tup.controller.CuentaDto;
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
public class CuentaValidatorTest {

    @Mock
    private CuentaDto cuentaDto;

    @InjectMocks
    private CuentaValidator cuentaValidator;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCuentaExito() {
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setMoneda("PESOS");
        cuentaDto.setTipoCuenta("CAJA_AHORRO");
        assertDoesNotThrow(() -> cuentaValidator.validate(cuentaDto));
    }

    @Test
    public void testCuentaMonedaErronea() {
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setMoneda("REALES");
        cuentaDto.setTipoCuenta("CAJA_AHORRO");
        assertThrows(IllegalArgumentException.class,() -> cuentaValidator.validate(cuentaDto));
    }

    @Test
    public void testPrestamoMonedaErronea() {
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setMoneda("PESOS");
        cuentaDto.setTipoCuenta("CAJA_COMPARTIDA");
        assertThrows(IllegalArgumentException.class,() -> cuentaValidator.validate(cuentaDto));
    }
}