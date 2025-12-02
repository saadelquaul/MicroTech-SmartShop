package com.SmartShop.MicroTech_SmartShop.repository;


import com.SmartShop.MicroTech_SmartShop.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByUserId(Long userId);

    boolean existsByEmail(String email);
}
