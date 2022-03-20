package api.controllers;

import api.exception.ResourceNotFoundException;
import api.models.Menu;
import api.repositories.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
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

   @PutMapping(value = "{id}")
   public ResponseEntity<List<Menu>> updateMenu(@PathVariable(value = "id") Long menuId, @RequestBody Menu menuUpdate) {
      Menu menu = menuRepository.findById(menuId)
              .orElseThrow(() -> new ResourceNotFoundException("Menu not found for this id :: " + menuId));
      try {
         menu.setNombre(menuUpdate.getNombre());
         menu.setPrecio(menuUpdate.getPrecio());
         menu.setProductos(menuUpdate.getProductos());
         menu.setDescripcion(menuUpdate.getDescripcion());
         menu.setDisponibilidad(menuUpdate.getDisponibilidad());
         menuRepository.save(menu);
         List<Menu> menus = menuRepository.findAll();
         return ResponseEntity.ok(menus);
      } catch (Exception e) {
         return ResponseEntity.notFound().build();
      }
   }

   @RequestMapping(value = "actualizar/{id}", method = RequestMethod.PATCH)
   public ResponseEntity<Menu> saveManager(@PathVariable(value = "id") Long menuId, @RequestBody Map<String, Object> fields) {
      Menu menu = menuRepository.findById(menuId)
              .orElseThrow(() -> new ResourceNotFoundException("Usuario not found for this id :: " + menuId));
      try {
         // Map key is field name, v is value
         fields.forEach((k, v) -> {
            // use reflection to get field k on manager and set it to value v
            Field field = ReflectionUtils.findField(Menu.class, k);
            assert field != null;
            field.setAccessible(true);
            ReflectionUtils.setField(field, menu, v);
         });
         menuRepository.save(menu);
         return ResponseEntity.ok(menu);
      } catch (Exception e) {
         return ResponseEntity.notFound().build();
      }
   }

   @DeleteMapping(value = "{menuId}")
   public void deleteProducto(@PathVariable("productoId") Long menuId) {
      menuRepository.deleteById(menuId);
   }
}
