package com.example.demo.services;

import com.example.demo.models.Good;

import java.util.List;

public class GoodServiceExample {
    public List<Good> getAll() {
        return List.of(new Good(), new Good());
    }
}
