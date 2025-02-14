package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Prestamo;
import ar.edu.utn.frbb.tup.persistence.entity.PrestamoEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PrestamoDao extends AbstractBaseDao{
    public void almacenarDatosPrestamo(Prestamo prestamo){
        PrestamoEntity prestamoEntity = new PrestamoEntity(prestamo);
        getInMemoryDatabase().put(prestamoEntity.getId(), prestamoEntity);
    }

    public List<Prestamo> getPrestamosByCliente(long dni) {
        List<Prestamo> prestamosCliente = new ArrayList<Prestamo>();

        for (Object valor : getInMemoryDatabase().values()){
            if (valor.getClass().equals(PrestamoEntity.class)){
                PrestamoEntity prestamo = (PrestamoEntity) valor;
                if(prestamo.getNumeroCliente() == dni){
                    prestamosCliente.add(prestamo.toPrestamo());
                }
            }
        }

        return prestamosCliente;
    }

    public List<Prestamo> getPrestamosById(long id) {
        List<Prestamo> prestamosCliente = new ArrayList<Prestamo>();

        for (Object valor : getInMemoryDatabase().values()){
            if (valor.getClass().equals(PrestamoEntity.class)){
                PrestamoEntity prestamo = (PrestamoEntity) valor;
                if(prestamo.getId() == id){
                    prestamosCliente.add(prestamo.toPrestamo());
                }
            }
        }

        return prestamosCliente;
    }

    @Override
    protected String getEntityName() {
        return "PRESTAMO";
    }

}
