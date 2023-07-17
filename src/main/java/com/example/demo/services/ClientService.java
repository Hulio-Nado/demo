package com.example.demo.services;

import com.example.demo.DTO.DTOGood;
import com.example.demo.DTO.DTORegistration;
import com.example.demo.DTO.DTOUpdate;
import com.example.demo.models.Client;
import com.example.demo.models.ClientGood;
import com.example.demo.models.Good;
import com.example.demo.repo.ClientGoodRepository;
import com.example.demo.repo.ClientRepository;
import com.example.demo.utils.exceptions.ClientNotCreatedException;
import org.springframework.context.annotation.Lazy;
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
public class ClientService {

    private final ClientRepository clientRepository;
    private final BCryptPasswordEncoder encoder;
    private final GoodsService goodsService;
    private final ClientGoodRepository clientGoodRepository;

    public ClientService(ClientRepository clientRepository,
                         BCryptPasswordEncoder encoder, @Lazy GoodsService goodsService,
                         ClientGoodRepository clientGoodRepository) {
        this.clientRepository = clientRepository;
        this.encoder = encoder;
        this.goodsService = goodsService;
        this.clientGoodRepository = clientGoodRepository;
    }

    public String save(DTORegistration request) {
        Client client = request.convertToClient();
        checkFromDB(client);
        client.setPassword(encoder.encode(request.getPassword()));
        clientRepository.save(client);
        return "Registration successful";
    }


    private void checkFromDB(Client client) {
        Optional<Client> clientFromDBbyUsername = clientRepository.findByUsername(client.getUsername());
        if(clientFromDBbyUsername.isPresent()){
            throw new ClientNotCreatedException("User with this username already exists");
        }
        Optional<Client> clientFromDBbyEmail = clientRepository.findByEmail(client.getEmail());
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

        clientRepository.save(client);
        return "Update successful";
    }

    public Client getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return findByUsernameOrThrow(userDetails.getUsername());
    }

    @Transactional(readOnly = true)
    public Client findByUsernameOrThrow(String username) {
        Optional<Client> optional = clientRepository.findByUsername(username);
        return optional.orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<DTOGood> addToBasket(int id, int quantity) {
        Client client = getCurrentUser();
        Good good = goodsService.findByID2(id);
        //проверка на наличие уже таких товаров
        clientGoodRepository.save(new ClientGood(client, good, quantity));
        List <DTOGood> list = client.getBasket().stream().map(t -> {
            Good good2 = t.getGood();
            DTOGood dtoGood = DTOGood.convertToDTO(good2);
            dtoGood.setQuantity(t.getQuantity());
            return dtoGood;
        }).toList();

        return list;
    }

    //методы сервиса и вызов методов бд
}
