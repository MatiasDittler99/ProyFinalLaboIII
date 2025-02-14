package ar.edu.utn.frbb.tup.controller.validator;

import ar.edu.utn.frbb.tup.controller.CuentaDto;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import org.springframework.stereotype.Component;


@Component
public class CuentaValidator {
    public static void validate(CuentaDto cuentaDto){
        //-->Hay q validar q nada este vacio, es con @Valid y @NotNull en 'model'
        validateTipoMoneda(cuentaDto);
        validateTipoCuenta(cuentaDto);
    }


    protected static void validateTipoMoneda(CuentaDto cuentaDto){
        if( (!"PESOS".equals(cuentaDto.getMonedaString())) && (!"DOLARES".equals(cuentaDto.getMonedaString()))) {
            throw new IllegalArgumentException("La moneda: "+cuentaDto.getMoneda()+" no soportada");
        }
    }
    protected static void validateTipoCuenta(CuentaDto cuentaDto){
        if((!"CAJA_AHORRO".equals(cuentaDto.getTipoCuentaString())) && (!"CUENTA_CORRIENTE".equals(cuentaDto.getTipoCuentaString()))){
            throw  new IllegalArgumentException("La cuenta del tipo "+cuentaDto.getTipoCuenta()+"no es soportada");
        }
    }
}
