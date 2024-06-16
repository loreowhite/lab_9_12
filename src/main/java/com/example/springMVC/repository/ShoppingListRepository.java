package com.example.springMVC.repository;

import com.example.springMVC.model.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingListRepository extends JpaRepository<Product, Long> {
    Product findProductById(Long id);

    @Transactional
    void deleteProductById(Long id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE products SET is_bought = :#{#bought} WHERE id = :#{#id}", nativeQuery = true)
    void updateProduct(boolean bought, Long id);
}
