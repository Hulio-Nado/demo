package com.example.demo.models;


import com.example.demo.DTO.DTOClient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table
@Getter
@Setter
public class FeedBack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "good_id", referencedColumnName = "id")
    private Good good;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private Client client;

    @Column
    private String feedback;

    @NotNull
    @Column
    @Min(value = 1, message = "Rate must be more then 1*")
    @Max(value = 5, message = "Rate must be no more then 5*")
    private int rate;

    @Column
    private LocalDateTime created;

    @PrePersist
    public void setCreated() {
        this.created = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "FeedBack{" +
                "id=" + id +
                ", rate=" + rate +
                '}';
    }

}
