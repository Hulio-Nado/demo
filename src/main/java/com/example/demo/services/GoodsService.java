package com.example.demo.services;

import com.example.demo.DTO.DTOGood;
import com.example.demo.models.Client;
import com.example.demo.models.FeedBack;
import com.example.demo.models.Goods;
import com.example.demo.repo.FeedBackRepository;
import com.example.demo.repo.GoodsRepository;
import com.example.demo.utils.Category;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class GoodsService {
    private final GoodsRepository goodsRepository;
    private final FeedBackRepository feedBackRepository;
    private final SellerService sellerService;
    private final ClientService clientService;

    public GoodsService(GoodsRepository goodsRepository, FeedBackRepository feedBackRepository,
                        SellerService sellerService, ClientService clientService) {
        this.goodsRepository = goodsRepository;
        this.feedBackRepository = feedBackRepository;
        this.sellerService = sellerService;
        this.clientService = clientService;
    }

    public Page<DTOGood> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Goods> resultPage = goodsRepository.findAll(pageable);

        //DTOGood.convertToDTOList(goodsRepository.findAll());

        return DTOGood.convertToDTOPage(resultPage);
    }

    public DTOGood findByID(long id) {
        Optional<Goods> optional = goodsRepository.findById(id);
        Goods goods = optional.orElseThrow(() -> new RuntimeException("Товар с данным ID не найден"));
        return DTOGood.convertToDTO(goods);
    }

    public List<DTOGood> findByCategory(Category category) {
        List<Goods> list = goodsRepository.findByCategory(category);
        return DTOGood.convertToDTOList(list);
    }

    public List<DTOGood> findByName(String name) {
        System.out.println(name);
        List<Goods> list = goodsRepository.findByName(name);
        System.out.println(list);
        return DTOGood.convertToDTOList(list);
    }

    public void save(Goods goods) {
        goods.setRate(0);
        goods.setCountRates(0);
        goods.setSeller(sellerService.getCurrentUser());
        goodsRepository.save(goods);
    }

    public void update(long id, DTOGood goodToUpdate) {
        Optional<Goods> optional = goodsRepository.findById(id);
        Goods goods = optional.orElseThrow(() -> new RuntimeException("Товар с данным ID не найден"));//лямбда
        goods.setName(goodToUpdate.getName());
        goods.setPrice(goodToUpdate.getPrice());
        goods.setCategory(goodToUpdate.getCategory());
        goodsRepository.save(goods);
    }

    public void delete(long id) {
        goodsRepository.deleteById(id);
    }

    public void addRate(long id, FeedBack feedBack) {
        Client client = clientService.getCurrentUser();
        Optional<Goods> optional = goodsRepository.findById(id);
        Goods goods = optional.orElseThrow(() -> new RuntimeException("Товар с данным ID не найден"));
        feedBack.setGoods(goods);
        feedBack.setClient(client);

        double sum = (goods.getRate()*goods.getCountRates());
        sum += feedBack.getRate();
        goods.setCountRates(goods.getCountRates()+1);
        goods.setRate((double) sum/(goods.getCountRates()));
        goodsRepository.save(goods);
        feedBackRepository.save(feedBack);
    }

    public double getRate(int id){
        ret
    }

    public List<FeedBack> findAllFeedbacks(long id) {
        Optional<Goods> optional = goodsRepository.findById(id);
        Goods goods = optional.orElseThrow(() -> new RuntimeException("Товар с данным ID не найден"));
        return goods.getListFeedbacks();
    }


}
