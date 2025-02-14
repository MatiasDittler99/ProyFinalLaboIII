package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.TipoPersona;

public class ClienteDto extends PersonaDto{
    private String tipoPersona;
    private String banco;

    public String getTipoPersonaString() {
        return tipoPersona;
    }
    public TipoPersona getTipoPersona() {
        TipoPersona personaTipo = null;
        if (this.tipoPersona.equals("F")){
            personaTipo=TipoPersona.PERSONA_FISICA;
        }
        if (this.tipoPersona.equals("J")){
            personaTipo=TipoPersona.PERSONA_JURIDICA;
        }
        return personaTipo;
    }

    public void setTipoPersona(String tipoPersona) {
        this.tipoPersona = tipoPersona;
    }
     public void setTipoPersona(TipoPersona tipoPersona){
        if (tipoPersona==TipoPersona.PERSONA_FISICA){
            this.tipoPersona="F";
        }
        if (tipoPersona==TipoPersona.PERSONA_JURIDICA){
            this.tipoPersona="J";
        }
    }


    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }
}
