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
public class Seller{
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private String username;
    @JsonIgnore
    @Column
    private String password;
    @Column
    private final String role = "ROLE_SELLER";
    @Column
    private String companyName;
    @Column
    private String address;
    @Column
    private String email;
    @Column
    private double rate;
    @Column
    private int countRates;

    @JsonIgnore
    @OneToMany(mappedBy = "seller")
    private List<Good> products;

    @Override
    public String toString() {
        return "Seller{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", companyName='" + companyName + '\'' +
                '}';
    }
}

