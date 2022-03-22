package api.models;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="ingrediente")
public class Ingrediente {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id", nullable = false)
   private Long id;

   @Column(name = "nombre", nullable = false)
   private String nombre;

   @Column(name = "stock", nullable = false)
   private int stock;


   @ManyToMany(mappedBy = "ingredientes", cascade = CascadeType.REMOVE)
   private List<Producto> productos;

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getNombre() {
      return nombre;
   }

   public void setNombre(String nombre) {
      this.nombre = nombre;
   }

   public int getStock() {
      return stock;
   }

   public void setStock(int stock) {
      this.stock = stock;
   }

   /*public List<Producto> getProductos() {
      return productos;
   }

   public void setProductos(List<Producto> productos) {
      this.productos = productos;
   }*/

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Ingrediente that = (Ingrediente) o;
      return id.equals(that.id);
   }

   @Override
   public int hashCode() {
      return Objects.hash(id);
   }

   @Override
   public String toString() {
      return "Ingrediente{" +
              "id=" + id +
              ", nombre='" + nombre + '\'' +
              ", stock=" + stock +
              '}';
   }
}
