package api.models;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="usuario")
public class Usuario {
   @Id
   @Column(name = "id", nullable = false)
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(name = "nombre", nullable = false)
   private String nombre;

   @Column(name = "email", nullable = false)
   private String email;

   @Column(name = "password", nullable = false)
   private String password;

   @Column(name = "direccion", nullable = true)
   private String direccion;

   @Column(name = "telefono", nullable = true)
   private String telefono;

   @Column(name = "rol", nullable = false)
   private String rol;

   @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuario")
   private List<Pedido> pedidos;


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

   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public String getPassword() {
      return password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public String getDireccion() {
      return direccion;
   }

   public void setDireccion(String direccion) {
      this.direccion = direccion;
   }

   public String getTelefono() {
      return telefono;
   }

   public void setTelefono(String telefono) {
      this.telefono = telefono;
   }

   public String getRol() {
      return rol;
   }

   public void setRol(String rol) {
      this.rol = rol;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Usuario usuario = (Usuario) o;
      return id.equals(usuario.id) && Objects.equals(email, usuario.email);
   }

   @Override
   public int hashCode() {
      return Objects.hash(id, email);
   }

   @Override
   public String toString() {
      return "Usuario{" +
              "id=" + id +
              ", nombre='" + nombre + '\'' +
              ", email='" + email + '\'' +
              ", password='" + password + '\'' +
              ", direccion='" + direccion + '\'' +
              ", telefono='" + telefono + '\'' +
              ", rol='" + rol + '\'' +
              '}';
   }
}
