package com.ncedu.fooddelivery.api.v1.specifications;

import com.ncedu.fooddelivery.api.v1.dto.ProductPositionDTOs.ProductPositionFilterDTO;
import com.ncedu.fooddelivery.api.v1.entities.productPosition.ProductPositionNotHierarchical;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.Date;

public class ProductPositionSpecifications {

    public static Specification<ProductPositionNotHierarchical> getFilterSpecification(ProductPositionFilterDTO filterDTO){
        return  hasProductId(filterDTO.getProductId()).
                and(hasWarehouseId(filterDTO.getWarehouseId())).
                and(hasCurrentAmount(filterDTO.getCurrentAmount())).
                and(hasSupplyAmount(filterDTO.getSupplyAmount())).
                and(hasManufactureDate(filterDTO.getManufactureDate())).
                and(hasSupplierInvoice(filterDTO.getSupplierInvoice())).
                and(hasSupplyDate(filterDTO.getSupplyDate())).
                and(hasSupplierName(filterDTO.getSupplierName())).
                and(hasWarehouseSection(filterDTO.getWarehouseSection())).
                and(hasInvoicePaid(filterDTO.getIsInvoicePaid()));
    }

    public static Specification<ProductPositionNotHierarchical> hasWarehouseId(Long id){
        return new Specification<ProductPositionNotHierarchical>() {
            @Override
            public Predicate toPredicate(Root<ProductPositionNotHierarchical> root, CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
                if(id == null) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
                return criteriaBuilder.equal(root.get("warehouseId"), id);
            }
        };
    }

    public static Specification<ProductPositionNotHierarchical> hasProductId(Long id){
        return new Specification<ProductPositionNotHierarchical>() {
            @Override
            public Predicate toPredicate(Root<ProductPositionNotHierarchical> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if(id == null) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
                return criteriaBuilder.equal(root.get("productId"), id);
            }
        };
    }

    public static Specification<ProductPositionNotHierarchical> hasWarehouseSection(String warehouseSection){
        return new Specification<ProductPositionNotHierarchical>() {
            @Override
            public Predicate toPredicate(Root<ProductPositionNotHierarchical> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if(warehouseSection == null) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
                return criteriaBuilder.equal(root.get("warehouseSection"), warehouseSection);
            }
        };
    }

    public static Specification<ProductPositionNotHierarchical> hasSupplyAmount(Integer amount){
        return new Specification<ProductPositionNotHierarchical>() {
            @Override
            public Predicate toPredicate(Root<ProductPositionNotHierarchical> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if(amount == null) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
                return criteriaBuilder.equal(root.get("supplyAmount"), amount);
            }
        };
    }

    public static Specification<ProductPositionNotHierarchical> hasCurrentAmount(Integer amount){
        return new Specification<ProductPositionNotHierarchical>() {
            @Override
            public Predicate toPredicate(Root<ProductPositionNotHierarchical> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if(amount == null) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
                return criteriaBuilder.equal(root.get("currentAmount"), amount);
            }
        };
    }

    public static Specification<ProductPositionNotHierarchical> hasSupplyDate(Date date){
        return new Specification<ProductPositionNotHierarchical>() {
            @Override
            public Predicate toPredicate(Root<ProductPositionNotHierarchical> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if(date == null) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
                return criteriaBuilder.equal(root.<Date>get("supplyDate"), date);
            }
        };
    }

    public static Specification<ProductPositionNotHierarchical> hasSupplierInvoice(BigDecimal supplierInvoice){
        return new Specification<ProductPositionNotHierarchical>() {
            @Override
            public Predicate toPredicate(Root<ProductPositionNotHierarchical> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if(supplierInvoice == null) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
                return criteriaBuilder.equal(root.get("supplierInvoice"), supplierInvoice);
            }
        };
    }

    public static Specification<ProductPositionNotHierarchical> hasSupplierName(String supplierName){
        return new Specification<ProductPositionNotHierarchical>() {
            @Override
            public Predicate toPredicate(Root<ProductPositionNotHierarchical> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if(supplierName == null) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
                return criteriaBuilder.equal(root.get("supplierName"), supplierName);
            }
        };
    }

    public static Specification<ProductPositionNotHierarchical> hasInvoicePaid(Boolean isInvoicePaid){
        return new Specification<ProductPositionNotHierarchical>() {
            @Override
            public Predicate toPredicate(Root<ProductPositionNotHierarchical> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if(isInvoicePaid == null) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
                return criteriaBuilder.equal(root.get("isInvoicePaid"), isInvoicePaid);
            }
        };
    }

    public static Specification<ProductPositionNotHierarchical> hasManufactureDate(Date date){
        return new Specification<ProductPositionNotHierarchical>() {
            @Override
            public Predicate toPredicate(Root<ProductPositionNotHierarchical> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if(date == null) return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
                return criteriaBuilder.equal(root.<Date>get("manufactureDate"), date);
            }
        };
    }
}
