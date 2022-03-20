package api.controllers;

import api.exception.ResourceNotFoundException;
import api.models.Categoria;
import api.models.Ingrediente;
import api.models.Usuario;
import api.repositories.IngredienteRepository;
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
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("api/ingredientes")
public class IngredienteController {

   @Autowired
   private IngredienteRepository ingredienteRepository;

   @GetMapping
   public ResponseEntity<List<Ingrediente>> getIngredientes() {
      List<Ingrediente> ingredientes = ingredienteRepository.findAll();
      return ResponseEntity.ok(ingredientes);
   }

   @RequestMapping(value = "{ingredieteId}")
   public ResponseEntity<Ingrediente> getIngredienteById(@PathVariable("ingredieteId") Long ingredieteId) {
      Optional<Ingrediente> optionalIngrediente = ingredienteRepository.findById(ingredieteId);
      return optionalIngrediente.map(ResponseEntity::ok).orElseThrow(() -> new ResourceNotFoundException("Ingrediente", "id", ingredieteId));
   }

   @PostMapping
   public ResponseEntity<Ingrediente> createIngrediente(@Valid @RequestBody Ingrediente ingrediente) {
      Ingrediente newIngrediente = ingredienteRepository.save(ingrediente);
      return ResponseEntity.ok(newIngrediente);
   }

   @PutMapping(value = "{ingredienteId}")
   public ResponseEntity<List<Ingrediente>> updateIngrediente(@PathVariable(value = "ingredienteId") Long ingredienteId,
                                              @Valid @RequestBody Ingrediente ingredienteUpdate) throws ResourceNotFoundException {
      Ingrediente ingrediente = ingredienteRepository.findById(ingredienteId)
              .orElseThrow(() -> new ResourceNotFoundException("Pedido not found for this id :: " + ingredienteId));
      try {
         ingrediente.setNombre(ingredienteUpdate.getNombre());
         ingrediente.setStock(ingredienteUpdate.getStock());
         final Ingrediente updatedIngrediente = ingredienteRepository.save(ingrediente);
         List<Ingrediente> ingredientes = ingredienteRepository.findAll();
         return ResponseEntity.ok(ingredientes);
      } catch (Exception e) {
         return ResponseEntity.notFound().build();
      }
   }

   @RequestMapping(value = "actualizar/{id}", method = RequestMethod.PATCH)
   public ResponseEntity<Ingrediente> saveManager(@PathVariable(value = "id") Long ingredienteId, @RequestBody Map<String, Object> fields) {
      Ingrediente ingrediente = ingredienteRepository.findById(ingredienteId)
              .orElseThrow(() -> new ResourceNotFoundException("Usuario not found for this id :: " + ingredienteId));
      try {
         // Map key is field name, v is value
         fields.forEach((k, v) -> {
            // use reflection to get field k on manager and set it to value v
            Field field = ReflectionUtils.findField(Ingrediente.class, k);
            assert field != null;
            field.setAccessible(true);
            ReflectionUtils.setField(field, ingrediente, v);
         });
         ingredienteRepository.save(ingrediente);
         return ResponseEntity.ok(ingrediente);
      } catch (Exception e) {
         return ResponseEntity.notFound().build();
      }
   }

   @DeleteMapping(value = "{ingredienteId}")
   public void deleteIngrediente(@PathVariable("ingredienteId") Long ingredienteId) {
      ingredienteRepository.deleteById(ingredienteId);
   }
}
