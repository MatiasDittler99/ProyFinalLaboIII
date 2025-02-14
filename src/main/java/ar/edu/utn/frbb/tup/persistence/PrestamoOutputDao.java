package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.PrestamoOutput;
import ar.edu.utn.frbb.tup.persistence.entity.PrestamoOutputEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PrestamoOutputDao extends AbstractBaseDao{
    public void almacenarDatosPrestamoOutput(PrestamoOutput prestamoOutput){
        PrestamoOutputEntity prestamoOutputEntity = new PrestamoOutputEntity(prestamoOutput);
        getInMemoryDatabase().put(prestamoOutputEntity.getId(), prestamoOutputEntity);
    }

    public List<PrestamoOutput> getPrestamosOutputByCliente(long dni) {
        List<PrestamoOutput> prestamosOutputCliente = new ArrayList<PrestamoOutput>();

        for (Object valor : getInMemoryDatabase().values()){
            if (valor.getClass().equals(PrestamoOutputEntity.class)){
                PrestamoOutputEntity prestamoOutput = (PrestamoOutputEntity) valor;
                if(prestamoOutput.getNumeroCliente() == dni){
                    prestamosOutputCliente.add(prestamoOutput.toPrestamoOutput());
                }
            }
        }

        return prestamosOutputCliente;
    }

    public List<PrestamoOutput> getPrestamosOutputById(long id) {
        List<PrestamoOutput> prestamosOutputCliente = new ArrayList<PrestamoOutput>();

        for (Object valor : getInMemoryDatabase().values()){
            if (valor.getClass().equals(PrestamoOutputEntity.class)){
                PrestamoOutputEntity prestamoOutput = (PrestamoOutputEntity) valor;
                if(prestamoOutput.getId() == id){
                    prestamosOutputCliente.add(prestamoOutput.toPrestamoOutput());
                }
            }
        }

        return prestamosOutputCliente;
    }

    @Override
    protected String getEntityName() {
        return "PRESTAMOOUTPUT";
    }

}
