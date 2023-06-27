package com.example.demo.services;

import com.example.demo.DTO.DTOGood;
import com.example.demo.models.FeedBack;
import com.example.demo.models.Goods;
import com.example.demo.repo.GoodsRepository;
import com.example.demo.utils.Category;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GoodsService {
    private final GoodsRepository goodsRepository;

    public GoodsService(GoodsRepository goodsRepository) {
        this.goodsRepository = goodsRepository;
    }

    public List<DTOGood> findAll() {
        return DTOGood.convertToDTOList(goodsRepository.findAll());
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
        Optional<Goods> optional = goodsRepository.findById(id);
        Goods goods = optional.orElseThrow(() -> new RuntimeException("Товар с данным ID не найден"));
        feedBack.setGoods(goods);
        goods.getListFeedbacks().add(feedBack);
        System.out.println(goods.getListFeedbacks());
        goodsRepository.save(goods);
        System.out.println(goods.getListFeedbacks());
    }

    public List<FeedBack> findAllFeedbacks(long id) {
        Optional<Goods> optional = goodsRepository.findById(id);
        Goods goods = optional.orElseThrow(() -> new RuntimeException("Товар с данным ID не найден"));
        return goods.getListFeedbacks();
    }
}
