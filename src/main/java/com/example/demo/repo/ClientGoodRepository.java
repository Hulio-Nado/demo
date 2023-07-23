package com.example.demo.repo;

import com.example.demo.models.Client;
import com.example.demo.models.ClientGood;
import com.example.demo.models.Good;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientGoodRepository extends JpaRepository<ClientGood, Long> {

    Optional<ClientGood> findByClientAndGood(Client client, Good good);

    List<ClientGood> findByClient(Client client);
}
