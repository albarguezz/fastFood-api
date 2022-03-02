package api.controllers;

import api.exception.ResourceNotFoundException;
import api.models.Categoria;
import api.models.Producto;
import api.repositories.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/productos")
public class ProductoController {

   @Autowired
   private ProductoRepository productoRepository;

   @GetMapping
   public ResponseEntity<List<Producto>> getProductos() {
      List<Producto> productos = productoRepository.findAll();
      return ResponseEntity.ok(productos);
   }


   @RequestMapping(value = "{productoId}")
   public ResponseEntity<Producto> getProductoById(@PathVariable("productoId") Long productoId) {
      Optional<Producto> optionalProducto = productoRepository.findById(productoId);
      return optionalProducto.map(ResponseEntity::ok).orElseThrow(() -> new ResourceNotFoundException("Categoria", "id", productoId));
   }

   @PostMapping
   public ResponseEntity<Producto> createProducto(@RequestBody Producto producto) {
      Producto newProducto = productoRepository.save(producto);
      return ResponseEntity.ok(newProducto);
   }

   @PutMapping
   public ResponseEntity<Producto> updateProducto(@RequestBody Producto producto) {
      Optional<Producto> optionalProducto = productoRepository.findById(producto.getId());
      if (optionalProducto.isPresent()) {
         Producto updateProduct = optionalProducto.get();
         updateProduct.setNombre(producto.getNombre());
         updateProduct.setCategoria(producto.getCategoria());
         updateProduct.setPrecio(producto.getPrecio());
         productoRepository.save(updateProduct);
         return ResponseEntity.ok(updateProduct);
      } else {
         return ResponseEntity.notFound().build();
      }
   }

   @DeleteMapping(value = "{productoId}")
   public void deleteProducto(@PathVariable("productoId") Long productoId) {
      productoRepository.deleteById(productoId);
   }
}
