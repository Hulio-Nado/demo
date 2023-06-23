package com.example.demo.controllers;

import com.example.demo.DTO.DTOGood;
import com.example.demo.DTO.DTOSearch;
import com.example.demo.services.GoodsService;
import com.example.demo.utils.Category;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
// @RequiredArgsConstructor
public class GoodsController {
    private final GoodsService goodsService;

    //посмотреть товары продавца
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

    @PostMapping("/create")
    public ResponseEntity<?> createGood(@Valid @RequestBody DTOGood goodToCreate){
        goodsService.save(DTOGood.convertToGoods(goodToCreate));
        return ResponseEntity.ok().body("Товар создан");
    }


    @PatchMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable long id, @Valid @RequestBody DTOGood goodToUpdate){
        goodsService.update(id, goodToUpdate);
        return ResponseEntity.ok().body("Товар обновлен");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteOneGood(@PathVariable long id){
        goodsService.delete(id);
        return ResponseEntity.ok().body("Товар удален");
    }

}
