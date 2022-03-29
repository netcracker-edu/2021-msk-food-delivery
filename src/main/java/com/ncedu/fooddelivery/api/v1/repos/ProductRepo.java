package com.ncedu.fooddelivery.api.v1.repos;

import com.ncedu.fooddelivery.api.v1.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends CrudRepository<Product, Long> {

    @Query(value = "SELECT DISTINCT ON (p.product_id) p.*" +
            " FROM products p" +
            " JOIN product_positions pp" +
            " ON p.product_id = pp.product_id" +
            " WHERE pp.warehouse_id = :warehouseId"+
            " AND p.in_showcase = true"+
            " AND (pp.manufacture_date + p.expiration_days) > CURRENT_DATE"+
            " AND pp.current_amount > 0",
            countQuery = "SELECT COUNT(DISTINCT p.product_id)" +
                    " FROM products p" +
                    " JOIN product_positions pp" +
                    " ON p.product_id = pp.product_id" +
                    " WHERE pp.warehouse_id = :warehouseId"+
                    " AND p.in_showcase = true"+
                    " AND (pp.manufacture_date + p.expiration_days) > CURRENT_DATE"+
                    " AND pp.current_amount > 0",
            nativeQuery = true)
    Page<Product> findAllByInShowcase(@Param(value = "warehouseId") Long warehouseId, Pageable pageable);

    @Query(value = "SELECT DISTINCT ON (p.product_id) p.*" +
                " FROM products p" +
                " JOIN product_positions pp" +
                " ON p.product_id = pp.product_id" +
                " WHERE pp.warehouse_id = :warehouseId"+
                " AND (pp.manufacture_date + p.expiration_days) > CURRENT_DATE"+
                " AND pp.current_amount > 0",
           countQuery = "SELECT COUNT(DISTINCT p.product_id)" +
                   " FROM products p" +
                   " JOIN product_positions pp" +
                   " ON p.product_id = pp.product_id" +
                   " WHERE pp.warehouse_id = :warehouseId"+
                   " AND (pp.manufacture_date + p.expiration_days) > CURRENT_DATE"+
                   " AND pp.current_amount > 0",
           nativeQuery = true)
    Page<Product> findAll(@Param(value = "warehouseId") Long warehouseId,
                            Pageable pageable);

    @Query(value = "SELECT DISTINCT ON (p.product_id, rank) p.* , ts_rank_cd(s.search_vector, query, 1) AS rank" +
                " FROM to_tsquery('russian', :phrase) AS query, products AS p" +
                " JOIN products_search AS s" +
                " ON p.product_id = s.product_search_id" +
                " JOIN product_positions AS pp" +
                " ON p.product_id = pp.product_id" +
                " WHERE query @@ s.search_vector" +
                " AND pp.warehouse_id = :warehouseId"+
                " AND (pp.manufacture_date + p.expiration_days) > CURRENT_DATE" +
                " AND pp.current_amount > 0"+
                " ORDER BY rank DESC",
           countQuery = "SELECT count(DISTINCT p.product_id)" +
                   " FROM to_tsquery('russian', :phrase) AS query, products AS p" +
                   " JOIN products_search AS s" +
                   " ON p.product_id = s.product_search_id" +
                   " JOIN product_positions AS pp" +
                   " ON p.product_id = pp.product_id" +
                   " WHERE query @@ s.search_vector" +
                   " AND pp.warehouse_id = :warehouseId"+
                   " AND (pp.manufacture_date + p.expiration_days) > CURRENT_DATE"+
                   " AND pp.current_amount > 0",
           nativeQuery = true)
    Page<Product> searchProducts(@Param(value = "phrase") String search,
                                 @Param(value = "warehouseId") Long warehouseId,
                                 Pageable pageable);

    @Query(value = "SELECT DISTINCT ON (p.product_id, rank) p.* , ts_rank_cd(s.search_vector, query, 1) AS rank" +
                " FROM to_tsquery('russian', :phrase) AS query, products AS p" +
                " JOIN products_search AS s" +
                " ON p.product_id = s.product_search_id" +
                " JOIN product_positions AS pp" +
                " ON p.product_id = pp.product_id" +
                " WHERE query @@ s.search_vector AND p.in_showcase = true" +
                " AND pp.warehouse_id = :warehouseId"+
                " AND (pp.manufacture_date + p.expiration_days) > CURRENT_DATE" +
                " AND pp.current_amount > 0" +
                " ORDER BY rank DESC",
           countQuery = "SELECT count(DISTINCT p.product_id)" +
                   " FROM to_tsquery('russian', :phrase) AS query, products AS p" +
                   " JOIN products_search AS s" +
                   " ON p.product_id = s.product_search_id" +
                   " JOIN product_positions AS pp" +
                   " ON p.product_id = pp.product_id" +
                   " WHERE query @@ s.search_vector AND p.in_showcase = true" +
                   " AND pp.warehouse_id = :warehouseId"+
                   " AND (pp.manufacture_date + p.expiration_days) > CURRENT_DATE"+
                   " AND pp.current_amount > 0",
           nativeQuery = true)
    Page<Product> searchProductsInShowcase(@Param(value = "phrase") String search,
                                           @Param(value = "warehouseId") Long warehouseId,
                                           Pageable pageable);

    @Query( value = "SELECT COUNT(DISTINCT p.product_id)" +
            " FROM products p" +
            " JOIN product_positions pp" +
            " ON p.product_id = pp.product_id" +
            " WHERE pp.warehouse_id = :warehouseId"+
            " AND p.in_showcase = true"+
            " AND (pp.manufacture_date + p.expiration_days) > CURRENT_DATE"+
            " AND pp.current_amount > 0",
            nativeQuery = true)
    int findAllByInShowcaseCount(@Param(value = "warehouseId") Long warehouseId);

    @Query(value = "SELECT COUNT(DISTINCT p.product_id)" +
            " FROM products p" +
            " JOIN product_positions pp" +
            " ON p.product_id = pp.product_id" +
            " WHERE pp.warehouse_id = :warehouseId"+
            " AND (pp.manufacture_date + p.expiration_days) > CURRENT_DATE"+
            " AND pp.current_amount > 0",
            nativeQuery = true)
    int findAllCount(@Param(value = "warehouseId") Long warehouseId);

    @Query(value = "SELECT count(DISTINCT p.product_id)" +
            " FROM to_tsquery('russian', :phrase) AS query, products AS p" +
            " JOIN products_search AS s" +
            " ON p.product_id = s.product_search_id" +
            " JOIN product_positions AS pp" +
            " ON p.product_id = pp.product_id" +
            " WHERE query @@ s.search_vector" +
            " AND pp.warehouse_id = :warehouseId"+
            " AND (pp.manufacture_date + p.expiration_days) > CURRENT_DATE"+
            " AND pp.current_amount > 0",
            nativeQuery = true)
    int searchProductsCount(@Param(value = "phrase") String search,
                                 @Param(value = "warehouseId") Long warehouseId);

    @Query(value = "SELECT count(DISTINCT p.product_id)" +
            " FROM to_tsquery('russian', :phrase) AS query, products AS p" +
            " JOIN products_search AS s" +
            " ON p.product_id = s.product_search_id" +
            " JOIN product_positions AS pp" +
            " ON p.product_id = pp.product_id" +
            " WHERE query @@ s.search_vector AND p.in_showcase = true" +
            " AND pp.warehouse_id = :warehouseId"+
            " AND (pp.manufacture_date + p.expiration_days) > CURRENT_DATE"+
            " AND pp.current_amount > 0",
            nativeQuery = true)
    int searchProductsCountInShowcase(@Param(value = "phrase") String search,
                                      @Param(value = "warehouseId") Long warehouseId);

    @Query( value = "SELECT *" +
            " FROM products AS p" +
            " WHERE p.product_id IN :ids",
            nativeQuery = true)
    Iterable<Product> findByIds(@Param("ids") List<Long> productIds);
}
