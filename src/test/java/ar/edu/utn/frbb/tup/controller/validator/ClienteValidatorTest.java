package ar.edu.utn.frbb.tup.controller.validator;

import ar.edu.utn.frbb.tup.controller.ClienteDto;
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
public class ClienteValidatorTest {

    @Mock
    private ClienteDto clienteDto;

    @InjectMocks
    private ClienteValidator clienteValidator;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testClienteMenor18Años() {
        ClienteDto clienteMenorDeEdad = new ClienteDto();
        clienteMenorDeEdad.setTipoPersona("F");
        clienteMenorDeEdad.setFechaNacimiento("2020-03-18");
        assertThrows(IllegalArgumentException.class, () -> clienteValidator.validate(clienteMenorDeEdad));
    }

    @Test
    public void testFechaErronea() {
        ClienteDto clienteFechaErronea = new ClienteDto();
        clienteFechaErronea.setTipoPersona("J");
        clienteFechaErronea.setFechaNacimiento("2020-18-03");
        assertThrows(IllegalArgumentException.class, () -> clienteValidator.validate(clienteFechaErronea));
    }

    @Test
    public void testTipoPersonaErronea() {
        ClienteDto cliente= new ClienteDto();
        cliente.setTipoPersona("G");
        cliente.setFechaNacimiento("2000-03-18");
        assertThrows(IllegalArgumentException.class, () -> clienteValidator.validate(cliente));
    }

    @Test
    public void testTipoPersonaExito() {
        ClienteDto cliente= new ClienteDto();
        cliente.setTipoPersona("J");
        cliente.setFechaNacimiento("2000-03-18");
        assertDoesNotThrow(() -> clienteValidator.validate(cliente));
    }


}
