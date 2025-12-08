package com.SmartShop.MicroTech_SmartShop.repository;

import com.SmartShop.MicroTech_SmartShop.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @Query(value = "SELECT * FROM PRODUCT",
            countQuery = "SELECT count(*) FROM product",
            nativeQuery = true)
    Page<Product> findAllIncludingDeleted(Pageable pageable);
    Page<Product> findByIsDeletedFalse(Pageable pageable);
}
