package com.ncedu.fooddelivery.api.v1.services.impls;

import com.ncedu.fooddelivery.api.v1.dto.ProductPositionDTOs.AcceptSupplyDTO;
import com.ncedu.fooddelivery.api.v1.dto.ProductPositionDTOs.ProductPositionInfoDTO;
import com.ncedu.fooddelivery.api.v1.entities.*;
import com.ncedu.fooddelivery.api.v1.entities.productPosition.ProductPosition;
import com.ncedu.fooddelivery.api.v1.entities.productPosition.ProductPositionNotHierarchical;
import com.ncedu.fooddelivery.api.v1.errors.notfound.ProductNotFoundException;
import com.ncedu.fooddelivery.api.v1.errors.notfound.WarehouseNotFoundException;
import com.ncedu.fooddelivery.api.v1.repos.OrderProductPositionRepo;
import com.ncedu.fooddelivery.api.v1.repos.ProductRepo;
import com.ncedu.fooddelivery.api.v1.repos.productPosition.ProductPositionNotHierarchicalRepo;
import com.ncedu.fooddelivery.api.v1.repos.productPosition.ProductPositionRepo;
import com.ncedu.fooddelivery.api.v1.repos.WarehouseRepo;
import com.ncedu.fooddelivery.api.v1.services.ProductPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductPositionServiceImpl1 implements ProductPositionService {

    @Autowired
    ProductPositionRepo productPositionRepo;

    @Autowired
    ProductPositionNotHierarchicalRepo productPositionNotHierarchicalRepo;

    @Autowired
    WarehouseRepo warehouseRepo;

    @Autowired
    ProductRepo productRepo;

    @Autowired
    OrderProductPositionRepo orderProductPositionRepo;

    @Override
    public ProductPositionInfoDTO getProductPositionInfoDTOById(Long id) {
        Optional<ProductPosition> optionalProductPosition = this.productPositionRepo.findById(id);
        if (optionalProductPosition.isEmpty()) return null;
        ProductPosition productPosition = optionalProductPosition.get();
        return convertToInfoDTO(productPosition);
    }

    @Override
    public Long acceptSupply(AcceptSupplyDTO acceptSupplyDTO) {
        ProductPosition productPosition = convertToProductPosition(acceptSupplyDTO);
        Long id = productPositionRepo.save(productPosition).getId();
        return id;
    }

    @Override
    public boolean deleteProductPosition(Long id) {
        Optional<ProductPosition> optionalProductPosition = productPositionRepo.findById(id);
        if(optionalProductPosition.isEmpty()) return false;
        productPositionRepo.delete(optionalProductPosition.get());
        return true;
    }

    @Override
    public ProductPosition getProductPosition(Long id) {
        Optional<ProductPosition> productPositionOptional = productPositionRepo.findById(id);
        if(productPositionOptional.isEmpty()) return null;
        return productPositionOptional.get();
    }

    @Override
    public boolean nullifyProductPosition(Long id) {
        ProductPosition productPosition = getProductPosition(id);
        if(productPosition == null) return false;
        productPosition.setCurrentAmount(0);
        productPositionRepo.save(productPosition);
        return true;
    }

    @Override
    public boolean updatePaymentStatus(List<Long> ids) {
        List<ProductPosition> productPositionList = new ArrayList<>();
        for(Long id: ids){
            ProductPosition productPosition = getProductPosition(id);
            if(id == null) return false;
            productPositionList.add(productPosition);
        }
        for(ProductPosition productPosition: productPositionList){
            productPosition.setIsInvoicePaid(true);
            productPositionRepo.save(productPosition);
        }
        return true;
    }

    @Override
    public List<ProductPositionInfoDTO> getExpiredPositions() {
        return productPositionRepo.findExpiredPositions().stream().map(p -> convertToInfoDTO(p)).collect(Collectors.toList());
    }

    @Override
    public List<ProductPositionInfoDTO> getExpiredPositions(Long warehouseId) {
        return productPositionRepo.findExpiredPositions(warehouseId).stream().map(p -> convertToInfoDTO(p)).collect(Collectors.toList());
    }

    @Override
    public List<AbstractMap.SimpleEntry<Integer, ProductPositionInfoDTO>> getPositionsFromOrder(Order order) {
        List<OrderProductPosition> ordersProductPositions = orderProductPositionRepo.findAllByOrder(order);
        List<AbstractMap.SimpleEntry<Integer, ProductPositionInfoDTO>> entries = new ArrayList<AbstractMap.SimpleEntry<Integer, ProductPositionInfoDTO>>();
        for(OrderProductPosition orderProductPosition: ordersProductPositions){
            entries.add(new AbstractMap.SimpleEntry<Integer, ProductPositionInfoDTO>(orderProductPosition.getAmount(), convertToInfoDTO(orderProductPosition.getProductPosition())));
        }
        return entries;
    }

    @Override
    public List<ProductPositionInfoDTO> findFiltered(Specification<ProductPositionNotHierarchical> spec, Pageable pageable) {
        Page<ProductPositionNotHierarchical> productPositions = productPositionNotHierarchicalRepo.findAll(spec, pageable);
        return productPositions.stream().map(position -> convertToInfoDTO(position)).collect(Collectors.toList());
    }

    public ProductPositionInfoDTO convertToInfoDTO(ProductPosition productPosition){
        return new ProductPositionInfoDTO(productPosition.getId(), productPosition.getProduct(),
                productPosition.getWarehouse(), productPosition.getWarehouseSection(),
                productPosition.getSupplyAmount(), productPosition.getCurrentAmount(),
                productPosition.getSupplyDate(), productPosition.getSupplierInvoice(),
                productPosition.getSupplierName(), productPosition.getIsInvoicePaid(),
                productPosition.getSupplyDate());
    }

    public ProductPositionInfoDTO convertToInfoDTO(ProductPositionNotHierarchical productPosition){
        return new ProductPositionInfoDTO(productPosition.getId(), productRepo.findById(productPosition.getProductId()).get(),
                warehouseRepo.findById(productPosition.getWarehouseId()).get(), productPosition.getWarehouseSection(),
                productPosition.getSupplyAmount(), productPosition.getCurrentAmount(),
                productPosition.getSupplyDate(), productPosition.getSupplierInvoice(),
                productPosition.getSupplierName(), productPosition.getIsInvoicePaid(),
                productPosition.getSupplyDate());
    }

    public ProductPosition convertToProductPosition(ProductPositionInfoDTO infoDTO){
        ProductPosition productPosition = new ProductPosition(infoDTO.getId(), infoDTO.getProduct(), infoDTO.getWarehouse(),
                                   infoDTO.getWarehouseSection(), infoDTO.getSupplyAmount(), infoDTO.getCurrentAmount(),
                                   infoDTO.getSupplyDate(), infoDTO.getSupplierInvoice(), infoDTO.getSupplierName(),
                                   infoDTO.getIsInvoicePaid(), infoDTO.getManufactureDate());
        if(infoDTO.getIsInvoicePaid() == null) productPosition.setIsInvoicePaid(false);
        return productPosition;
    }

    public ProductPosition convertToProductPosition(AcceptSupplyDTO acceptSupplyDTO){
        Optional<Product> productOptional = productRepo.findById(acceptSupplyDTO.getProductId());
        Optional<Warehouse> warehouseOptional = warehouseRepo.findById(acceptSupplyDTO.getWarehouseId());

        if(warehouseOptional.isEmpty()) throw new WarehouseNotFoundException(acceptSupplyDTO.getWarehouseId());
        if(productOptional.isEmpty()) throw new ProductNotFoundException(acceptSupplyDTO.getProductId());

        Product product = productOptional.get();
        Warehouse warehouse = warehouseOptional.get();

        ProductPosition productPosition = new ProductPosition(null,
                product, warehouse, acceptSupplyDTO.getWarehouseSection(),
                acceptSupplyDTO.getSupplyAmount(), acceptSupplyDTO.getSupplyAmount(), acceptSupplyDTO.getManufactureDate(),
                acceptSupplyDTO.getSupplierInvoice(), acceptSupplyDTO.getSupplierName(), acceptSupplyDTO.getIsInvoicePaid(),
                acceptSupplyDTO.getManufactureDate()
        );

        if(acceptSupplyDTO.getIsInvoicePaid() == null){
            productPosition.setIsInvoicePaid(false);
        }

        return productPosition;
    }

}
