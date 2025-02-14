package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.persistence.entity.ClienteEntity;
import ar.edu.utn.frbb.tup.persistence.entity.CuentaEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CuentaDao  extends AbstractBaseDao{
    @Override
    protected String getEntityName() {
        return "CUENTA";
    }

    public void save(Cuenta cuenta) {
        CuentaEntity entity = new CuentaEntity(cuenta);
        getInMemoryDatabase().put(entity.getId(), entity);
    }

    public Cuenta find(long id) {
        if (getInMemoryDatabase().get(id) == null) {
            return null;
        }
        return ((CuentaEntity) getInMemoryDatabase().get(id)).toCuenta();
    }

    public List<Cuenta> getCuentasByCliente(long dni) {
        List<Cuenta> cuentasDelCliente = new ArrayList<>();
        for (Object object:
                getInMemoryDatabase().values()) {
            CuentaEntity cuenta = ((CuentaEntity) object);
            if (cuenta.getTitular().equals(dni)) {
                cuentasDelCliente.add(cuenta.toCuenta());
            }
        }
        return cuentasDelCliente;
    }

    public List<Cuenta> getCuentas() {
    List<Cuenta> cuentas = new ArrayList<>();
    for (Object value : getInMemoryDatabase().values()) {
        CuentaEntity cuentaEntity = (CuentaEntity) value;
        Cuenta cuenta = cuentaEntity.toCuenta();
        cuentas.add(cuenta);
    }
    return cuentas;
    }

    public void delete(long numeroCuenta) {
        if (getInMemoryDatabase().get(numeroCuenta) == null) {
            throw new IllegalArgumentException("La cuenta " + numeroCuenta + " no existe.");
        }
        getInMemoryDatabase().remove(numeroCuenta);
    }

}
