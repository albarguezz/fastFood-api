package api.controllers;

import api.exception.ResourceNotFoundException;
import api.models.Pedido;
import api.repositories.PedidoRepository;

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
      Pedido newPedido = pedidoRepository.save(pedido);
      return ResponseEntity.ok(newPedido);
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
