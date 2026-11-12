// Sucursal: un punto físico por donde pasa el envío en su recorrido.
// La sucursal NO decide cómo cambia el estado del envío: solo le avisa
// "te recibí" o "te despacho", y es el propio Envio el que se encarga
// de todo lo demás (cambiar su estado, avisarle a sus paquetes, etc).
//
// TP4: cada sucursal tiene una COLA de envíos pendientes de despacho.
// La cola respeta el orden de llegada (FIFO: el primero que entra es
// el primero que sale), como la fila del banco. El camión tiene una
// capacidad limitada, por eso los envíos salen de a grupos.

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Sucursal {

    private int id; // código de la sucursal
    private String nombre;
    private String ciudad;
    private Queue<Envio> colaDeDespacho;

    public Sucursal(int id, String nombre, String ciudad) {
        this.id = id;
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.colaDeDespacho = new LinkedList<>();
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCiudad() {
        return ciudad;
    }

    public int cantidadEnCola() {
        return colaDeDespacho.size();
    }

    // El envío llega a la sucursal y se pone al FINAL de la cola.
    public void recibirEnvio(Envio envio) {
        envio.serRecibidoEn(this); // le pido al envío que se encargue
        colaDeDespacho.add(envio);
    }

    // Salen como máximo "cantidad" envíos, en el orden en que llegaron.
    // Si hay menos envíos que la capacidad, salen los que haya.
    // Devuelve la lista de los envíos que salieron.
    public List<Envio> despachar(int cantidad) {
        List<Envio> despachados = new ArrayList<>();

        if (colaDeDespacho.isEmpty()) {
            System.out.println("La " + nombre + " no tiene envíos esperando para despachar.");
            return despachados;
        }

        while (despachados.size() < cantidad && !colaDeDespacho.isEmpty()) {
            Envio envio = colaDeDespacho.poll(); // saca el PRIMERO de la cola
            envio.serDespachadoDesde(this);      // el envío hace su parte
            despachados.add(envio);
        }
        return despachados;
    }

    @Override
    public String toString() {
        return "Sucursal #" + id + " - " + nombre + " (" + ciudad + ")";
    }
}
