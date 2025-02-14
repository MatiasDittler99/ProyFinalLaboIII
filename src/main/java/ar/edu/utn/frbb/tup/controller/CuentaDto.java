package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;

public class CuentaDto {
    private long dniTitular;
    private String tipoCuenta;
    private String moneda;

    public long getDniTitular() {
        return dniTitular;
    }
    public void setDniTitular(long dniTitular) {
        this.dniTitular = dniTitular;
    }

    public String getTipoCuentaString() {
        return tipoCuenta;
    }

    public TipoCuenta getTipoCuenta() {
        TipoCuenta cuentaTipo = null;
        switch (this.tipoCuenta) {
            case "CAJA_AHORRO":
                cuentaTipo = TipoCuenta.CAJA_AHORRO;
                break;
            case "CUENTA_CORRIENTE":
                cuentaTipo = TipoCuenta.CUENTA_CORRIENTE;
                break;
        }
        return cuentaTipo;
    }

    public void setTipoCuenta(String tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public void setTipoCuenta(TipoCuenta tipoCuenta) {
        switch (tipoCuenta) {
            case CAJA_AHORRO:
                this.tipoCuenta = "CAJA_AHORRO";
                break;
            case CUENTA_CORRIENTE:
                this.tipoCuenta = "CUENTA_CORRIENTE";
                break;
        }
    }

    public String getMonedaString() {
        return moneda;
    }
    public TipoMoneda getMoneda() {
        TipoMoneda monedaTipo = null;
        if (this.moneda.equals("PESOS")){
            monedaTipo=TipoMoneda.PESOS;
        }
        if (this.moneda.equals("DOLARES")){
            monedaTipo=TipoMoneda.DOLARES;
        }
        return monedaTipo;
    }
    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }
    public void setMoneda(TipoMoneda moneda){
        if (moneda==TipoMoneda.PESOS){
            this.moneda="PESOS";
        }
        if (moneda==TipoMoneda.DOLARES){
            this.moneda="DOLARES";
        }
    }
}
