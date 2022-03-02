package api.controllers;

import api.models.Menu;
import api.repositories.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/menus")
public class MenuController {

   @Autowired
   private MenuRepository menuRepository;

   @GetMapping
   public ResponseEntity<List<Menu>> getProductos() {
      List<Menu> menus = menuRepository.findAll();
      return ResponseEntity.ok(menus);
   }
}
