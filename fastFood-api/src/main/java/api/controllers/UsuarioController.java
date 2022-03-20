package api.controllers;

import api.exception.ResourceNotFoundException;
import api.models.Categoria;
import api.models.Usuario;
import api.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
// @CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("api/usuarios")
public class UsuarioController {

   @Autowired
   private UsuarioRepository usuarioRepository;

   @GetMapping
   public ResponseEntity<List<Usuario>> getUsuarios() {
      List<Usuario> usuarios = usuarioRepository.findAll();
      return ResponseEntity.ok(usuarios);
   }

   @GetMapping(value = "{usuariosId}")
   public ResponseEntity<Usuario> getUsuarioById(@PathVariable("usuariosId") Long usuariosId) {
      Optional<Usuario> optionalUsuario = usuarioRepository.findById(usuariosId);
      return optionalUsuario.map(ResponseEntity::ok).orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", usuariosId));
   }

   @PostMapping
   public ResponseEntity<Usuario> createUsuario(@RequestBody Usuario usuario) {
      Usuario newUsuario = usuarioRepository.save(usuario);
      return ResponseEntity.ok(newUsuario);
   }

   @PutMapping(value = "{userId}")
   public ResponseEntity<List<Usuario>> updateUsuario(@PathVariable(value = "userId") Long usuarioId,
                                                  @Valid @RequestBody Usuario usuarioDetails) throws ResourceNotFoundException {
      Usuario usuario = usuarioRepository.findById(usuarioId)
              .orElseThrow(() -> new ResourceNotFoundException("Usuario not found for this id :: " + usuarioId));
      try {
         usuario.setNombre(usuarioDetails.getNombre());
         usuario.setPassword(usuarioDetails.getPassword());
         usuario.setDireccion(usuarioDetails.getDireccion());
         usuario.setEmail(usuarioDetails.getEmail());
         usuario.setRol(usuarioDetails.getRol());
         usuario.setTelefono(usuarioDetails.getTelefono());
         final Usuario updatedUsuario = usuarioRepository.save(usuario);
         List<Usuario> usuarios = usuarioRepository.findAll();
         return ResponseEntity.ok(usuarios);
      }catch (Exception e) {
         return ResponseEntity.notFound().build();
      }
   }

   @RequestMapping(value = "actualizar/{id}", method = RequestMethod.PATCH)
   public ResponseEntity<Usuario> saveManager(@PathVariable(value = "id") Long usuarioId, @RequestBody Map<String, Object> fields) {
      Usuario usuario = usuarioRepository.findById(usuarioId)
              .orElseThrow(() -> new ResourceNotFoundException("Usuario not found for this id :: " + usuarioId));
      try {
         // Map key is field name, v is value
         fields.forEach((k, v) -> {
            // use reflection to get field k on manager and set it to value v
            Field field = ReflectionUtils.findField(Usuario.class, k);
            assert field != null;
            field.setAccessible(true);
            ReflectionUtils.setField(field, usuario, v);
         });
         usuarioRepository.save(usuario);
         return ResponseEntity.ok(usuario);
      } catch (Exception e) {
         return ResponseEntity.notFound().build();
      }
   }

   @DeleteMapping(value = "{categoriaId}")
   public void deleteUsuario(@PathVariable("categoriaId") Long categoriaId) {
      usuarioRepository.deleteById(categoriaId);
   }
}
