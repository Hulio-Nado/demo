package com.example.demo.DTO;

import com.example.demo.models.Client;
import com.example.demo.models.Seller;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DTOUpdate {
    @Size(min = 2, max = 100, message ="Username should be more then 2 characters")
    private String username;

    @Size(min = 2, max = 30, message ="Password should be more then 2 characters")
    private String password;

    private String address;

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
