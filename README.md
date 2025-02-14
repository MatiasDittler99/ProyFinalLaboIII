# Trabajo Final Laboratorio III
## Matías Emanuel Coronel Dittler

## Tabla de Contenidos

- [Aclaración](#aclaración)
- [Model](#model)
- [Controller](#controller)
- [Service](#service)
- [Persistence](#persistence)
- [pom.xml](#pom.xml)

## Aclaración

En la presente documentación no se pretende describir todo el sistema sino que sólo se limita a detallar las actualizaciones realizadas al repositorio del curso con el fín de completar el trabajo final de Laboratorio III.

## Controller

Se agregan 'PrestamoController', 'PrestamoDto', 'PrestamoOutputDto', 'PrestamoConsultaDto' y 'PrestamoValidator'

### PrestamoController
Presenta dos métodos posibles, el primero un Post 'solicitarPrestamo' con URL .../api/prestamo tiene por objetivo el otorgamiento de préstamos a los clientes que lo soliciten (Siempre y cuando el mismo sea validado), el segundo un Get 'retornarPrestamosClientes' se accede a traves de la URL .../api/prestamo/(dniCliente) retorna todos los préstamos de un cliente y su estado actual.

### PrestamoDto
Sus atributos representan los valores que ingresa un cliente para solicitar un préstamo, gracias a la anotación @NotNull se evita que alguno de los campos sea nulo, esta clase también es la encargada de llevar la información entre capas.

### PrestamoOutputDto
Similar a la clase anterior aunque esus atributos serán el retorno que obtenga un cliente al realizar el Post 'solicitarPrestamo'.

### PrestamoValidator
Encargado de chequear que los montos para los préstamos sean positivos y que la divisa de los mismos sea el PESO, lo cual permitiría de forma rápida y sencilla alterar las monedas en las que los mismos son otorgados.

## Model

Se agregan 'Prestamo', 'PrestamoOutput', 'PrestamoConsultaCliente', 'Cuota' y 'TipoCuentaNoSoportadaException'.

### Prestamo
Esta clase es la encargada de modelar el Input para la petición de préstamos ("numeroCliente", "plazoMeses", "montoPrestamo" y "moneda") sumado a un identificador adicional "numeroPrestamo" que facilita su vinculación con la clase 'PrestamoOutput" y el atributo "interesTotal" para evitar repetir el cálculo del mismo en repetidas ocasiones.

### PrestamoOutput
Esta clase hace de complemento para "Prestamo" ya que es la encargada de tener los atributos para el output ("estado", "mensaje" y "planPagos") además del identificador "numeroPrestamo" para que pueda asociarse fácilmente con un objeto 'Prestamo'.

### Cuota
La clase representa las distintas cuotas del préstamo, teniendo por lo tanto los atributos "numeroCuota" y "monto". Siendo pensadas para almacenarse en una lista en el parámetro "planPagos" de la clase "PrestamoOutput".

### PrestamoConsultaCliente
Para responder a las consultas de préstamos fue necesario crear esta esta clase que utiliza valores de 'Prestamo' y de 'PrestamoOutput' para simplificar la repuesta. Sus atributos son 'monto', 'intereses', 'plazoMeses', 'pagosRealizados' y 'saldoRestante'.

## Service

Se agregan 'ScoreCrediticioService', 'PrestamoService' y 'CuotaService', además de modificarse 'CuentaService'.

### PrestamoService
Se maneja los métodos Post y Get relacionados con la petición y consulta de préstamos, además de incluir ciertas validaciones y funciones complementarias.

### CuotaService
Clase encargada de a generación y adición de cuotas para los planes de pago de los préstamos.

### ScoreCrediticioService
Esta clase tiene como objetivo simular el servicio externo de calificación de crédito por lo que en pos de la simplicidad únicamente retorna true (Equivalente a un score válido para préstamos) si el dni del cliente es inpar y false si es par;

### CuentaService
Se agrega el método 'actualizarCuentaCliente', el cual se asegura de la existencia de una cuenta capacitada para recibir el préstamo y la actualiza con los nuevos valores una vez otorgado el préstamo.

## Persistence

Se agregan 'PrestamoDao' y 'PrestamoOutputDao' siguiendo la misma lógica que en el resto de DAOs.

## pom.xml

Se agregaron las dependencias JaCoCo para obtener la cobertura de los test e Hibernate Validator para facilitar ciertas validaciones, además de configurar spring boot para sumar logs al proyecto y así obtener información adicional durante la ejecución.