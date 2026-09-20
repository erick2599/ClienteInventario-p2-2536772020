package sv.edu.utec.servicio;

import sv.edu.utec.datos.ProductoDAO; // Ajusta el paquete según tu proyecto base si difiere
import sv.edu.utec.modelo.Producto;
import sv.edu.utec.api.ProveedorAPI;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class SincronizacionService {
    private final ProveedorAPI proveedorAPI;
    private final ProductoDAO productoDAO;

    // Inyección de dependencias por constructor
    public SincronizacionService(ProveedorAPI proveedorAPI, ProductoDAO productoDAO) {
        this.proveedorAPI = proveedorAPI;
        this.productoDAO = productoDAO;
    }

    public int[] sincronizar(int limite) throws IOException, InterruptedException, SQLException {
        int insertados = 0;
        int actualizados = 0;

        // Obtener productos desde el proveedor API
        List<Producto> productosApi = proveedorAPI.obtenerProductos(limite);

        for (Producto prod : productosApi) {
            // Verificar si el producto ya existe mediante su ID en la BD local
            if (productoDAO.existe(prod.getId())) {
                productoDAO.actualizar(prod);
                actualizados++;
            } else {
                productoDAO.insertar(prod);
                insertados++;
            }
        }

        return new int[]{insertados, actualizados};
    }
}
