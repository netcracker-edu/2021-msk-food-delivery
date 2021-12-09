package com.ncedu.fooddelivery.api.v1.repos;

import com.ncedu.fooddelivery.api.v1.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends CrudRepository<Product, Long> {

    Page<Product> findAll(Pageable pageable);
    Page<Product> findAllByInShowcase(Boolean inShowcase, Pageable pageable);

    @Query(value = "SELECT p.* , ts_rank_cd(s.search_vector, query, 1) AS rank" +
                " FROM to_tsquery('russian', ?1) AS query, products AS p" +
                " JOIN products_search AS s" +
                " ON p.product_id = s.product_search_id" +
                " WHERE query @@ s.search_vector" +
                " ORDER BY rank DESC",
           countQuery = "SELECT count(*)" +
                " FROM products_search AS s, to_tsquery('russian', ?1) AS query" +
                " WHERE query @@ s.search_vector",
           nativeQuery = true)
    Page<Product> searchProducts(String search, Pageable pageable);

    @Query(value = "SELECT p.* , ts_rank_cd(s.search_vector, query, 1) AS rank" +
                " FROM to_tsquery('russian', ?1) AS query, products AS p" +
                " JOIN products_search AS s" +
                " ON p.product_id = s.product_search_id" +
                " WHERE query @@ s.search_vector AND p.in_showcase = true" +
                " ORDER BY rank DESC",
           countQuery = "SELECT count(*)" +
                " FROM to_tsquery('russian', ?1) AS query, products_search AS s" +
                " JOIN products AS p " +
                " ON s.product_search_id = p.product_id" +
                " WHERE query @@ s.search_vector AND p.in_showcase = true",
           nativeQuery = true)
    Page<Product> searchProductsInShowcase(String search, Pageable pageable);
}
