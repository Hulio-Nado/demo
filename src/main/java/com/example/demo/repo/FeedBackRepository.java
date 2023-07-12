package com.example.demo.repo;

import com.example.demo.models.FeedBack;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedBackRepository extends CrudRepository<FeedBack, Long> {

    List<FeedBack> findAllByOrderByCreatedDesc();

    List<FeedBack> findAllByOrderByRateDesc();

    List<FeedBack> findAllByOrderByCreatedAsc();

    List<FeedBack> findAllByOrderByRateAsc();
}
