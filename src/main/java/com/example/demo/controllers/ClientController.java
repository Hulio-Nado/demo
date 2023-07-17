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

    @ResponseBody
    @GetMapping("/{id}/add")
    public ResponseEntity<?> add(@PathVariable int id,
                                 @RequestParam(required = false, defaultValue = "1") int quantity){
        return ResponseEntity.ok(clientService.addToBasket(id, quantity));
    }
}
