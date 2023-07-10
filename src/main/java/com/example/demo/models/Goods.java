package com.example.demo.models;

import com.example.demo.DTO.DTOGood;
import com.example.demo.utils.Category;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cascade;

import java.util.List;
import java.util.Set;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor//констр без параметров
public class Goods {

    public Goods(DTOGood createGood) {
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

    @Enumerated(value = EnumType.STRING)
    private Category category;

    @OneToMany(mappedBy = "goods_id")
    private List<Charackteristics> list;

    @OneToMany(mappedBy = "goods")
    @Cascade(value = org.hibernate.annotations.CascadeType.ALL)
    private List<FeedBack> listFeedbacks;

    @JsonIgnore
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
                ", list=" + list +
                ", listFeedbacks=" + listFeedbacks +
                ", seller=" + seller.getUsername() +
                '}';
    }
}
