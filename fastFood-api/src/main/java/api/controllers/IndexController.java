package api.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class IndexController {

   @GetMapping
   public String sayHello() {
      return "Hola a mi api de un restaurante en Spring Boot";
   }
}
