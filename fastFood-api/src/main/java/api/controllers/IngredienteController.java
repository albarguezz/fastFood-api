package api.controllers;

import api.exception.ResourceNotFoundException;
import api.models.Categoria;
import api.models.Ingrediente;
import api.repositories.CategoriaRepository;
import api.repositories.IngredienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
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
   public ResponseEntity<Ingrediente> createIngrediente(@RequestBody Ingrediente ingrediente) {
      Ingrediente newIngrediente = ingredienteRepository.save(ingrediente);
      return ResponseEntity.ok(newIngrediente);
   }

   @PutMapping
   public ResponseEntity<Ingrediente> updateIngrediente(@RequestBody Ingrediente ingrediente) {
      Optional<Ingrediente> optionalIngrediente = ingredienteRepository.findById(ingrediente.getId());
      if (optionalIngrediente.isPresent()) {
         Ingrediente updateIngrediente = optionalIngrediente.get();
         updateIngrediente.setNombre(ingrediente.getNombre());
         ingredienteRepository.save(updateIngrediente);
         return ResponseEntity.ok(updateIngrediente);
      } else {
         return ResponseEntity.notFound().build();
      }
   }

   @DeleteMapping(value = "{ingredienteId}")
   public void deleteIngrediente(@PathVariable("ingredienteId") Long ingredienteId) {
      ingredienteRepository.deleteById(ingredienteId);
   }
}
