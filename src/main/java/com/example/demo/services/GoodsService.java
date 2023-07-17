package com.example.demo.services;

import com.example.demo.DTO.DTOFeedback;
import com.example.demo.DTO.DTOGood;
import com.example.demo.models.Client;
import com.example.demo.models.FeedBack;
import com.example.demo.models.Good;
import com.example.demo.models.Seller;
import com.example.demo.repo.FeedBackRepository;
import com.example.demo.repo.GoodsRepository;
import com.example.demo.repo.SellerRepository;
import com.example.demo.utils.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class GoodsService {
    private final GoodsRepository goodsRepository;
    private final FeedBackRepository feedBackRepository;
    private final SellerService sellerService;
    private final ClientService clientService;
    private final SellerRepository sellerRepository;

    public GoodsService(GoodsRepository goodsRepository, FeedBackRepository feedBackRepository,
                        SellerService sellerService, ClientService clientService, SellerRepository sellerRepository) {
        this.goodsRepository = goodsRepository;
        this.feedBackRepository = feedBackRepository;
        this.sellerService = sellerService;
        this.clientService = clientService;
        this.sellerRepository = sellerRepository;
    }

    public Page<DTOGood> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Good> resultPage = goodsRepository.findAll(pageable);

        return DTOGood.convertToDTOPage(resultPage);
    }

    public Page<DTOGood> findAllByRate(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Good> resultPage = goodsRepository.findAllByOrderByRateDesc(pageable);

        return DTOGood.convertToDTOPage(resultPage);
    }

    public Page<DTOGood> findAllByRateRev(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Good> resultPage = goodsRepository.findAllByOrderByRateAsc(pageable);

        return DTOGood.convertToDTOPage(resultPage);
    }

    public Page<DTOGood> findAllByCount(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Good> resultPage = goodsRepository.findAllByOrderByCountRatesDesc(pageable);

        return DTOGood.convertToDTOPage(resultPage);
    }

    public DTOGood findByID(long id) {
        Optional<Good> optional = goodsRepository.findById(id);
        Good good = optional.orElseThrow(() -> new RuntimeException("Товар с данным ID не найден"));
        return DTOGood.convertToDTO(good);
    }

    public List<DTOGood> findByCategory(Category category) {
        List<Good> list = goodsRepository.findByCategory(category);
        return DTOGood.convertToDTOList(list);
    }

    public List<DTOGood> findByName(String name) {
        System.out.println(name);
        List<Good> list = goodsRepository.findByName(name);
        System.out.println(list);
        return DTOGood.convertToDTOList(list);
    }

    @Transactional
    public void save(Good good) {
        good.setRate(0);
        good.setCountRates(0);
        good.setSeller(sellerService.getCurrentUser());
        goodsRepository.save(good);
    }

    @Transactional
    public void update(long id, DTOGood goodToUpdate) {
        Optional<Good> optional = goodsRepository.findById(id);
        Good good = optional.orElseThrow(() -> new RuntimeException("Товар с данным ID не найден"));//лямбда
        good.setName(goodToUpdate.getName());
        good.setPrice(goodToUpdate.getPrice());
        good.setCategory(goodToUpdate.getCategory());
        goodsRepository.save(good);
    }
    //goodmapper подумать

    @Transactional
    public void delete(long id) {
        goodsRepository.deleteById(id);
    }

    @Transactional
    public void addRate(long id, FeedBack feedBack) {
        Client client = clientService.getCurrentUser();
        Good good = goodsRepository.findById(id).
                orElseThrow(() -> new RuntimeException("Товар с данным ID не найден"));
        Seller seller = good.getSeller();
        feedBack.setGood(good);
        feedBack.setClient(client);

        calculate(seller, feedBack, good);
    }

    @Transactional
    public void calculate(Seller seller, FeedBack feedBack, Good good){
        double sum = seller.getRate()*seller.getCountRates();
        sum += feedBack.getRate();
        seller.setCountRates(seller.getCountRates() + 1);
        seller.setRate(sum / seller.getCountRates());
        sellerRepository.save(seller);

        double sum1 = good.getRate()*good.getCountRates();
        sum1 += feedBack.getRate();
        good.setCountRates(good.getCountRates() + 1);
        good.setRate(sum1 / good.getCountRates());
        goodsRepository.save(good);
        feedBackRepository.save(feedBack);
    }

    public List<DTOFeedback> findAllFeedbacks(long id) {
        Optional<Good> optional = goodsRepository.findById(id);
        Good good = optional.orElseThrow(() -> new RuntimeException("Товар с данным ID не найден"));
        List<DTOFeedback> list = DTOFeedback.convertToDTOList(good.getListFeedbacks());
        return list;
    }

    //вывод отзывов одного товара по айди начиная с ближней к нам даты
    public List<DTOFeedback> findAllFeedbackSortedByDate(long id, int rate) {
        List <FeedBack> feeds = feedBackRepository.findByGoodIdOrderByCreatedDesc(id);
        if(rate != 0) {
            feeds = feeds.stream().filter(s -> s.getRate() == rate).toList();
        }

        return DTOFeedback.convertToDTOList(feeds);
    }

    //вывод отзывов одного товара по айди начиная с более ранней даты
    public List<DTOFeedback> findAllFeedbackSortedByDateRev(long id, int rate) {
        List <FeedBack> feeds = feedBackRepository.findByGoodIdOrderByCreatedAsc(id);
        if(rate != 0) {
            feeds = feeds.stream().filter(s -> s.getRate() == rate).toList();
        }

        return DTOFeedback.convertToDTOList(feeds);
    }

    //вывод отзывов одного товара по айди начиная с большего рейтинга
    public List<DTOFeedback> findAllFeedbackByRate(long id, int rate) {

        List <FeedBack> feeds = feedBackRepository.findByGoodIdOrderByRateDesc(id);
        if(rate != 0) {
            feeds = feeds.stream().filter(s -> s.getRate() == rate).toList();
        }

        return DTOFeedback.convertToDTOList(feeds);
    }

    //вывод отзывов одного товара по айди начиная с меньшего рейтинга
    public List<DTOFeedback> findAllFeedbackByRateAsc(long id, int rate) {
        List <FeedBack> feeds = feedBackRepository.findByGoodIdOrderByRateAsc(id);
        if(rate != 0) {
            feeds = feeds.stream().filter(s -> s.getRate() == rate).toList();
        }

        return DTOFeedback.convertToDTOList(feeds);
    }
}
