package api.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="menu")
public class Menu {
   @Id
   @Column(name = "id", nullable = false)
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(name = "nombre", nullable = false)
   private String nombre;

   @Column(name = "precio")
   private float precio;

   @Column(name = "descripcion", nullable = true, length = 255)
   private String descripcion;

   @Column(name = "disponibilidad", nullable = true, length = 255)
   private String disponibilidad;

   @JoinTable(
           name = "menu_producto",
           joinColumns = @JoinColumn(name = "menu_fk", nullable = false),
           inverseJoinColumns = @JoinColumn(name="producto_fk", nullable = false)
   )
   @ManyToMany(cascade = CascadeType.MERGE)
   private List<Producto> productos;

   public void addProducto(Producto producto){
      if(this.productos == null){
         this.productos = new ArrayList<>();
      }

      this.productos.add(producto);
   }

   @ManyToMany(mappedBy = "menus")
   private List<Pedido> pedidos;


   public String getDescripcion() {
      return descripcion;
   }

   public void setDescripcion(String descripcion) {
      this.descripcion = descripcion;
   }

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

   public float getPrecio() {
      return precio;
   }

   public void setPrecio(float precio) {
      this.precio = precio;
   }

   public List<Producto> getProductos() {
      return productos;
   }

   public void setProductos(List<Producto> productos) {
      this.productos = productos;
   }

   public String getDisponibilidad() {
      return disponibilidad;
   }

   public void setDisponibilidad(String disponibilidad) {
      this.disponibilidad = disponibilidad;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Menu menu = (Menu) o;
      return id.equals(menu.id);
   }

   @Override
   public int hashCode() {
      return Objects.hash(id);
   }

   @Override
   public String toString() {
      return "Menu{" +
              "id=" + id +
              ", nombre='" + nombre + '\'' +
              ", productos=" + productos +
              '}';
   }
}
