package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table
@Setter
@Getter
public class Seller {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private String companyName;
    @Column
    private double rate;
    @Column
    private int sumOfRates;

    @JsonIgnore
    @OneToMany(mappedBy = "seller")
    private List<Goods> products;
}

