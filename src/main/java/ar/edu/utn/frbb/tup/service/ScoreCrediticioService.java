package ar.edu.utn.frbb.tup.service;

import org.springframework.stereotype.Service;

@Service
public class ScoreCrediticioService {
    public boolean verificarScore(long dni){
        boolean valido;
        if (dni % 2 != 0){
            valido = true;
        }
        else {
            valido = false;
        }
        return valido;
    }
}
