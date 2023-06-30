package com.example.demo.repo;

import com.example.demo.models.FeedBack;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedBackRepository extends CrudRepository<FeedBack, Long> {
}
