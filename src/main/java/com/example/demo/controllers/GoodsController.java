package com.example.demo.controllers;

import com.example.demo.DTO.DTOFeedback;
import com.example.demo.DTO.DTOGood;
import com.example.demo.DTO.DTOSearch;
import com.example.demo.models.FeedBack;
import com.example.demo.services.GoodsService;
import com.example.demo.utils.Category;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/good")
// @RequiredArgsConstructor
public class GoodsController {
    private final GoodsService goodsService;

    //посмотреть товары продавца(*)
    //админ - блокировать продавца или один товар продавца
    //положить в корзину, удалить из корзины, изменить количество товара в корзине,
    //подкатегории, хештеги - поиск по ним, поиск по названиЮ, по цене фильтрация, сортировка (различная)
    //

    public GoodsController(GoodsService goodsService) {
        this.goodsService = goodsService;
    }

    //вывод всех товаров без сортировок
    @GetMapping("/all")
    public ResponseEntity<?> showAllGoods(@RequestParam(required = false, defaultValue = "1") int page,
                                          @RequestParam(required = false, defaultValue = "3") int size){
        Page<DTOGood> list = goodsService.findAll(page-1, size);
        return ResponseEntity.ok(list);
    }

    //вывод всех товаров с сортировкой по рейтингу
    @GetMapping("/all/rate")
    public ResponseEntity<?> showAllGoodsOrderByRate(@RequestParam(required = false, defaultValue = "1") int page,
                                          @RequestParam(required = false, defaultValue = "3") int size){
        Page<DTOGood> list = goodsService.findAllByRate(page-1, size);
        return ResponseEntity.ok(list);
    }

    //вывод всех товаров с сортировкой по рейтингу(начиная с низкого)
    @GetMapping("/all/raterev")
    public ResponseEntity<?> showAllGoodsOrderByRateRev(@RequestParam(required = false, defaultValue = "1") int page,
                                                     @RequestParam(required = false, defaultValue = "3") int size){
        Page<DTOGood> list = goodsService.findAllByRateRev(page-1, size);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/all/count")
    public ResponseEntity<?> showAllGoodsOrderByCount(@RequestParam(required = false, defaultValue = "1") int page,
                                                        @RequestParam(required = false, defaultValue = "3") int size){
        Page<DTOGood> list = goodsService.findAllByCount(page-1, size);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> showGoodById(@PathVariable long id){
        DTOGood good = goodsService.findByID(id);
        return ResponseEntity.ok(good);
    }

    @GetMapping("/categories")
        public ResponseEntity<?> showAllCategories(){
        List<Category> list = List.of(Category.values());
        return ResponseEntity.ok(list);
    }

    @PostMapping("/category")
    public ResponseEntity<?> showGoodsByCategory(@RequestBody DTOSearch dtoSearch){
        List<DTOGood> list = goodsService.findByCategory(dtoSearch.getCategory());
        return ResponseEntity.ok(list);
    }

    @PostMapping(value = "/name")
    public ResponseEntity<?> showGoodsByName(@RequestBody DTOSearch dtoSearch){
        List<DTOGood> list = goodsService.findByName(dtoSearch.getName());
        return ResponseEntity.ok(list);
    }

    @PostMapping(value = "/{id}/feedback")
    @Secured("CLIENT")
    public ResponseEntity<?> rateGood(@PathVariable long id, @RequestBody @Valid FeedBack feedBack){
        System.out.println(feedBack);
        goodsService.addRate(id, feedBack);
        return showAllFeedbacksByGoodsID(id);
    }

    //вывод всех отзывов у одного товара по айди
    @GetMapping("/{id}/feeds")
    public ResponseEntity<?> showAllFeedbacksByGoodsID(@PathVariable long id) {
        List<DTOFeedback> list = goodsService.findAllFeedbacks(id);
        return ResponseEntity.ok(list);
    }

    //сортировка отзывов по дате с последней начиная
    @GetMapping("/{id}/feeds/date")
    public ResponseEntity<?> showAllFeedbacksByDate(@PathVariable long id,
                                                    @RequestParam(defaultValue = "0") int rate) {
        List<DTOFeedback> list = goodsService.findAllFeedbackSortedByDate(id, rate);
        return ResponseEntity.ok(list);
    }

    //сортировка отзывов по дате начиная со старых
    @GetMapping("/{id}/feeds/daterev")
    public ResponseEntity<?> showAllFeedbacksByDateRev(@PathVariable long id,
                                                       @RequestParam(defaultValue = "0") int rate) {
        List<DTOFeedback> list = goodsService.findAllFeedbackSortedByDateRev(id, rate);
        return ResponseEntity.ok(list);
    }

    //сортировка отзывов по рейтингу (начиная с 5 баллов)
    @GetMapping("/{id}/feeds/rate")
    public ResponseEntity<?> showAllFeedbacksByRate(@PathVariable long id,
                                                    @RequestParam(defaultValue = "0") int rate) {
        List<DTOFeedback> list = goodsService.findAllFeedbackByRate(id, rate);

        return ResponseEntity.ok(list);
    }

    //сортировка отзывов по рейтингу (начиная с меньшего рейтинга)
    @GetMapping("/{id}/feeds/raterev")
    public ResponseEntity<?> showAllFeedbacksByRateAsc(@PathVariable long id,
                                                       @RequestParam(defaultValue = "0") int rate) {
        List<DTOFeedback> list = goodsService.findAllFeedbackByRateAsc(id, rate);
        return ResponseEntity.ok(list);
    }

}
