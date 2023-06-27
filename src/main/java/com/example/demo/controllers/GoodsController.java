package com.example.demo.controllers;

import com.example.demo.DTO.DTOGood;
import com.example.demo.DTO.DTOSearch;
import com.example.demo.models.FeedBack;
import com.example.demo.services.GoodsService;
import com.example.demo.utils.Category;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/good")
// @RequiredArgsConstructor
public class GoodsController {
    private final GoodsService goodsService;

    //посмотреть товары продавца(*)
    //оставлять отзыв функционал, админ - блокировать продавца или один товар продавца
    //положить в корзину, удалить из корзины, изменить количество товара в корзине,
    //подкатегории, хештеги - поиск по ним, поиск по названиЮ, по цене фильтрация, сортировка (различная)
    //

    public GoodsController(GoodsService goodsService) {
        this.goodsService = goodsService;
    }

    //пагинация
    @GetMapping("/all")
    public ResponseEntity<?> showAllGoods(){
        List<DTOGood> list = goodsService.findAll();
        return ResponseEntity.ok(list);
        //обертка
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
    public ResponseEntity<?> rateGood(@PathVariable long id, @RequestBody FeedBack feedBack){
        System.out.println(feedBack);
        goodsService.addRate(id, feedBack);
        return showAllFeedbacksByGoodsID(id);
    }

    @GetMapping("/{id}/allFeedbacks")
    public ResponseEntity<?> showAllFeedbacksByGoodsID(@PathVariable long id) {
        List<FeedBack> list = goodsService.findAllFeedbacks(id);
        return ResponseEntity.ok(list);
    }
}