package com.example.demo.models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private String name;
    @Column
    private char sex;
    @Column
    private String login;
    @Column
    private String password;
    @Column
    private String address;
    @Column
    private String email;
    @Column(name="has_a_card")
    private boolean hasACard;
    @Column
    private int card;

    @OneToMany(mappedBy = "client")
    private List<FeedBack> listFeedbacks;
    //private Basket basket;
}
