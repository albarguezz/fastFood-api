package api.controllers;

import api.exception.ResourceNotFoundException;
import api.models.Pedido;
import api.repositories.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

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
      return optionalPedido.map(ResponseEntity::ok).orElseThrow(() -> new ResourceNotFoundException("Categoria", "id", pedidoId));
   }

   @PostMapping
   public ResponseEntity<Pedido> createPedido(@RequestBody Pedido pedido) {
      Pedido newPedido = pedidoRepository.save(pedido);
      return ResponseEntity.ok(newPedido);
   }

   @PutMapping
   public ResponseEntity<Pedido> updatePedido(@RequestBody Pedido pedido) {
      Optional<Pedido> optionalPedido = pedidoRepository.findById(pedido.getId());
      if (optionalPedido.isPresent()) {
         Pedido updatePedido = optionalPedido.get();
         updatePedido.setEstado(pedido.getEstado());
         updatePedido.setPrecioTotal(pedido.getPrecioTotal());
         updatePedido.addProducto(pedido.getProductos().get(1));
         updatePedido.addMenu(pedido.getMenus().get(1));
         pedidoRepository.save(updatePedido);
         return ResponseEntity.ok(updatePedido);
      } else {
         return ResponseEntity.notFound().build();
      }
   }

   @DeleteMapping(value = "{pedidoId}")
   public void deletePedido(@PathVariable("pedidoId") Long pedidoId) {
      pedidoRepository.deleteById(pedidoId);
   }

}
