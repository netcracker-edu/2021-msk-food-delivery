package com.ncedu.fooddelivery.api.v1.specifications;

import com.ncedu.fooddelivery.api.v1.entities.OrderStatus;
import com.ncedu.fooddelivery.api.v1.entities.order.OrderNotHierarchical;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

public class OrderSpecifications {

    public static Specification<OrderNotHierarchical> getFilterSpecification(
            Long clientId, Long warehouseId, Long courierId, String address,
            OrderStatus status, Date dateStart, Date dateEnd,
            BigDecimal overallCost, BigDecimal highDemandCoeff,
            BigDecimal discount, Long promoCodeId,
            BigDecimal clientRating, BigDecimal deliveryRating){

        return  hasClientId(clientId).
                and(hasWarehouseId(warehouseId)).
                and(hasCourierId(courierId)).
                and(hasAddress(address)).
                and(hasStatus(status)).
                and(hasDateStart(dateStart)).
                and(hasDateEnd(dateEnd)).
                and(hasOverallCost(overallCost)).
                and(hasHighDemandCoeff(highDemandCoeff)).
                and(hasDiscount(discount)).
                and(hasPromoCodeId(promoCodeId)).
                and(hasClientRating(clientRating)).
                and(hasDeliveryRating(deliveryRating));
    }

    public static Specification<OrderNotHierarchical> hasClientId(Long id){
        return new Specification<OrderNotHierarchical>() {
            @Override
            public Predicate toPredicate(Root<OrderNotHierarchical> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if(id == null) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
                return criteriaBuilder.equal(root.get("clientId"), id);
            }
        };
    }

    public static Specification<OrderNotHierarchical> hasWarehouseId(Long id){
        return new Specification<OrderNotHierarchical>() {
            @Override
            public Predicate toPredicate(Root<OrderNotHierarchical> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if(id == null) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
                return criteriaBuilder.equal(root.get("warehouseId"), id);
            }
        };
    }

    public static Specification<OrderNotHierarchical> hasCourierId(Long id){
        return new Specification<OrderNotHierarchical>() {
            @Override
            public Predicate toPredicate(Root<OrderNotHierarchical> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if(id == null) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
                return criteriaBuilder.equal(root.get("courierId"), id);
            }
        };
    }

    public static Specification<OrderNotHierarchical> hasAddress(String address){
        return new Specification<OrderNotHierarchical>() {
            @Override
            public Predicate toPredicate(Root<OrderNotHierarchical> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if(address == null) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
                return criteriaBuilder.equal(root.get("address"), address);
            }
        };
    }

    public static Specification<OrderNotHierarchical> hasStatus(OrderStatus status){
        return new Specification<OrderNotHierarchical>() {
            @Override
            public Predicate toPredicate(Root<OrderNotHierarchical> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if(status == null) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
                return criteriaBuilder.equal(root.get("status"), status);
            }
        };
    }

    public static Specification<OrderNotHierarchical> hasDateStart(Date date){
        return new Specification<OrderNotHierarchical>() {
            @Override
            public Predicate toPredicate(Root<OrderNotHierarchical> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if(date == null) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
                return criteriaBuilder.equal(root.<Date>get("dateStart"), date);
            }
        };
    }

    public static Specification<OrderNotHierarchical> hasDateEnd(Date date){
        return new Specification<OrderNotHierarchical>() {
            @Override
            public Predicate toPredicate(Root<OrderNotHierarchical> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if(date == null) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
                return criteriaBuilder.equal(root.<Date>get("dateEnd"), date);
            }
        };
    }

    public static Specification<OrderNotHierarchical> hasOverallCost(BigDecimal cost){
        return new Specification<OrderNotHierarchical>() {
            @Override
            public Predicate toPredicate(Root<OrderNotHierarchical> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if(cost == null) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
                return criteriaBuilder.equal(root.<BigDecimal>get("overallCost"), cost);
            }
        };
    }

    public static Specification<OrderNotHierarchical> hasHighDemandCoeff(BigDecimal coeff){
        return new Specification<OrderNotHierarchical>() {
            @Override
            public Predicate toPredicate(Root<OrderNotHierarchical> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if(coeff == null) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
                return criteriaBuilder.equal(root.<BigDecimal>get("highDemandCoeff"), coeff);
            }
        };
    }

    public static Specification<OrderNotHierarchical> hasDiscount(BigDecimal discount){
        return new Specification<OrderNotHierarchical>() {
            @Override
            public Predicate toPredicate(Root<OrderNotHierarchical> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if(discount == null) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
                return criteriaBuilder.equal(root.<BigDecimal>get("discount"), discount);
            }
        };
    }

    public static Specification<OrderNotHierarchical> hasPromoCodeId(Long promoCodeId){
        return new Specification<OrderNotHierarchical>() {
            @Override
            public Predicate toPredicate(Root<OrderNotHierarchical> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if(promoCodeId == null) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
                return criteriaBuilder.equal(root.<Long>get("promoCodeId"), promoCodeId);
            }
        };
    }

    public static Specification<OrderNotHierarchical> hasClientRating(BigDecimal clientRating){
        return new Specification<OrderNotHierarchical>() {
            @Override
            public Predicate toPredicate(Root<OrderNotHierarchical> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if(clientRating == null) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
                return criteriaBuilder.equal(root.<BigDecimal>get("clientRating"), clientRating);
            }
        };
    }

    public static Specification<OrderNotHierarchical> hasDeliveryRating(BigDecimal deliveryRating){
        return new Specification<OrderNotHierarchical>() {
            @Override
            public Predicate toPredicate(Root<OrderNotHierarchical> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if(deliveryRating == null) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
                return criteriaBuilder.equal(root.<BigDecimal>get("deliveryRating"), deliveryRating);
            }
        };
    }
}
