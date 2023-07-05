package com.example.demo.models;


import com.example.demo.DTO.DTORegistration;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;

@Getter
@Setter
@Entity
@Table
public class Client  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private String username;
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
    @Column
    private String role = "CLIENT";

    @OneToMany(mappedBy = "client")
    private List<FeedBack> listFeedbacks;

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
