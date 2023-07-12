package com.example.demo.DTO;

import com.example.demo.models.Client;
import com.example.demo.models.Seller;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class DTOClient {
    private static ModelMapper modelMapper = new ModelMapper();

    private String name;

    public static DTOClient convertToDTO(Client client){
        return modelMapper.map(client, DTOClient.class);
    }

    public static Client convertToClient(DTOClient dtoClient){
        return modelMapper.map(dtoClient, Client.class);
    }
}
