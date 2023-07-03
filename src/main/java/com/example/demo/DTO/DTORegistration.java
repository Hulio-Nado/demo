package com.example.demo.DTO;

import com.example.demo.models.Client;
import com.example.demo.models.Seller;
import com.example.demo.utils.exceptions.NotOurRoleException;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DTORegistration {
    @NotEmpty(message ="Username mustnt be empty")
    @Size(min = 2, max = 100, message ="Username should be more then 2 characters")
    private String username;
    @NotEmpty(message ="Password mustnt be empty")
    @NotNull
    @Size(min = 2, max = 30, message ="Password should be more then 2 characters")
    private String password;
    @NotNull
    private String address;
    @NotEmpty
    @Email
    private String email;

    public Seller convertToSeller(){
        Seller seller = new Seller();
        seller.setUsername(username);
        seller.setPassword(password);
        seller.setEmail(email);
        seller.setAddress(address);

        return seller;
    }

    public Client convertToClient(){
        Client client = new Client();
        client.setUsername(username);
        client.setPassword(password);
        client.setEmail(email);
        client.setAddress(address);

        return client;
    }
}
//это входящие даннык в наше приложение, поэтому мы тут валидируем данные