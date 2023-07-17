package com.example.demo.services;

import com.example.demo.DTO.DTORegistration;
import com.example.demo.DTO.DTOUpdate;
import com.example.demo.models.Client;
import com.example.demo.models.Seller;
import com.example.demo.repo.GoodsRepository;
import com.example.demo.repo.SellerRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SellerService {

    private final SellerRepository repository;
    private final GoodsRepository goodsRepository;
    private final BCryptPasswordEncoder encoder;

    public SellerService(SellerRepository repository,
                         BCryptPasswordEncoder encoder, GoodsRepository goodsRepository) {
        this.repository = repository;
        this.encoder = encoder;
        this.goodsRepository = goodsRepository;
    }

    public String save(DTORegistration request) {
        Seller seller = request.convertToSeller();
        seller.setPassword(encoder.encode(request.getPassword()));
        repository.save(seller);
        return "Registration successful";
    }

    public String update(DTOUpdate user) {
        Seller seller = getCurrentUser();
        if(user.getUsername() != null){
            seller.setUsername(user.getUsername());
        }
        if(user.getPassword() != null){
            seller.setPassword(encoder.encode(user.getPassword()));
        }
        if(user.getEmail() != null){
            seller.setEmail(user.getEmail());
        }
        if(user.getAddress() != null){
            seller.setAddress(user.getAddress());
        }
        repository.save(seller);
        return "Update successful";

    }

    public Seller getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return findByUsernameOrThrow(userDetails.getUsername());
    }

    @Transactional(readOnly = true)
    public Seller findByUsernameOrThrow(String username) {
        Optional<Seller> optional = repository.findByUsername(username);
        return optional.orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional(readOnly = true)
    public List<?> findAll() {
        return goodsRepository.findAllBySeller(getCurrentUser());
    }

    //методы сервиса и вызов методов бд
}
