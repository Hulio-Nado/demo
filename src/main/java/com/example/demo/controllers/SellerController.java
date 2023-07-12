package com.example.demo.controllers;

import com.example.demo.DTO.DTOGood;
import com.example.demo.DTO.DTORegistration;
import com.example.demo.DTO.DTOUpdate;
import com.example.demo.services.GoodsService;
import com.example.demo.services.SellerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/seller")
public class SellerController {
    private final GoodsService goodsService;
    private final SellerService sellerService;

    public SellerController(GoodsService goodsService, SellerService sellerService) {
        this.goodsService = goodsService;
        this.sellerService = sellerService;
    }

    @GetMapping("/reg")
    public String registration(@ModelAttribute("seller") DTORegistration request){
        return "sellerRegForm.html";
    }

    @ResponseBody
    @PostMapping("/reg")
    public ResponseEntity<?> registerUser(@Valid DTORegistration request) {
        return ResponseEntity.ok(sellerService.save(request));
    }

    @ResponseBody
    @PatchMapping
    @Secured("SELLER")
    public ResponseEntity<?> updateUser(@RequestBody @Valid DTOUpdate request) {
        return ResponseEntity.ok(sellerService.update(request));
    }

    @ResponseBody
    @PostMapping("/create")
    @Secured("SELLER")
    public ResponseEntity<?> createGood(@Valid @RequestBody DTOGood goodToCreate){
        goodsService.save(DTOGood.convertToGoods(goodToCreate));
        return ResponseEntity.ok().body("Товар создан");
    }

    @ResponseBody
    @PatchMapping("/update/{id}")
    @Secured("SELLER")
    public ResponseEntity<?> updateGood(@PathVariable long id, @Valid @RequestBody DTOGood goodToUpdate){
        goodsService.update(id, goodToUpdate);
        return ResponseEntity.ok().body("Товар обновлен");
    }

    @ResponseBody
    @DeleteMapping("/delete/{id}")
    @Secured("SELLER")
    public ResponseEntity<?> deleteOneGood(@PathVariable long id){
        goodsService.delete(id);
        return ResponseEntity.ok().body("Товар удален");
    }

    @ResponseBody
    @GetMapping("/all")
    @Secured("SELLER")
    public ResponseEntity<?> showGoods(){
        return ResponseEntity.ok().body(sellerService.findAll());
    }
}
