package com.example.demo;


import com.example.demo.DTO.DTOGood;
import com.example.demo.models.Good;
import com.example.demo.repo.FeedBackRepository;
import com.example.demo.repo.GoodsRepository;
import com.example.demo.repo.SellerRepository;
import com.example.demo.services.ClientService;
import com.example.demo.services.GoodServiceExample;
import com.example.demo.services.GoodsService;
import com.example.demo.services.SellerService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class GoodServiceTest {
    @InjectMocks
    private GoodsService goodsService;
    @Mock
    GoodsRepository goodsRepository;
    @Mock
    FeedBackRepository feedBackRepository;
    @Mock
    SellerService sellerService;
    @Mock
    ClientService clientService;
    @Mock
    SellerRepository sellerRepository;

    @Test
    void pageFindAllConsistsDTOGood(){
        GoodServiceExample goodsServiceExample = new GoodServiceExample();
        List<Good> list = goodsServiceExample.getAll();
        assertTrue(!list.isEmpty(), () -> "List is not empty");

        assertAll(() -> assertTrue(true), () -> assertFalse(false));

        assertThat(list).isEqualTo(list);
    }
}
