/cliente
{
    "nombre": "Juan",
    "apellido": "Pérez",
    "dni": 12345678,
    "fechaNacimiento": "1980-10-10",
    "tipoPersona": "F",
    "banco": "Banco Nación"
}

/cuenta
{
    "dniTitular": 12345678,
    "tipoCuenta": "CAJA_AHORRO",
    "moneda": "PESOS"
}

/api/prestamo
{
    "numeroCliente": 12345678,
    "plazoMeses": 12,
    "montoPrestamo": 5000,
    "moneda": "PESOS"
}


mvn clean install
mvn verify
mvn spring-boot:run