package com.shopping_cart.repository;

import com.shopping_cart.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category ,Long> {
    Category findByName(String name);

    boolean existsByName(String name);
}
