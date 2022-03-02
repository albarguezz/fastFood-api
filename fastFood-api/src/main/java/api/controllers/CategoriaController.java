package api.controllers;

import api.exception.ResourceNotFoundException;
import api.models.Categoria;
import api.repositories.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
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

    @RequestMapping(value = "{categoriaId}")
    public ResponseEntity<Categoria> getCategoriaById(@PathVariable("categoriaId") Long categoriaId) {
        Optional<Categoria> optionalCategoria = categoriaRepository.findById(categoriaId);
        return optionalCategoria.map(ResponseEntity::ok).orElseThrow(() -> new ResourceNotFoundException("Categoria", "id", categoriaId));
    }

    @PostMapping
    public ResponseEntity<Categoria> createCategoria(@RequestBody Categoria categoria) {
        Categoria newCategoria = categoriaRepository.save(categoria);
        return ResponseEntity.ok(newCategoria);
    }

    @PutMapping
    public ResponseEntity<Categoria> updateCategoria(@RequestBody Categoria categoria) {
        Optional<Categoria> optionalCategoria = categoriaRepository.findById(categoria.getId());
        if (optionalCategoria.isPresent()) {
            Categoria updateCategoria = optionalCategoria.get();
            updateCategoria.setNombre(categoria.getNombre());
            categoriaRepository.save(updateCategoria);
            return ResponseEntity.ok(updateCategoria);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(value = "{categoriaId}")
    public void deleteCategoria(@PathVariable("categoriaId") Long categoriaId) {
        categoriaRepository.deleteById(categoriaId);
    }
}
