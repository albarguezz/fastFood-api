package api.controllers;

import api.exception.ResourceNotFoundException;
import api.models.Ingrediente;
import api.models.Menu;
import api.models.Producto;
import api.repositories.IngredienteRepository;
import api.repositories.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("api/productos")
public class ProductoController {

   @Autowired
   private ProductoRepository productoRepository;
   @Autowired
   private IngredienteRepository ingredienteRepository;


   @GetMapping
   public ResponseEntity<List<Producto>> getProductos() {
      List<Producto> productos = productoRepository.findAll();
      try {
         for (Producto p: productos) {
            if (p.getIngredientes().size() > 0) {
               for (Ingrediente i: p.getIngredientes()) {
                  if(i.getStock() == 0) {
                     p.setDisponibilidad("sin stock");
                  }
               }
               productoRepository.save(p);
            }
         }
         return ResponseEntity.ok(productos);
      } catch (Exception e) {
         return ResponseEntity.internalServerError().build();
      }
   }

   @RequestMapping(value = "{productoId}")
   public ResponseEntity<Producto> getProductoById(@PathVariable("productoId") Long productoId) {
      Optional<Producto> optionalProducto = productoRepository.findById(productoId);
      return optionalProducto.map(ResponseEntity::ok).orElseThrow(() -> new ResourceNotFoundException("Producto", "id", productoId));
   }

   @PostMapping
   public ResponseEntity<Producto> createProducto(@RequestBody Producto producto) {
      try {
         Producto newProducto = productoRepository.save(producto);
         return ResponseEntity.ok(newProducto);
      } catch (Exception e) {
         System.out.println(e);
         return ResponseEntity.badRequest().build();
      }
   }

   @PutMapping(value = "{id}")
   public ResponseEntity<List<Producto>> updateMenu(@PathVariable(value = "id") Long productoId, @RequestBody Producto menuUpdate) {
      Producto updateProduct = productoRepository.findById(productoId)
              .orElseThrow(() -> new ResourceNotFoundException("Menu not found for this id :: " + productoId));
      try {
         updateProduct.setNombre(menuUpdate.getNombre());
         updateProduct.setPrecio(menuUpdate.getPrecio());
         updateProduct.setCategoria(menuUpdate.getCategoria());
         updateProduct.setIngredientes(menuUpdate.getIngredientes());
         updateProduct.setDisponibilidad(menuUpdate.getDisponibilidad());
         productoRepository.save(updateProduct);
         List<Producto> productos = productoRepository.findAll();
         return ResponseEntity.ok(productos);
      } catch (Exception e) {
         return ResponseEntity.notFound().build();
      }
   }

   @RequestMapping(value = "actualizar/{id}", method = RequestMethod.PATCH)
   public ResponseEntity<Producto> saveManager(@PathVariable(value = "id") Long productoId, @RequestBody Map<String, Object> fields) {
      Producto producto = productoRepository.findById(productoId)
              .orElseThrow(() -> new ResourceNotFoundException("Usuario not found for this id :: " + productoId));
      try {
         // Map key is field name, v is value
         fields.forEach((k, v) -> {
            // use reflection to get field k on manager and set it to value v
            Field field = ReflectionUtils.findField(Producto.class, k);
            assert field != null;
            field.setAccessible(true);
            ReflectionUtils.setField(field, producto, v);
         });
         productoRepository.save(producto);
         return ResponseEntity.ok(producto);
      } catch (Exception e) {
         return ResponseEntity.notFound().build();
      }
   }

   /**
    * Funcion que realiza el delete de usuario
    * @param productoId
    */
   @DeleteMapping(value = "{productoId}")
   public void deleteProducto(@PathVariable("productoId") Long productoId) {
         productoRepository.deleteById(productoId);
   }
}
