package com.farmeasy.marketplace.repository;

import com.farmeasy.marketplace.entity.Product;
import com.farmeasy.marketplace.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findByCategoryAndInStockTrueOrderByNameEnAsc(ProductCategory category);
}
