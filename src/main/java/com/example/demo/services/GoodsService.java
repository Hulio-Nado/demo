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
import jakarta.validation.Valid;
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

    //метод поиска всех товаров и вывод с пагинацией
    public Page<DTOGood> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Good> resultPage = goodsRepository.findAll(pageable);

        return DTOGood.convertToDTOPage(resultPage);
    }

    //метод поиска всех товаров и вывод с пагинацией по рейтингу, начиная с лучшего
    public Page<DTOGood> findAllByRate(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Good> resultPage = goodsRepository.findAllByOrderByRateDesc(pageable);

        return DTOGood.convertToDTOPage(resultPage);
    }

    //метод поиска всех товаров и вывод с пагинацией по рейтингу, начиная с худшего
    public Page<DTOGood> findAllByRateRev(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Good> resultPage = goodsRepository.findAllByOrderByRateAsc(pageable);

        return DTOGood.convertToDTOPage(resultPage);
    }

    //метод поиска всех товаров и вывод с пагинацией по количествву оценок
    public Page<DTOGood> findAllByCount(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Good> resultPage = goodsRepository.findAllByOrderByCountRatesDesc(pageable);

        return DTOGood.convertToDTOPage(resultPage);
    }

    //метод поиска товара по ID, с возвращением ДТО
    public DTOGood findByID(long id) {
        Optional<Good> optional = goodsRepository.findById(id);
        Good good = optional.orElseThrow(() -> new RuntimeException("Товар с данным ID не найден"));
        return DTOGood.convertToDTO(good);
    }

    //метод поиска товара по ID, с возвращением товара
    public Good findGoodByID(long id) {
        Optional<Good> optional = goodsRepository.findById(id);
        Good good = optional.orElseThrow(() -> new RuntimeException("Товар с данным ID не найден"));
        return good;
    }

    //метод поиска товара по категории, с возвращением ДТО
    public List<DTOGood> findByCategory(Category category) {
        List<Good> list = goodsRepository.findByCategory(category);
        return DTOGood.convertToDTOList(list);
    }

    //метод поиска товара по названию, с возвращением ДТО
    public List<DTOGood> findByName(String name) {
        System.out.println(name);
        List<Good> list = goodsRepository.findByName(name);
        System.out.println(list);
        return DTOGood.convertToDTOList(list);
    }

    //метод сохранения товара в БД с установкой начальных значений рейтинга
    @Transactional
    public void save(Good good) {
        good.setRate(0);
        good.setCountRates(0);
        good.setSeller(sellerService.getCurrentUser());
        goodsRepository.save(good);
    }

    //метод обновления информации о товаре
    @Transactional
    public void update(long id, DTOGood goodToUpdate) {
        Optional<Good> optional = goodsRepository.findById(id);
        Good good = optional.orElseThrow(() -> new RuntimeException("Товар с данным ID не найден"));//лямбда
        good.setName(goodToUpdate.getName());
        good.setPrice(goodToUpdate.getPrice());
        good.setCategory(goodToUpdate.getCategory());
        goodsRepository.save(good);
    }

    //метод удаления товара по ID из БД
    @Transactional
    public void delete(long id) {
        goodsRepository.deleteById(id);
    }

    //метод добавления отзыва к товару
    @Transactional
    public boolean addRate(long id, FeedBack feedBack) {
        Client client = clientService.getCurrentUser();
        Good good = goodsRepository.findById(id).
                orElseThrow(() -> new RuntimeException("Товар с данным ID не найден"));
        Seller seller = good.getSeller();

        // Проверяем, оставил ли клиент уже отзыв для данного товара
        boolean hasFeedback = feedBackRepository.existsByGoodIdAndClientId(id, client.getId());

        if (hasFeedback) {
            // Если клиент уже оставил отзыв, вернуть ошибку или информацию о том, что он уже оставил отзыв
            return false;
        }

        //сохранеям данные по товару и по клиенту в отзыв в базу данных
        feedBack.setGood(good);
        feedBack.setClient(client);

        //перерасчет рейтинга
        calculate(seller, feedBack, good);
        return true;
    }

    //метод изменения существующего отзыва от клиента с перерасчетом рейтинга товара и продавца
    @Transactional
    public void changeRate(long id, FeedBack feedBack) {
        Client client = clientService.getCurrentUser();
        Good good = goodsRepository.findById(id).
                orElseThrow(() -> new RuntimeException("Товар с данным ID не найден"));
        Seller seller = good.getSeller();
        Optional<FeedBack> optional = feedBackRepository.findByClientId(client.getId());
        int oldRate = optional.get().getRate();
        feedBackRepository.delete(optional.get());

        //сохранеям данные по товару и по клиенту в отзыв в базу данных
        feedBack.setGood(good);
        feedBack.setClient(client);

        //перерасчет рейтинга
        calculate(seller, feedBack, good, oldRate);
    }

    //метод удаления отзыва клиента с перерасчетом рейтинга товара и продавца
    @Transactional
    public void deleteFeedback(long goodsId) {
        Client client = clientService.getCurrentUser();
        Good good = goodsRepository.findById(goodsId).
                orElseThrow(() -> new RuntimeException("Товар с данным ID не найден"));
        Seller seller = good.getSeller();
        FeedBack feedBack = feedBackRepository.findByClientId(client.getId())
                .orElseThrow(() -> new RuntimeException("Отзыв не найден"));
        int oldRate = feedBack.getRate();
        feedBackRepository.delete(feedBack);

        calculate(seller, good, oldRate);
    }

    //метод подсчета рейтинга при добавлении нового отзыва
    @Transactional
    public void calculate(Seller seller, FeedBack feedBack, Good good){
        double sellerSum = seller.getRate()*seller.getCountRates();
        sellerSum += feedBack.getRate();
        seller.setCountRates(seller.getCountRates() + 1);
        seller.setRate(sellerSum / seller.getCountRates());
        sellerRepository.save(seller);

        double goodsSum = good.getRate()*good.getCountRates();
        goodsSum += feedBack.getRate();
        good.setCountRates(good.getCountRates() + 1);
        good.setRate(goodsSum / good.getCountRates());
        goodsRepository.save(good);
        feedBackRepository.save(feedBack);
    }

    //метод подсчета рейтинга при удалении одного из отзывов
    @Transactional
    public void calculate(Seller seller, Good good, int oldRate){
        double sum = seller.getRate()*seller.getCountRates();
        sum -= oldRate;
        seller.setCountRates(seller.getCountRates() - 1);
        seller.setRate(sum / seller.getCountRates());
        sellerRepository.save(seller);

        double sum1 = good.getRate()*good.getCountRates();
        sum1 -= oldRate;
        good.setCountRates(good.getCountRates() - 1);
        good.setRate(sum1 / good.getCountRates());
        goodsRepository.save(good);
    }

    //метод подсчета рейтинга при изменении одного из отзывов
    @Transactional
    public void calculate(Seller seller, FeedBack newFeedBack, Good good, int oldRate){
        double sellerSum = seller.getRate()*seller.getCountRates();
        sellerSum += newFeedBack.getRate() - oldRate;
        seller.setRate(sellerSum / seller.getCountRates());
        sellerRepository.save(seller);

        double goodsSum = good.getRate()*good.getCountRates();
        goodsSum += newFeedBack.getRate() - oldRate;
        good.setRate(goodsSum / good.getCountRates());
        goodsRepository.save(good);
        feedBackRepository.save(newFeedBack);
    }

    //метод получения всех отзывов по товару по его ID
    public List<DTOFeedback> findAllFeedbacks(long id) {
        Optional<Good> optional = goodsRepository.findById(id);
        Good good = optional.orElseThrow(() -> new RuntimeException("Товар с данным ID не найден"));

        return DTOFeedback.convertToDTOList(good.getListFeedbacks());
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
