package com.example.demo.DTO;

import com.example.demo.models.FeedBack;
import com.example.demo.models.Seller;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class DTOFeedback {
    private static ModelMapper modelMapper = new ModelMapper();

    private DTOClient clientName;

    private String feedback;

    private int rate;

    private LocalDateTime created;

    public static DTOFeedback convertToDTO(FeedBack feedBack){
        DTOFeedback dtoFeedback = modelMapper.map(feedBack, DTOFeedback.class);
        DTOClient dtoClient = DTOClient.convertToDTO(feedBack.getClient());
        dtoFeedback.setClientName(dtoClient);
        return dtoFeedback;
    }

    public static FeedBack convertToFeedback(DTOFeedback dtoFeedback){
        return modelMapper.map(dtoFeedback, FeedBack.class);
    }

    public static List<DTOFeedback> convertToDTOList(List<FeedBack> listFeedbacks) {
        return listFeedbacks.stream().map(DTOFeedback::convertToDTO).toList();
    }
}
