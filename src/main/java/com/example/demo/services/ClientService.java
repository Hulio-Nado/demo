package com.example.demo.services;

import com.example.demo.DTO.DTORegistration;
import com.example.demo.models.Client;
import com.example.demo.models.Seller;
import com.example.demo.repo.ClientRepository;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientService {

    private final ClientRepository repository;
    private final BCryptPasswordEncoder encoder;

    public ClientService(ClientRepository repository, BCryptPasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    public boolean isPresent(Long id) {
        Optional<Client> optional = repository.findById(id);
        if(optional.isPresent()){
            return true;
        } else return false;
    }

    public String save(DTORegistration request) {
        Client client = request.convertToClient();
        client.setPassword(encoder.encode(request.getPassword()));
        repository.save(client);
        return "Registration successful";
    }

    public String update(DTORegistration user) {
        Client client = getCurrentUser();
        client.setUsername(user.getUsername());
        client.setPassword(encoder.encode(user.getPassword()));
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
