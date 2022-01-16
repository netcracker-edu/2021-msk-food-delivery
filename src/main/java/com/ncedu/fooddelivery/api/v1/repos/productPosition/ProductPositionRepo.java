package com.ncedu.fooddelivery.api.v1.repos.productPosition;

import com.ncedu.fooddelivery.api.v1.entities.productPosition.ProductPosition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ProductPositionRepo extends JpaRepository<ProductPosition, Long> {
    @Query(
            value = "SELECT * FROM product_positions " +
                    "WHERE product_position_id IN " +
                        "(SELECT product_position_id " +
                            "FROM products INNER JOIN product_positions USING (product_id) " +
                            "WHERE ( (product_positions.manufacture_date + products.expiration_days) < CURRENT_DATE))",

            countQuery = "SELECT count(*) FROM product_positions " + // enabling pagination with native queries
                    "WHERE product_position_id IN " +
                    "(SELECT product_position_id " +
                    "FROM products INNER JOIN product_positions USING (product_id) " +
                    "WHERE ( (product_positions.manufacture_date + products.expiration_days) < CURRENT_DATE))",
            nativeQuery = true
    )
    Page<ProductPosition> findExpiredPositions(Pageable pageable);

    @Query(
            value = "SELECT * FROM product_positions " +
                    "WHERE product_position_id IN " +
                    "(SELECT product_position_id " +
                    "FROM products INNER JOIN product_positions USING (product_id) " +
                    "WHERE ( warehouse_id = :id AND (product_positions.manufacture_date + products.expiration_days) < CURRENT_DATE)) ",
            countQuery = "SELECT count(*) FROM product_positions " + // enabling pagination with native queries
                    "WHERE product_position_id IN " +
                    "(SELECT product_position_id " +
                    "FROM products INNER JOIN product_positions USING (product_id) " +
                    "WHERE ( warehouse_id = :id AND (product_positions.manufacture_date + products.expiration_days) < CURRENT_DATE)) ",
            nativeQuery = true
    )
    Page<ProductPosition> findExpiredPositions(@Param(value = "id") Long id, Pageable pageable);

    Page<ProductPosition> findAll(Specification<ProductPosition> spec, Pageable pageable);

    @Query(
            value = "SELECT * FROM product_positions " +
                    "WHERE product_id = :id AND warehouse_id = :warehouseId",
            nativeQuery = true
    )
    List<ProductPosition> findByProductIdAndWarehouseId(@Param(value = "id") Long id, @Param(value = "warehouseId") Long warehouseId);
}