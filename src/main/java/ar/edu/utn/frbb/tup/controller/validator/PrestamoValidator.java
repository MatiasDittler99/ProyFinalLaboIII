package ar.edu.utn.frbb.tup.controller.validator;

import ar.edu.utn.frbb.tup.controller.PrestamoDto;
import ar.edu.utn.frbb.tup.model.TipoMoneda;

public class PrestamoValidator {
    public static void validate(PrestamoDto prestamoDto){
        validateMonto(prestamoDto.getMontoPrestamo());
        validateMoneda(prestamoDto.getMonedaString());
    }

    private static void validateMonto(double monto){
        if (monto < 0){
            throw new IllegalArgumentException("Debe establecer un monto en positivo");
        }
        if (monto == 0){
            throw new IllegalArgumentException("Debe establecer cuanto va a pedir");
        }
    }

    private static void validateMoneda(String moneda){
        if (!moneda.equals("PESOS")){
            throw new IllegalArgumentException("Los préstamos únicamente serán otorgados en PESOS");
        }
    }
}
