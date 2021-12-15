package com.ncedu.fooddelivery.api.v1.repos.order;

import com.ncedu.fooddelivery.api.v1.entities.order.OrderNotHierarchical;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderNotHierarchicalRepo extends JpaRepository<OrderNotHierarchical, Long> {
    Page<OrderNotHierarchical> findAll(Specification<OrderNotHierarchical> spec, Pageable pageable);
}
