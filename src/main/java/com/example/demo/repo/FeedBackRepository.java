package com.example.demo.repo;

import com.example.demo.models.FeedBack;
import com.example.demo.models.Good;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedBackRepository extends CrudRepository<FeedBack, Long> {
    List<FeedBack> findByGoodIdOrderByRateDesc(long id);

    List<FeedBack> findByGoodIdOrderByRateAsc(long id);

    List<FeedBack> findByGoodIdOrderByCreatedDesc(long id);

    List<FeedBack> findByGoodIdOrderByCreatedAsc(long id);
}
