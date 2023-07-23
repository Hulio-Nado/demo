package com.example.demo.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "client_good")
public class ClientGood {

    public ClientGood(Client client, Good good, int quantity) {
        this.client = client;
        this.good = good;
        this.quantity = quantity;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @JoinColumn(name = "client_id", referencedColumnName = "id")
    @ManyToOne
    private Client client;

    @JoinColumn(name = "good_id", referencedColumnName = "id")
    @ManyToOne
    private Good good;

    @Column
    @Min(value = 0)
    private int quantity;

}
