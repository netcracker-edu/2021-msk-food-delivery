package com.ncedu.fooddelivery.api.v1.repos;

import com.ncedu.fooddelivery.api.v1.entities.ProductPosition;
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
                            "WHERE ( (product_positions.manufacture_date + products.expiration_days) < CURRENT_DATE));",
            nativeQuery = true
    )
    List<ProductPosition> findExpiredPositions();

    @Query(
            value = "SELECT * FROM product_positions " +
                    "WHERE product_position_id IN " +
                    "(SELECT product_position_id " +
                    "FROM products INNER JOIN product_positions USING (product_id) " +
                    "WHERE ( warehouse_id = :id AND (product_positions.manufacture_date + products.expiration_days) < CURRENT_DATE));",
            nativeQuery = true
    )
    List<ProductPosition> findExpiredPositions(@Param(value = "id") Long id);

}
