package com.example.demo.security;

import com.example.demo.DTO.DTORegistration;
import com.example.demo.models.Client;
import com.example.demo.models.Seller;
import com.example.demo.repo.ClientRepository;
import com.example.demo.repo.SellerRepository;
import com.example.demo.services.ClientService;
import com.example.demo.services.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final ClientRepository clientRepository;
    private final SellerRepository sellerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Client> client = clientRepository.findByUsername(username);
        Optional<Seller> seller = sellerRepository.findByUsername(username);

        System.out.println(client);
        System.out.println(seller);
        if (client.isPresent()) {
            return new org.springframework.security.core.userdetails.User(client.get().getUsername(),
                    client.get().getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("CLIENT")));
        } else if (seller.isPresent()) {
            return new org.springframework.security.core.userdetails.User(seller.get().getUsername(),
                    seller.get().getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("SELLER")));
        } else throw new UsernameNotFoundException("User not found");
    }

}
