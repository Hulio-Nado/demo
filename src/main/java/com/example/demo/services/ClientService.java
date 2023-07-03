package com.example.demo.services;

import com.example.demo.DTO.DTORegistration;
import com.example.demo.models.Client;
import com.example.demo.models.Seller;
import com.example.demo.repo.ClientRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientService {

    private final ClientRepository repository;

    public ClientService(ClientRepository repository) {
        this.repository = repository;
    }

    public boolean isPresent(Long id) {
        Optional<Client> optional = repository.findById(id);
        if(optional.isPresent()){
            return true;
        } else return false;
    }

    public String save(DTORegistration request) {
        repository.save(request.convertToClient());

        return "Registration successful";
    }

    public String update(DTORegistration user) {
        Client client = getCurrentUser();
        client.setUsername(user.getUsername());
        client.setPassword(user.getPassword());
        client.setAddress(user.getAddress());
        client.setEmail(user.getEmail());
        repository.save(client);
        return "Update successful";
    }

    private Client getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return findByUsernameOrThrow(userDetails.getUsername());
    }

    public Client findByUsernameOrThrow(String username) {
        Optional<Client> optional = repository.findByUsername(username);
        return optional.orElseThrow(() -> new RuntimeException("User not found"));
    }

    //методы сервиса и вызов методов бд
}
