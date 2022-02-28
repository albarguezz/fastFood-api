package api.models;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "repartidor")
public class Repartidor {

   @Id
   @Column(name = "id", nullable = false)
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(name = "nombre", nullable = false, length = 50)
   private String nombre;

   @Column(name = "email", nullable = false, length = 100)
   private String email;

   @Column(name = "contraseña", nullable = false, length = 50)
   private String contraseña;

   @Column(name = "direccion", length = 50)
   private String direccion;

   @Column(name = "telefono", nullable = false)
   private int telefono;

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

   public String getContraseña() {
      return contraseña;
   }

   public void setContraseña(String contraseña) {
      this.contraseña = contraseña;
   }

   public String getDireccion() {
      return direccion;
   }

   public void setDireccion(String direccion) {
      this.direccion = direccion;
   }

   public int getTelefono() {
      return telefono;
   }

   public void setTelefono(int telefono) {
      this.telefono = telefono;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Repartidor that = (Repartidor) o;
      return telefono == that.telefono && Objects.equals(id, that.id) && Objects.equals(nombre, that.nombre) && Objects.equals(email, that.email) && Objects.equals(contraseña, that.contraseña) && Objects.equals(direccion, that.direccion);
   }

   @Override
   public int hashCode() {
      return Objects.hash(id, nombre, email, contraseña, direccion, telefono);
   }

   @Override
   public String toString() {
      return "Repartidor{" +
              "id=" + id +
              ", nombre='" + nombre + '\'' +
              ", email='" + email + '\'' +
              ", contraseña='" + contraseña + '\'' +
              ", direccion='" + direccion + '\'' +
              ", telefono=" + telefono +
              '}';
   }
}
