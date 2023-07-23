package com.example.demo.controllers;

import com.example.demo.DTO.DTORegistration;
import com.example.demo.DTO.DTOUpdate;
import com.example.demo.services.ClientService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/client")
public class ClientController {
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/reg")
    public String registration(@ModelAttribute("person") DTORegistration request){
        return "regform.html";
    }

    @ResponseBody
    @PostMapping("/reg")
    public ResponseEntity<?> registerUser(@Valid DTORegistration request) {
        System.out.println(request);
        return ResponseEntity.ok(clientService.save(request));
    }

    @ResponseBody
    @PatchMapping
    @Secured("CLIENT")
    public ResponseEntity<?> updateUser(@RequestBody @Valid DTOUpdate request) {
        System.out.println(request);
        return ResponseEntity.ok(clientService.update(request));
    }

    //метод добавления конкретного товара в корзину
    @ResponseBody
    @GetMapping("/{id}/add")
    @Secured("CLIENT")
    public ResponseEntity<?> add(@PathVariable int id,
                                 @RequestParam(required = false, defaultValue = "1") int q){
        return ResponseEntity.ok(clientService.addToBasket(id, q));
    }

    @ResponseBody
    @GetMapping("/{id}/del")
    @Secured("CLIENT")
    public ResponseEntity<?> del(@PathVariable int id,
                                 @RequestParam(required = false, defaultValue = "1") int q){
        return ResponseEntity.ok(clientService.delFromBasket(id, q));
    }

    @ResponseBody
    @GetMapping("/basket")
    @Secured("CLIENT")
    public ResponseEntity<?> showBasket(){
        return ResponseEntity.ok(clientService.showBasket());
    }

    @ResponseBody
    @GetMapping("/basket/clear")
    @Secured("CLIENT")
    public ResponseEntity<?> clearBasket(){
        return ResponseEntity.ok(clientService.clearBasket());
    }
}
