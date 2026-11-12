// RegistroCentral: el "archivo de la oficina" del sistema.
// Guarda los clientes, las sucursales y los envíos, y los encuentra
// cuando alguien los pide. NO hace el trabajo de nadie: no despacha,
// no entrega, no cambia estados. Solo registra, busca y lista.
//
// Guarda los MISMOS objetos, no copias: si busco un envío acá y lo
// entrego, queda entregado en todo el sistema.

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegistroCentral {

    // Clientes: en una lista. Para encontrar uno hay que recorrerla
    // (búsqueda secuencial, Parte 4.a).
    private List<Cliente> clientes;

    // Sucursales: en un HashMap, la clave es el código de la sucursal.
    private Map<Integer, Sucursal> sucursales;

    // Envíos: en un HashMap, la clave es el código de seguimiento.
    // Con el código se encuentra el envío directo, sin recorrer nada.
    private Map<Integer, Envio> envios;

    public RegistroCentral() {
        this.clientes = new ArrayList<>();
        this.sucursales = new HashMap<>();
        this.envios = new HashMap<>();
    }

    // ---------------------------------------------------------------
    // Clientes
    // ---------------------------------------------------------------

    public boolean registrarCliente(Cliente c) {
        if (existeCliente(c.getDni())) {
            System.out.println("No se registró: ya existe un cliente con DNI " + c.getDni() + ".");
            return false;
        }
        clientes.add(c);
        return true;
    }

    public List<Cliente> listarClientes() {
        return clientes;
    }

    // Búsqueda secuencial: recorre la lista desde el principio
    // hasta encontrar el cliente con ese DNI.
    public Cliente buscarClienteSecuencial(int dni) {
        for (Cliente c : clientes) {
            if (c.getDni() == dni) {
                return c;
            }
        }
        System.out.println("No se encontró ningún cliente con DNI " + dni + ".");
        return null;
    }

    // ---------------------------------------------------------------
    // Sucursales
    // ---------------------------------------------------------------

    public boolean registrarSucursal(Sucursal s) {
        if (sucursales.containsKey(s.getId())) {
            System.out.println("No se registró: ya existe una sucursal con código " + s.getId() + ".");
            return false;
        }
        sucursales.put(s.getId(), s);
        return true;
    }

    public Sucursal buscarSucursal(int codigo) {
        Sucursal s = sucursales.get(codigo);
        if (s == null) {
            System.out.println("No se encontró ninguna sucursal con código " + codigo + ".");
        }
        return s;
    }

    // ---------------------------------------------------------------
    // Envíos
    // ---------------------------------------------------------------

    public boolean registrarEnvio(Envio e) {
        if (envios.containsKey(e.getId())) {
            System.out.println("No se registró: ya existe un envío con código " + e.getId() + ".");
            return false;
        }
        envios.put(e.getId(), e);
        return true;
    }

    public Envio buscarEnvio(int codigo) {
        Envio e = envios.get(codigo);
        if (e == null) {
            System.out.println("No se encontró ningún envío con el código " + codigo
                    + ". Revise que el número esté bien escrito.");
        }
        return e;
    }

    public List<Envio> listarEnvios() {
        return new ArrayList<>(envios.values());
    }

    // ---------------------------------------------------------------
    // Validación privada
    // ---------------------------------------------------------------

    private boolean existeCliente(int dni) {
        for (Cliente c : clientes) {
            if (c.getDni() == dni) {
                return true;
            }
        }
        return false;
    }
}
