package com.example.demo.models;

import com.example.demo.DTO.DTOGood;
import com.example.demo.utils.Category;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import java.util.List;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor//констр без параметров
public class Good {

    public Good(DTOGood createGood) {
        this.name = createGood.getName();
        this.price = createGood.getPrice();
    }

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String name;

    @Column
    private double price;

    @Column
    private double rate;

    @Column
    private int countRates;

    @Column
    private int quantity;

    @Enumerated(value = EnumType.STRING)
    private Category category;

    @OneToMany(mappedBy = "good_id")
    private List<Charackteristics> list;

    @OneToMany(mappedBy = "good")
    @Cascade(value = org.hibernate.annotations.CascadeType.ALL)
    private List<FeedBack> listFeedbacks;

    @OneToMany(mappedBy = "good")
    private List<ClientGood> clients;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "seller_id", referencedColumnName = "id")
    private Seller seller;

    @Override
    public String toString() {
        return "Good{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", rate=" + rate +
                ", countRates=" + countRates +
                ", quantity=" + quantity +
                ", category=" + category +
                ", list=" + list +
                ", listFeedbacks=" + listFeedbacks +
                ", clients=" + clients +
                ", seller=" + seller +
                '}';
    }
}
