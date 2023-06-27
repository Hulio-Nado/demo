package com.example.demo.models;

import java.util.ArrayList;
import java.util.List;

public class Basket {
    private List<Goods> list;
    private Client client;

    public Basket(List<Goods> list) {
        this.list = new ArrayList<>();
    }


}
