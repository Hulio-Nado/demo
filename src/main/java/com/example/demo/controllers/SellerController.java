package com.example.demo.controllers;

import com.example.demo.DTO.DTOGood;
import com.example.demo.services.GoodsService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/seller")
public class SellerController {
    private final GoodsService goodsService;

    public SellerController(GoodsService goodsService) {
        this.goodsService = goodsService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createGood(@Valid @RequestBody DTOGood goodToCreate){
        goodsService.save(DTOGood.convertToGoods(goodToCreate));
        return ResponseEntity.ok().body("Товар создан");
    }


    @PatchMapping("/update/{id}")
    public ResponseEntity<?> updateGood(@PathVariable long id, @Valid @RequestBody DTOGood goodToUpdate){
        goodsService.update(id, goodToUpdate);
        return ResponseEntity.ok().body("Товар обновлен");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteOneGood(@PathVariable long id){
        goodsService.delete(id);
        return ResponseEntity.ok().body("Товар удален");
    }
}
