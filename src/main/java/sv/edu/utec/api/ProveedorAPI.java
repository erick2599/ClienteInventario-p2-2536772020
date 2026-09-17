package sv.edu.utec.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import sv.edu.utec.modelo.Producto;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class ProveedorAPI {

    public List<Producto> obtenerProductos(int limite) throws IOException, InterruptedException {
        // Se definen los componentes por separado para evitar errores de concatenación o espacios ocultos
        String esquema = "https";
        String host = "dummyjson.com";
        String ruta = "/products";
        String consulta = "limit=" + limite + "&select=title,stock";

        URI uri;
        try {
            uri = new URI(esquema, host, ruta, consulta, null);
        } catch (Exception e) {
            throw new IOException("Error en la estructura interna de la URL: " + e.getMessage());
        }

        // Línea de diagnóstico: imprimirá la URL exacta en la terminal antes de hacer la petición
        System.out.println("Enviando solicitud HTTP a: " + uri.toString());

        HttpClient cliente = HttpClient.newHttpClient();
        HttpRequest solicitud = HttpRequest.newBuilder()
                .uri(uri)
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> respuesta = cliente.send(solicitud, HttpResponse.BodyHandlers.ofString());

        // Validar código de estado diferente a 200
        if (respuesta.statusCode() != 200) {
            throw new IOException("Error al consumir la API. Código de estado recibido: " + respuesta.statusCode());
        }

        // Deserializar usando ObjectMapper
        ObjectMapper mapper = new ObjectMapper();
        RespuestaProductos resp = mapper.readValue(respuesta.body(), RespuestaProductos.class);

        // Convertir y devolver la lista
        List<Producto> listaProductos = new ArrayList<>();
        if (resp != null && resp.getProducts() != null) {
            for (ProductoApi apiProd : resp.getProducts()) {
                listaProductos.add(apiProd.aProducto());
            }
        }

        return listaProductos;
    }

}
