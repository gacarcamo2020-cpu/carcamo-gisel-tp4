// App: acá se crean los objetos y se prueba que todo funcione.
// Esta clase no decide NADA sobre el negocio: solo pide cosas
// (crea objetos y manda mensajes). Toda la lógica real vive en
// las otras clases.
//
// TP4: ahora todo lo que existe se guarda en un único RegistroCentral.

import java.util.List;

public class App {

    public static void main(String[] args) {

        // Única instancia del registro, creada en el main.
        RegistroCentral registro = new RegistroCentral();

        // =========================================================
        // PARTE 1 y 4.a: clientes (lista + búsqueda secuencial por DNI)
        // =========================================================
        System.out.println("=== Clientes ===");
        registro.registrarCliente(new Cliente(30111222, "Juan Perez", "juan@mail.com"));
        registro.registrarCliente(new Cliente(28444555, "Maria Gomez"));
        registro.registrarCliente(new Cliente(35666777, "Lucia Fernandez"));
        registro.registrarCliente(new Cliente(30111222, "Juan Repetido")); // DNI repetido

        System.out.println("Clientes registrados:");
        for (Cliente c : registro.listarClientes()) {
            System.out.println("  " + c);
        }

        System.out.println("\n=== Búsqueda secuencial por DNI ===");
        Cliente juan = registro.buscarClienteSecuencial(30111222);
        System.out.println("Buscado 30111222: " + juan);
        Cliente maria = registro.buscarClienteSecuencial(28444555);
        Cliente lucia = registro.buscarClienteSecuencial(35666777);
        registro.buscarClienteSecuencial(99999999); // no existe

        // =========================================================
        // Sucursales (guardadas en un HashMap por código)
        // =========================================================
        registro.registrarSucursal(new Sucursal(1, "Sucursal Viedma", "Viedma"));
        registro.registrarSucursal(new Sucursal(2, "Sucursal Bahia Blanca", "Bahia Blanca"));
        registro.registrarSucursal(new Sucursal(3, "Sucursal Buenos Aires", "Buenos Aires"));

        Sucursal viedma = registro.buscarSucursal(1);
        Sucursal bahiaBlanca = registro.buscarSucursal(2);
        Sucursal buenosAires = registro.buscarSucursal(3);

        // =========================================================
        // PARTE 2: envíos en un HashMap (código de seguimiento -> envío)
        // =========================================================
        System.out.println("\n=== Registro de envíos (HashMap) ===");
        Envio envio1 = new EnvioEstandar(1001, juan);
        envio1.agregarPaquete(new Paquete(1, "Notebook", 2.5));
        envio1.agregarPaquete(new Paquete(2, "Libros", 3.0));

        Envio envio2 = new EnvioExpress(1002, maria);
        envio2.agregarPaquete(new Paquete(3, "Documentos", 0.5));

        Envio envio3 = new EnvioEstandar(1003, lucia);
        envio3.agregarPaquete(new Paquete(4, "Ropa", 1.2));

        registro.registrarEnvio(envio1);
        registro.registrarEnvio(envio2);
        registro.registrarEnvio(envio3);
        registro.registrarEnvio(envio1); // código repetido: no se registra
        System.out.println("Envíos registrados: " + registro.listarEnvios().size());

        // =========================================================
        // PARTE 3: cola de despacho (FIFO + capacidad del camión)
        // =========================================================
        System.out.println("\n=== Cola de despacho en Viedma ===");
        // Llegan en este orden: 1001, 1002, 1003
        viedma.recibirEnvio(envio1);
        viedma.recibirEnvio(envio2);
        viedma.recibirEnvio(envio3);
        System.out.println("Envíos esperando en Viedma: " + viedma.cantidadEnCola());

        mostrarDespacho(viedma, 2); // camión con capacidad 2: salen 1001 y 1002
        mostrarDespacho(viedma, 2); // queda uno solo: sale 1003
        mostrarDespacho(viedma, 2); // la cola está vacía: se informa

        // Siguen viaje: llegan a Bahía Blanca y después a Buenos Aires
        bahiaBlanca.recibirEnvio(envio1);
        bahiaBlanca.recibirEnvio(envio2);
        bahiaBlanca.recibirEnvio(envio3);
        mostrarDespacho(bahiaBlanca, 5); // capacidad 5, pero hay 3: salen los 3

        buenosAires.recibirEnvio(envio1);
        buenosAires.recibirEnvio(envio2);
        buenosAires.recibirEnvio(envio3);

        // =========================================================
        // El registro SOLO busca; la acción la hace el propio envío
        // =========================================================
        System.out.println("\n=== Buscar un envío y entregarlo ===");
        Envio buscado = registro.buscarEnvio(1001);
        buscado.entregar(); // la responsabilidad sigue en Envio
        // Es el mismo objeto: si lo vuelvo a buscar, ya figura entregado
        System.out.println(registro.buscarEnvio(1001));
        registro.buscarEnvio(9999); // no existe

        System.out.println("\n=== Historial del envío 1001 ===");
        envio1.mostrarHistorial();

        // =========================================================
        // Resumen general
        // =========================================================
        System.out.println("\n=== Resumen de todos los envíos ===");
        double costoTotal = 0;
        for (Envio e : registro.listarEnvios()) {
            System.out.println(e + " -> Costo: $" + e.calcularCosto());
            costoTotal += e.calcularCosto();
        }
        System.out.println("Costo total de todos los envíos: $" + costoTotal);

        // =========================================================
        // Pruebas de que las validaciones del negocio siguen funcionando
        // =========================================================
        System.out.println("\n=== Probando que el sistema rechace operaciones inválidas ===");
        try {
            envio1.entregar(); // ya se había entregado
        } catch (IllegalStateException error) {
            System.out.println("Error esperado (entregar dos veces): " + error.getMessage());
        }

        try {
            Envio sinPaquetes = new EnvioEstandar(1004, juan);
            viedma.recibirEnvio(sinPaquetes); // no tiene paquetes
        } catch (IllegalStateException error) {
            System.out.println("Error esperado (envío sin paquetes): " + error.getMessage());
        }
    }

    // Muestra qué envíos salieron en un despacho (solo imprime).
    private static void mostrarDespacho(Sucursal sucursal, int capacidad) {
        System.out.println("Despacho desde " + sucursal.getNombre() + " (capacidad " + capacidad + "):");
        List<Envio> salieron = sucursal.despachar(capacidad);
        for (Envio e : salieron) {
            System.out.println("  Salió el envío " + e.getId());
        }
        System.out.println("  Quedan en cola: " + sucursal.cantidadEnCola());
    }
}
