package com.example.demo.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {
    //удалять товары с ретинггм ниже 3.0 при оценках больше 10
    //блокировать продавца, банить покупателей
    //удалять отзывы (если видно что заказные)
    //одобрение товара перед публикацией (символика, нарушения итп)
}
