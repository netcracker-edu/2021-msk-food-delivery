package com.ncedu.fooddelivery.api.v1.specifications;

import com.ncedu.fooddelivery.api.v1.entities.OrderStatus;
import com.ncedu.fooddelivery.api.v1.entities.order.Order;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

public class OrderSpecifications {

    private static final Calendar cal = Calendar.getInstance();

    public static Specification<Order> getFilterSpecification(
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

    public static Specification<Order> hasClientId(Long id){
        return new Specification<Order>() {
            @Override
            public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if(id == null) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
                return criteriaBuilder.equal(root.get("client").get("id"), id);
            }
        };
    }

    public static Specification<Order> hasWarehouseId(Long id){
        return new Specification<Order>() {
            @Override
            public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if(id == null) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
                return criteriaBuilder.equal(root.get("warehouse").get("id"), id);
            }
        };
    }

    public static Specification<Order> hasCourierId(Long id){
        return new Specification<Order>() {
            @Override
            public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if(id == null) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
                return criteriaBuilder.equal(root.get("courier").get("id"), id);
            }
        };
    }

    public static Specification<Order> hasAddress(String address){
        return new Specification<Order>() {
            @Override
            public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if(address == null) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
                return criteriaBuilder.equal(root.get("address"), address);
            }
        };
    }

    public static Specification<Order> hasStatus(OrderStatus status){
        return new Specification<Order>() {
            @Override
            public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if(status == null) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
                return criteriaBuilder.equal(root.get("status"), status);
            }
        };
    }

    public static Specification<Order> hasDateStart(Date date){
        return new Specification<Order>() {
            @Override
            public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if(date == null) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
                cal.setTime(date);
                cal.add(Calendar.DATE, 1);
                Date dateNextDay = cal.getTime();
                return criteriaBuilder.between(root.<Timestamp>get("dateStart"), new Timestamp(date.getTime()), new Timestamp(dateNextDay.getTime()));
            }
        };
    }

    public static Specification<Order> hasDateEnd(Date date){
        return new Specification<Order>() {
            @Override
            public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if(date == null) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
                cal.setTime(date);
                cal.add(Calendar.DATE, 1);
                Date dateNextDay = cal.getTime();
                return criteriaBuilder.between(root.<Timestamp>get("dateEnd"), new Timestamp(date.getTime()), new Timestamp(dateNextDay.getTime()));
            }
        };
    }

    public static Specification<Order> hasOverallCost(BigDecimal cost){
        return new Specification<Order>() {
            @Override
            public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if(cost == null) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
                return criteriaBuilder.equal(root.<BigDecimal>get("overallCost"), cost);
            }
        };
    }

    public static Specification<Order> hasHighDemandCoeff(BigDecimal coeff){
        return new Specification<Order>() {
            @Override
            public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if(coeff == null) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
                return criteriaBuilder.equal(root.<BigDecimal>get("highDemandCoeff"), coeff);
            }
        };
    }

    public static Specification<Order> hasDiscount(BigDecimal discount){
        return new Specification<Order>() {
            @Override
            public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if(discount == null) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
                return criteriaBuilder.equal(root.<BigDecimal>get("discount"), discount);
            }
        };
    }

    public static Specification<Order> hasPromoCodeId(Long promoCodeId){
        return new Specification<Order>() {
            @Override
            public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if(promoCodeId == null) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
                return criteriaBuilder.equal(root.<Long>get("promoCodeId"), promoCodeId);
            }
        };
    }

    public static Specification<Order> hasClientRating(BigDecimal clientRating){
        return new Specification<Order>() {
            @Override
            public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if(clientRating == null) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
                return criteriaBuilder.equal(root.<BigDecimal>get("clientRating"), clientRating);
            }
        };
    }

    public static Specification<Order> hasDeliveryRating(BigDecimal deliveryRating){
        return new Specification<Order>() {
            @Override
            public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if(deliveryRating == null) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
                return criteriaBuilder.equal(root.<BigDecimal>get("deliveryRating"), deliveryRating);
            }
        };
    }
}
