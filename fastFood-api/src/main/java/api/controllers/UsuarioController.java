package api.controllers;

import api.exception.ResourceNotFoundException;
import api.models.Usuario;
import api.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/usuarios")
public class UsuarioController {

   @Autowired
   private UsuarioRepository usuarioRepository;

   @GetMapping
   public ResponseEntity<List<Usuario>> getUsuarios() {
      List<Usuario> usuarios = usuarioRepository.findAll();
      return ResponseEntity.ok(usuarios);
   }

   @RequestMapping(value = "{usuariosId}")
   public ResponseEntity<Usuario> getUsuarioById(@PathVariable("usuariosId") Long usuariosId) {
      Optional<Usuario> optionalUsuario = usuarioRepository.findById(usuariosId);
      return optionalUsuario.map(ResponseEntity::ok).orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", usuariosId));
   }

   @PostMapping
   public ResponseEntity<Usuario> createUsuario(@RequestBody Usuario usuario) {
      Usuario newUsuario = usuarioRepository.save(usuario);
      return ResponseEntity.ok(newUsuario);
   }

   @PutMapping
   public ResponseEntity<Usuario> updateUsuario(@RequestBody Usuario usuario) {
      Optional<Usuario> optionalUsuario = usuarioRepository.findById(usuario.getId());
      if (optionalUsuario.isPresent()) {
         Usuario updateUsuario = optionalUsuario.get();
         updateUsuario.setNombre(usuario.getNombre());
         usuarioRepository.save(updateUsuario);
         return ResponseEntity.ok(updateUsuario);
      } else {
         return ResponseEntity.notFound().build();
      }
   }

   @DeleteMapping(value = "{categoriaId}")
   public void deleteUsuario(@PathVariable("categoriaId") Long categoriaId) {
      usuarioRepository.deleteById(categoriaId);
   }
}
