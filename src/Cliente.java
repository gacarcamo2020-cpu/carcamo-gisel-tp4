// Cliente: la persona que pide los envíos.
// Guarda sus datos y la lista de envíos que hizo.
// Se identifica por su DNI (con el DNI el RegistroCentral lo encuentra).

import java.util.ArrayList;
import java.util.List;

public class Cliente {

    private int dni;
    private String nombre;
    private String contacto;
    private List<Envio> misEnvios;

    public Cliente(int dni, String nombre, String contacto) {
        this.dni = dni;
        this.nombre = nombre;
        this.contacto = contacto;
        this.misEnvios = new ArrayList<>();
    }

    public Cliente(int dni, String nombre) {
        this(dni, nombre, "");
    }

    public int getDni() {
        return dni;
    }

    public String getNombre() {
        return nombre;
    }

    public String getContacto() {
        return contacto;
    }

    public List<Envio> getMisEnvios() {
        return misEnvios;
    }

    // Solo lo llama Envio cuando se crea, para anotarse en la lista del cliente.
    void agregarEnvio(Envio envio) {
        misEnvios.add(envio);
    }

    public int cantidadDeEnvios() {
        return misEnvios.size();
    }

    @Override
    public String toString() {
        return "Cliente DNI " + dni + " - " + nombre;
    }
}
