package com.liseh.auth.repository;

import com.liseh.auth.persistence.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Client findByClientId(String clientId);
}
