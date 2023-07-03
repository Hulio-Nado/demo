package com.example.demo.services;

import com.example.demo.DTO.DTORegistration;
import com.example.demo.models.Client;
import com.example.demo.models.Seller;
import com.example.demo.repo.SellerRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SellerService {

    private final SellerRepository repository;

    public SellerService(SellerRepository repository) {
        this.repository = repository;
    }

    public boolean isPresent(Long id) {
        Optional<Seller> optional = repository.findById(id);
        if(optional.isPresent()){
            return true;
        } else return false;
    }

    public String save(DTORegistration request, String role) {
        //sellerRepository.save()
        return "Registration successful";
    }

    public void update(Long id, DTORegistration user) {
        Optional<Seller> optional = repository.findById(id);
        DTORegistration user1 = optional.orElseThrow(() -> new RuntimeException("Пользователь с данным ID не найден"));//лямбда
        /*user.setUsername(user1.getUsername());
        user.setPassword(user1.getPassword());

        userRepository.save(user);*/
    }

    public DTORegistration findByUsernameOrThrow(String username) {
        Optional<Seller> optional = repository.findByUsername(username);
        return null;
    }

    //методы сервиса и вызов методов бд
}
