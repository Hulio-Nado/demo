package com.example.demo.repo;

import com.example.demo.models.Goods;
import com.example.demo.utils.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface GoodsRepository extends JpaRepository<Goods, Long> {
    List<Goods> findByCategory(Category category);


    List<Goods> findByName(String name);
}
