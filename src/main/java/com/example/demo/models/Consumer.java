package com.example.demo.models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table
public class Consumer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private char sex;
    private String login;
    private String password;
    private String address;
    private String email;
    private boolean hasACard;
    private int card;
    //private Basket basket;
}
