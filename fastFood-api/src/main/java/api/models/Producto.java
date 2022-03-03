package api.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="producto")
public class Producto {

   @Id
   @Column(name = "id", nullable = false)
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(name = "nombre", nullable = false, length = 50)
   private String nombre;

   @ManyToOne(optional = false, cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
   private Categoria categoria;


   @Column(name = "precio", nullable = false, length = 50)
   private String precio;

   @Column(name = "disponibilidad", nullable = false, length = 50)
   private String disponibilidad;

   @ManyToMany(mappedBy = "productos")
   private List<Pedido> pedidos;

   @ManyToMany(mappedBy = "productos")
   private List<Menu> menus;

   @JoinTable(
           name = "producto_ingrediente",
           joinColumns = @JoinColumn(name = "producto_fk", nullable = false),
           inverseJoinColumns = @JoinColumn(name="ingrediente_fk", nullable = false)
   )
   @ManyToMany(cascade = CascadeType.MERGE)
   private List<Ingrediente> ingredientes;

   public void addIngredientes(Ingrediente ingrediente){
      if(this.ingredientes == null){
         this.ingredientes = new ArrayList<>();
      }
      this.ingredientes.add(ingrediente);
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

   public Categoria getCategoria() {
      return categoria;
   }

   public void setCategoria(Categoria categoria) {
      this.categoria = categoria;
   }

   public String getPrecio() {
      return precio;
   }

   public void setPrecio(String precio) {
      this.precio = precio;
   }

   public String getDisponibilidad() { return this.disponibilidad; }

   public void setDisponibilidad(String disponibilidad) { this.disponibilidad = disponibilidad; }

   public List<Ingrediente> getIngredientes() {
      return ingredientes;
   }

   public void setIngredientes(List<Ingrediente> ingredientes) {
      this.ingredientes = ingredientes;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Producto producto = (Producto) o;
      return id.equals(producto.id);
   }

   @Override
   public int hashCode() {
      return Objects.hash(id);
   }

   @Override
   public String toString() {
      return "Producto {" +
              "id=" + id +
              ", nombre='" + nombre + '\'' +
              ", categoria=" + categoria +
              ", precio='" + precio + '\'' +
              ", disponibilidad='" + disponibilidad + '\'' +
              '}';
   }
}

