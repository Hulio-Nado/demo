package com.example.demo.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table
public class Admin extends User{

    @Column
    private String name;

    @Column
    private String email;
}
