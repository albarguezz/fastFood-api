package api.controllers;

import api.exception.ResourceNotFoundException;
import api.models.Categoria;
import api.models.Ingrediente;
import api.models.Pedido;
import api.repositories.CategoriaRepository;
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
@RequestMapping("api/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @GetMapping
    public ResponseEntity<List<Categoria>> getCategorias() {
        List<Categoria> categorias = categoriaRepository.findAll();
        System.out.println(categorias);
        return ResponseEntity.ok(categorias);
    }

    @GetMapping(value = "{categoriaId}")
    public ResponseEntity<Categoria> getCategoriaById(@PathVariable("categoriaId") Long categoriaId) {
        Optional<Categoria> optionalCategoria = categoriaRepository.findById(categoriaId);
        return optionalCategoria.map(ResponseEntity::ok).orElseThrow(() -> new ResourceNotFoundException("Categoria", "id", categoriaId));
    }

    @PostMapping
    public ResponseEntity<Categoria> createCategoria(@RequestBody Categoria categoria) {
        Categoria newCategoria = categoriaRepository.save(categoria);
        return ResponseEntity.ok(newCategoria);
    }


    @PutMapping(value = "{categoriaId}")
    public ResponseEntity<List<Categoria>> updateCategoria(@PathVariable(value = "categoriaId") Long categoriaId,
                                                         @Valid @RequestBody Categoria categoriaUpdate) throws ResourceNotFoundException {
        Categoria categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria not found for this id :: " + categoriaId));
        try {
            categoria.setNombre(categoriaUpdate.getNombre());
            categoria.setDescripcion(categoriaUpdate.getDescripcion());
            final Categoria updatedCategoria = categoriaRepository.save(categoria);
            List<Categoria> categorias = categoriaRepository.findAll();
            return ResponseEntity.ok(categorias);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(value = "actualizar/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<Categoria> saveManager(@PathVariable(value = "id") Long categoriaId, @RequestBody Map<String, Object> fields) {
        Categoria categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido not found for this id :: " + categoriaId));
        try {
            // Map key is field name, v is value
            fields.forEach((k, v) -> {
                // use reflection to get field k on manager and set it to value v
                Field field = ReflectionUtils.findField(Categoria.class, k);
                assert field != null;
                field.setAccessible(true);
                ReflectionUtils.setField(field, categoria, v);
            });
            categoriaRepository.save(categoria);
            return ResponseEntity.ok(categoria);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(value = "{categoriaId}")
    public void deleteCategoria(@PathVariable("categoriaId") Long categoriaId) {
        categoriaRepository.deleteById(categoriaId);
    }
}
