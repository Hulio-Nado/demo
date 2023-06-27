package com.example.demo.DTO;

import com.example.demo.models.Goods;
import com.example.demo.models.Seller;
import com.example.demo.utils.Category;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

//dto
@Getter
@Setter
public class DTOGood {
    private static ModelMapper modelMapper = new ModelMapper();

    private long id;
    @NotEmpty(message = "Имя должно быть не пустым")
    private String name;
    @Min(value = 0, message = "Цена должна быть больше нуля")
    private double price;
    @NotNull
    private Category category;

    private Seller seller;


    public static DTOGood convertToDTO(Goods goods){
        return modelMapper.map(goods, DTOGood.class);
    }

    public static Goods convertToGoods(DTOGood dtoGood){
        return modelMapper.map(dtoGood, Goods.class);
    }

    public static List<DTOGood> convertToDTOList(List<Goods> list){
        List<DTOGood> listDTO = new ArrayList<>();
        for(Goods goods : list) listDTO.add(convertToDTO(goods));
        return listDTO;
    }
}
