package api.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="pedido")
public class Pedido {
   @Id
   @Column(name = "id", nullable = false)
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(name = "estado", nullable = false)
   private String estado;

   @Column(name = "precio_total", nullable = false)
   private float precioTotal;

   @JoinColumn(name = "usuario", nullable = false)
   @ManyToOne(optional = false, cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
   private Usuario usuario;


   @JoinTable(
           name = "producto_pedido",
           joinColumns = @JoinColumn(name = "pedido_fk", nullable = false),
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

   @JoinTable(
           name = "menu_pedido",
           joinColumns = @JoinColumn(name = "pedido_fk", nullable = false),
           inverseJoinColumns = @JoinColumn(name="menu_fk", nullable = false)
   )
   @ManyToMany(cascade = CascadeType.MERGE)
   private List<Menu> menus;

   public void addMenu(Menu menu){
      if(this.menus == null){
         this.menus = new ArrayList<>();
      }
      this.menus.add(menu);
   }

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getEstado() {
      return estado;
   }

   public void setEstado(String estado) {
      this.estado = estado;
   }

   public float getPrecioTotal() {
      return precioTotal;
   }

   public void setPrecioTotal(float precioTotal) {
      this.precioTotal = precioTotal;
   }

   public List<Producto> getProductos() {
      return productos;
   }

   public void setProductos(List<Producto> productos) {
      this.productos = productos;
   }

   public List<Menu> getMenus() {
      return menus;
   }

   public void setMenus(List<Menu> menus) {
      this.menus = menus;
   }

   public Usuario getUsuario() {
      return usuario;
   }

   public void setUsuario(Usuario usuario) {
      this.usuario = usuario;
   }
}
