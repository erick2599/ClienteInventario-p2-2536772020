package sv.edu.utec.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import sv.edu.utec.modelo.Producto; // Ajusta el paquete según tu proyecto base si difiere

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductoApi {
    private int id;
    private String title;
    private int stock;

    public ProductoApi() {
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public Producto aProducto() {
        Producto producto = new Producto();
        producto.setId(this.id);

        // Recorte a 50 caracteres si supera el límite de VARCHAR(50)
        if (this.title != null && this.title.length() > 50) {
            producto.setNombre(this.title.substring(0, 50));
        } else {
            producto.setNombre(this.title);
        }

        producto.setCantidad(this.stock);
        return producto;
    }
}
