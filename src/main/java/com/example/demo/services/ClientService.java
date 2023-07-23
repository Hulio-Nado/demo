package com.example.demo.services;

import com.example.demo.DTO.DTOGood;
import com.example.demo.DTO.DTORegistration;
import com.example.demo.DTO.DTOUpdate;
import com.example.demo.models.Client;
import com.example.demo.models.ClientGood;
import com.example.demo.models.Good;
import com.example.demo.repo.ClientGoodRepository;
import com.example.demo.repo.ClientRepository;
import com.example.demo.repo.GoodsRepository;
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
    private final GoodsRepository goodsRepository;

    public ClientService(ClientRepository clientRepository,
                         BCryptPasswordEncoder encoder, @Lazy GoodsService goodsService,
                         ClientGoodRepository clientGoodRepository, GoodsRepository goodsRepository) {
        this.clientRepository = clientRepository;
        this.encoder = encoder;
        this.goodsService = goodsService;
        this.clientGoodRepository = clientGoodRepository;
        this.goodsRepository = goodsRepository;
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

    //метод частичного обновления информации о клиенте
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
        return "Обновлено успешно";
    }

    //метод получения текущего клиента из сессии
    public Client getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return findByUsernameOrThrow(userDetails.getUsername());
    }

    public Client findByUsernameOrThrow(String username) {
        Optional<Client> optional = clientRepository.findByUsername(username);
        return optional.orElseThrow(() -> new RuntimeException("Пользователь не найден"));
    }

    //метод добавления товара к корзину с заданным количеством
    @Transactional
    public List<DTOGood> addToBasket(long id, int quantity) {
        Client client = getCurrentUser();
        Optional<Good> optional = goodsRepository.findById(id);
        Good good = optional.orElseThrow(() -> new RuntimeException("Товар не найден"));
        int balance = good.getQuantity();

        // Проверяем наличие товара в корзине
        Optional<ClientGood> optionalClientGood = checkFromBasketByGoodAndClient(good, client);
        ClientGood existingClientGood = optionalClientGood.
                orElse(null);

        // Если товар уже есть в корзине, увеличиваем его количество на заданное значение
        if (existingClientGood != null && (balance - quantity) >= 0) {
            int newQuantity = existingClientGood.getQuantity() + quantity;
            existingClientGood.setQuantity(newQuantity);
            good.setQuantity(balance - quantity);
            goodsRepository.save(good);
            clientGoodRepository.save(existingClientGood);
        } else if ((balance - quantity) >= 0) {
            // Если товара нет в корзине, сохраняем его с указанным количеством
            clientGoodRepository.save(new ClientGood(client, good, quantity));
            good.setQuantity(balance - quantity);
            goodsRepository.save(good);
        } else {
            throw new RuntimeException("Нет запрошенного количества товара на складе");
        }
        return showBasket();
    }

    //метод частичного удаления товара из корзины
    public List<DTOGood> delFromBasket(long id, int quantity) {
        Client client = getCurrentUser();

        Optional<Good> optional = goodsRepository.findById(id);
        Good good = optional.orElseThrow(() -> new RuntimeException("Товар не найден"));
        int balance = good.getQuantity();

        // Проверяем наличие товара в корзине
        Optional<ClientGood> optionalClientGood = checkFromBasketByGoodAndClient(good, client);
        ClientGood existingClientGood = optionalClientGood.
                orElseThrow(() -> new RuntimeException("У вас нет такого товара в корзине"));

        // Если товар уже есть в корзине, уменьшаем его количество на заданное значение
        if (existingClientGood != null && existingClientGood.getQuantity() > 0) {
            int newQuantity = existingClientGood.getQuantity() - quantity;
            balance = balance + quantity;
            good.setQuantity(balance);
            goodsRepository.save(good);
            if(newQuantity == 0){
                clientGoodRepository.delete(existingClientGood);
                client.getBasket().remove(existingClientGood);
            } else{
                existingClientGood.setQuantity(newQuantity);
                clientGoodRepository.save(existingClientGood);
            }

        } else {
            // Если товара нет в корзине, его количество = нулю
            throw new RuntimeException("Товара с указанным ID нет в вашей корзине");
        }
        return showBasket();
    }

    //метод показа товаров в корзине
    public List<DTOGood> showBasket() {
        Client client = getCurrentUser();
        //взять юзера из айди каррентюзера - короче из БД по айди

        return client.getBasket().stream().map(t -> {
            Good good = t.getGood();
            DTOGood dtoGood = DTOGood.convertToDTO(good);
            dtoGood.setQuantity(t.getQuantity());
            return dtoGood;
        }).toList();
    }

    //метод чистки корзины с одновременным пополнением остатков тех товаров что лежали в корзине
    public List<DTOGood> clearBasket() {
        Client client = getCurrentUser();
        for (ClientGood clientGood : client.getBasket()) {
            Good good = clientGood.getGood();
            int quantityInBasket = clientGood.getQuantity();
            int currentQuantity = good.getQuantity();
            good.setQuantity(currentQuantity + quantityInBasket);
            goodsRepository.save(good);
        }

        client.getBasket().clear();
        clientRepository.save(client);

        List <ClientGood> list = checkFromBasketByClient(client);
        clientGoodRepository.deleteAll(list);
        client.setBasket(List.of());

        return showBasket();
    }

    private Optional<ClientGood> checkFromBasketByGoodAndClient(Good good, Client client) {
        return clientGoodRepository.findByClientAndGood(client, good);
    }

    private List <ClientGood> checkFromBasketByClient(Client client) {
        return clientGoodRepository.findByClient(client);
    }
}
