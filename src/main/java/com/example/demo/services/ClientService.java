package com.example.demo.services;

import com.example.demo.DTO.DTORegistration;
import com.example.demo.DTO.DTOUpdate;
import com.example.demo.models.Client;
import com.example.demo.models.Seller;
import com.example.demo.repo.ClientRepository;
import com.example.demo.utils.exceptions.ClientNotCreatedException;
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
        checkFromDB(client);
        client.setPassword(encoder.encode(request.getPassword()));
        repository.save(client);
        return "Registration successful";
    }

    private void checkFromDB(Client client) {
        Optional<Client> clientFromDBbyUsername = repository.findByUsername(client.getUsername());
        if(clientFromDBbyUsername.isPresent()){
            throw new ClientNotCreatedException("User with this username already exists");
        }
        Optional<Client> clientFromDBbyEmail = repository.findByEmail(client.getEmail());
        if(clientFromDBbyEmail.isPresent()){
            throw new ClientNotCreatedException("User with this email already exists");
        }
    }

    public String update(DTOUpdate user) {
        Client client = getCurrentUser();
        if(user.getUsername() != null){
            client.setUsername(user.getUsername());
        }
        if(user.getPassword() != null){
            client.setPassword(encoder.encode(user.getPassword()));
        }
        if(user.getEmail() != null){
            client.setEmail(user.getEmail());
        }
        if(user.getAddress() != null){
            client.setAddress(user.getAddress());
        }

        repository.save(client);
        return "Update successful";
    }

    public Client getCurrentUser() {
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
