package com.example.demo.repo;

import com.example.demo.models.Goods;
import com.example.demo.utils.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GoodsRepository extends JpaRepository<Goods, Long> {
    List<Goods> findByCategory(Category category);

    List<Goods> findByName(String name);
}
