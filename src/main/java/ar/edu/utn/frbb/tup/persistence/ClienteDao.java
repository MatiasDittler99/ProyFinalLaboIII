package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.persistence.entity.ClienteEntity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteDao extends AbstractBaseDao{

    @Autowired
    CuentaDao cuentaDao;

    public Cliente find(long dni, boolean loadComplete) {
        if (getInMemoryDatabase().get(dni) == null)
            return null;
        Cliente cliente =   ((ClienteEntity) getInMemoryDatabase().get(dni)).toCliente();
        if (loadComplete) {
            for (Cuenta cuenta :
                    cuentaDao.getCuentasByCliente(dni)) {
                cliente.addCuenta(cuenta);
            }
        }
        return cliente;
    }

   public List<Cliente> getClientes(boolean loadComplete) {
    List<Cliente> clientes = new ArrayList<>();
    for (Object value : getInMemoryDatabase().values()) {
        ClienteEntity clienteEntity = (ClienteEntity) value;
        Cliente cliente = clienteEntity.toCliente();
        if (loadComplete) {
            for (Cuenta cuenta : 
                    cuentaDao.getCuentasByCliente(cliente.getDni())) {
                cliente.addCuenta(cuenta);
            }
        }
        clientes.add(cliente);
    }
    return clientes;
    }

    public void delete(long dni) {
        if (getInMemoryDatabase().get(dni) == null) {
            throw new IllegalArgumentException("El cliente con DNI " + dni + " no existe.");
        }
        getInMemoryDatabase().remove(dni);
    }

    public void save(Cliente cliente) {
        ClienteEntity entity = new ClienteEntity(cliente);
        getInMemoryDatabase().put(entity.getId(), entity);
    }

    @Override
    protected String getEntityName() {
        return "CLIENTE";
    }
}
