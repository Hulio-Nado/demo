package com.example.demo.controllers;

import com.example.demo.DTO.DTORegistration;
import com.example.demo.services.ClientService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/client")
public class ClientController {
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/register")
    public String registration(@ModelAttribute("person") DTORegistration request){
        return "registrationForm";
    }

    @ResponseBody
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid DTORegistration request) {
        return ResponseEntity.ok(clientService.save(request));
    }

    @ResponseBody
    @PatchMapping
    public ResponseEntity<?> updateUser(@RequestBody DTORegistration request) {
        return ResponseEntity.ok(clientService.update(request));
    }
}
