package api.controllers;

import api.exception.ResourceNotFoundException;
import api.models.Ingrediente;
import api.models.Menu;
import api.models.Pedido;
import api.models.Producto;
import api.repositories.IngredienteRepository;
import api.repositories.MenuRepository;
import api.repositories.PedidoRepository;

import api.repositories.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("api/pedidos")
public class PedidoController {

   @Autowired
   private PedidoRepository pedidoRepository;
   @Autowired
   private IngredienteRepository ingredienteRepository;
   @Autowired
   private ProductoRepository productoRepository;
   @Autowired
   private MenuRepository menuRepository;

   @GetMapping
   public ResponseEntity<List<Pedido>> getPedidos() {
      List<Pedido> pedidos = pedidoRepository.findAll();
      return ResponseEntity.ok(pedidos);
   }

  @RequestMapping(value = "{pedidoId}")
   public ResponseEntity<Pedido> getPedidoById(@PathVariable("pedidoId") Long pedidoId) {
      Optional<Pedido> optionalPedido = pedidoRepository.findById(pedidoId);
      return optionalPedido.map(ResponseEntity::ok).orElseThrow(() -> new ResourceNotFoundException("Pedido", "id", pedidoId));
   }

   @PostMapping
   public ResponseEntity<Pedido> createPedido(@RequestBody Pedido pedido) {
      try {
         Pedido newPedido = pedidoRepository.save(pedido);
         if(newPedido.getProductos().size() > 0) {
            for (Producto p: newPedido.getProductos()) {
               Optional<Producto> optionalProducto = productoRepository.findById(p.getId());
               if (optionalProducto.isPresent()) {
                  Producto product = optionalProducto.get();
                  for (Ingrediente i: product.getIngredientes()) {
                     if (i.getStock() > 0) {
                        i.setStock(i.getStock()-1);
                        ingredienteRepository.save(i);
                     } else {
                        p.setDisponibilidad("sin stock");
                        productoRepository.save(p);
                        System.out.println("No hay stock de este ingrediente" + i.getNombre());
                     }
                  }
               }
            }
         }
         if (newPedido.getMenus().size() > 0) {
            for (Menu m: newPedido.getMenus()) {
               Optional<Menu> optionalMenu = menuRepository.findById(m.getId());
               if (optionalMenu.isPresent()) {
                  Menu menu = optionalMenu.get();
                  for (Producto p: menu.getProductos()) {
                     for (Ingrediente i: p.getIngredientes()) {
                        if (i.getStock() > 0) {
                           i.setStock(i.getStock()-1);
                           ingredienteRepository.save(i);
                        } else {
                           p.setDisponibilidad("sin stock");
                           productoRepository.save(p);
                           System.out.println("No hay stock de este ingrediente" + i.getNombre());
                        }
                     }
                  }
               }
            }
         }
         return ResponseEntity.ok(newPedido);
      } catch (Exception e) {
         System.out.println(e);
         return ResponseEntity.badRequest().build();
      }
   }

   @PutMapping(value = "{id}")
   public ResponseEntity<Pedido> updatePedido(@PathVariable(value = "id") Long pedidoId,
                                              @Valid @RequestBody Pedido pedidoDetails) throws ResourceNotFoundException {
      Pedido pedido = pedidoRepository.findById(pedidoId)
              .orElseThrow(() -> new ResourceNotFoundException("Pedido not found for this id :: " + pedidoId));
      try {
         pedido.setUsuario(pedidoDetails.getUsuario());
         pedido.setProductos(pedidoDetails.getProductos());
         pedido.setEstado(pedidoDetails.getEstado());
         pedido.setMenus(pedidoDetails.getMenus());
         pedido.setPrecioTotal(pedidoDetails.getPrecioTotal());
         final Pedido updatedPedido = pedidoRepository.save(pedido);
         return ResponseEntity.ok(updatedPedido);
      } catch (Exception e) {
         return ResponseEntity.notFound().build();
      }
   }

   /**
    * Metodo request de tipo put que actualiza el estado del pedido, utilizado para la aplicacion de android del repartidor
    * @param pedidoId @Long identificador del pedido a actualizar
    * @param estadoPedido String del nuevo estado del pedido
    * @return Devuelve el objeto pedido si todo salio bien
    * @throws ResourceNotFoundException
    */
   @PutMapping("/update-estado/{id}")
   public ResponseEntity<Pedido> updateEstado(@PathVariable(value = "id") Long pedidoId,
                                              @Valid @RequestBody String estadoPedido) throws ResourceNotFoundException {
      Pedido pedido = pedidoRepository.findById(pedidoId)
              .orElseThrow(() -> new ResourceNotFoundException("Pedido not found for this id :: " + pedidoId));
      try {
         pedido.setEstado(estadoPedido);
         final Pedido updatedPedido = pedidoRepository.save(pedido);
         return ResponseEntity.ok(updatedPedido);
      } catch (Exception e) {
         return ResponseEntity.notFound().build();
      }
   }

   @RequestMapping(value = "actualizar/{id}", method = RequestMethod.PATCH)
   public ResponseEntity<Pedido> saveManager(@PathVariable(value = "id") Long pedidoId, @RequestBody Map<String, Object> fields) {
      Pedido pedido = pedidoRepository.findById(pedidoId)
              .orElseThrow(() -> new ResourceNotFoundException("Pedido not found for this id :: " + pedidoId));
      try {
         // Map key is field name, v is value
         fields.forEach((k, v) -> {
            // use reflection to get field k on manager and set it to value v
            Field field = ReflectionUtils.findField(Pedido.class, k);
            assert field != null;
            field.setAccessible(true);
            ReflectionUtils.setField(field, pedido, v);
         });
         pedidoRepository.save(pedido);
         return ResponseEntity.ok(pedido);
      } catch (Exception e) {
         return ResponseEntity.notFound().build();
      }
   }

   @DeleteMapping(value = "{pedidoId}")
   public void deletePedido(@PathVariable("pedidoId") Long pedidoId) {
      pedidoRepository.deleteById(pedidoId);
   }
}
