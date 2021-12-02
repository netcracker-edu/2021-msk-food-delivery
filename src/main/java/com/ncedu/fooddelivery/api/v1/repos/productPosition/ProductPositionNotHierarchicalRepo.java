package com.ncedu.fooddelivery.api.v1.repos.productPosition;

import com.ncedu.fooddelivery.api.v1.entities.productPosition.ProductPosition;
import com.ncedu.fooddelivery.api.v1.entities.productPosition.ProductPositionNotHierarchical;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductPositionNotHierarchicalRepo extends JpaRepository<ProductPositionNotHierarchical, Long> {
    Page<ProductPositionNotHierarchical> findAll(Specification<ProductPositionNotHierarchical> spec, Pageable pageable);
}
