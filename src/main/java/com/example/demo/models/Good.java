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

    @JsonIgnore
    @OneToMany(mappedBy = "good_id")
    private List<Charackteristics> list;

    @JsonIgnore
    @OneToMany(mappedBy = "good")
    @Cascade(value = org.hibernate.annotations.CascadeType.ALL)
    private List<FeedBack> listFeedbacks;

    @JsonIgnore
    @OneToMany(mappedBy = "good")
    private List<ClientGood> clients;

    @ManyToOne
    @JoinColumn(name = "seller_id", referencedColumnName = "id")
    private Seller seller;

    @Override
    public String toString() {
        return "Goods{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", category=" + category +
                ", listFeedbacks=" + listFeedbacks +
                ", seller=" + seller.getUsername() +
                '}';
    }
}
