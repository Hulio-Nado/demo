package com.example.demo.DTO;

import com.example.demo.models.Good;
import com.example.demo.utils.Category;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

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

    private DTOSeller seller;

    private double rate;

    public static DTOGood convertToDTO(Good good){
        DTOGood dtoGood = modelMapper.map(good, DTOGood.class);
        DTOSeller dtoSeller = DTOSeller.convertToDTO(good.getSeller());
        dtoGood.setSeller(dtoSeller);
        return dtoGood;
    }

    public static Good convertToGoods(DTOGood dtoGood){
        return modelMapper.map(dtoGood, Good.class);
    }

    public static List<DTOGood> convertToDTOList(List<Good> list){
        List<DTOGood> listDTO = new ArrayList<>();
        for(Good good : list) listDTO.add(convertToDTO(good));
        return listDTO;
    }

    public static Page<DTOGood> convertToDTOPage(Page<Good> resultPage) {
        return resultPage.map(DTOGood::convertToDTO);
    }

}
