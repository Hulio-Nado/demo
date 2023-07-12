package com.example.demo.repo;

import com.example.demo.models.Good;
import com.example.demo.models.Seller;
import com.example.demo.utils.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface GoodsRepository extends JpaRepository<Good, Long> {
    List<Good> findByCategory(Category category);


    List<Good> findByName(String name);

    Page<Good> findAll(Pageable pageable);

    List<?> findAllBySeller(Seller currentUser);

    Page<Good> findAllByOrderByRateDesc(Pageable pageable);

    Page<Good> findAllByOrderByRateAsc(Pageable pageable);

    Page<Good> findAllByOrderByCountRatesDesc(Pageable pageable);
}
