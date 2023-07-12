package com.example.demo.DTO;

import com.example.demo.models.Seller;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class DTOSeller {
    private static ModelMapper modelMapper = new ModelMapper();

    private long id;

    private String username;

    private String address;

    private double rate;

    public static DTOSeller convertToDTO(Seller seller){
        return modelMapper.map(seller, DTOSeller.class);
    }

    public static Seller convertToSeller(DTOSeller dtoSeller){
        return modelMapper.map(dtoSeller, Seller.class);
    }

}
