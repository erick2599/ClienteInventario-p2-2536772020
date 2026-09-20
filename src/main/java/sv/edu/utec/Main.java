package sv.edu.utec;

import sv.edu.utec.datos.ProductoDAO;
import sv.edu.utec.modelo.Producto;
import sv.edu.utec.api.ProveedorAPI;
import sv.edu.utec.servicio.SincronizacionService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        // ... (Conserva aquí cualquier código inicial que ya tenga tu Main, como inicialización de BD o pruebas previas) ...

        try {
            // Inicialización de componentes según la arquitectura por capas
            ProductoDAO dao = new ProductoDAO();
            ProveedorAPI api = new ProveedorAPI();
            SincronizacionService sincronizacionService = new SincronizacionService(api, dao);

            // 1. Invocación de la sincronización con límite de 10 productos
            int[] resultado = sincronizacionService.sincronizar(10);

            // Mostrar resumen en consola exactamente como lo pide el enunciado
            System.out.println("Sincronizacion con la API -> insertados: " + resultado[0] + " | actualizados: " + resultado[1]);


            // 2. Imprimir el inventario resultante usando el método imprimir(...) existente
            System.out.println("--- Inventario sincronizado ---");
            List<Producto> todos = dao.listar(); // O el método que use tu DAO para traer la lista completa
            imprimir(todos);

        } catch (SQLException e) {
            System.err.println("Error en la base de datos: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error de red o E/S al conectar con el proveedor: " + e.getMessage());
        } catch (InterruptedException e) {
            System.err.println("La sincronización fue interrumpida: " + e.getMessage());
            Thread.currentThread().interrupt(); // Restablecer el estado de interrupción
        }
    }

    /**
     * Este método ya existe en tu proyecto base. Asegúrate de mantenerlo intacto.
     */
    public static void imprimir(List<Producto> lista) {
        System.out.printf("%-5s %-35s %-10s%n", "ID", "PRODUCTO", "CANTIDAD");
        for (Producto p : lista) {
            // Si el nombre es muy largo para la consola, se puede recortar visualmente
            String nombreTruncado = p.getNombre().length() > 30 ? p.getNombre().substring(0, 27) + "..." : p.getNombre();
            System.out.printf("%-5d %-35s %-10d%n", p.getId(), nombreTruncado, p.getCantidad());
        }
    }
}
