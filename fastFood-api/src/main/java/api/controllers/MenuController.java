package api.controllers;

import api.exception.ResourceNotFoundException;
import api.models.Menu;
import api.models.Producto;
import api.repositories.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/menus")
public class MenuController {

   @Autowired
   private MenuRepository menuRepository;

   @GetMapping
   public ResponseEntity<List<Menu>> getMenus() {
      List<Menu> menus = menuRepository.findAll();
      return ResponseEntity.ok(menus);
   }

   @RequestMapping(value = "{menuId}")
   public ResponseEntity<Menu> getMenuById(@PathVariable("menuId") Long menuId) {
      Optional<Menu> optionalMenu = menuRepository.findById(menuId);
      return optionalMenu.map(ResponseEntity::ok).orElseThrow(() -> new ResourceNotFoundException("Producto", "id", menuId));
   }

   @PostMapping
   public ResponseEntity<Menu> createMenu(@RequestBody Menu menu) {
      Menu newMenu = menuRepository.save(menu);
      return ResponseEntity.ok(newMenu);
   }

   @PutMapping
   public ResponseEntity<Menu> updateMenu(@RequestBody Menu menu) {
      Optional<Menu> optionalMenu = menuRepository.findById(menu.getId());
      if (optionalMenu.isPresent()) {
         Menu updateMenu = optionalMenu.get();
         updateMenu.setNombre(menu.getNombre());
         updateMenu.setPrecio(menu.getPrecio());
         updateMenu.setProductos(menu.getProductos());
         menuRepository.save(updateMenu);
         return ResponseEntity.ok(updateMenu);
      } else {
         return ResponseEntity.notFound().build();
      }
   }

   @DeleteMapping(value = "{menuId}")
   public void deleteProducto(@PathVariable("productoId") Long menuId) {
      menuRepository.deleteById(menuId);
   }
}
