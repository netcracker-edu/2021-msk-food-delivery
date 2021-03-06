package com.ncedu.fooddelivery.api.v1.services;

import com.ncedu.fooddelivery.api.v1.dto.ProductPositionDTOs.AcceptSupplyDTO;
import com.ncedu.fooddelivery.api.v1.dto.ProductPositionDTOs.ProductPositionInfoDTO;
import com.ncedu.fooddelivery.api.v1.dto.ProductPositionDTOs.ProductPositionsFromOrderDTO;
import com.ncedu.fooddelivery.api.v1.dto.ProductPositionDTOs.ProductPositionsShipmentDTO;
import com.ncedu.fooddelivery.api.v1.entities.order.Order;
import com.ncedu.fooddelivery.api.v1.entities.productPosition.ProductPosition;
import com.ncedu.fooddelivery.api.v1.entities.productPosition.ProductPositionNotHierarchical;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface ProductPositionService {
    ProductPositionInfoDTO getProductPositionInfoDTOById(Long id);
    Long acceptSupply(AcceptSupplyDTO acceptSupplyDTO);

    boolean deleteProductPosition(Long id);

    ProductPosition getProductPosition(Long id);

    boolean nullifyProductPosition(Long id);

    boolean updatePaymentStatus(List<Long> ids);

    List<ProductPositionInfoDTO> getExpiredPositions(Pageable pageable);

    List<ProductPositionInfoDTO> getExpiredPositions(Long warehouseId, Pageable pageable);

    ProductPositionsFromOrderDTO getPositionsFromOrder(Order order);

    List<ProductPositionInfoDTO> findFiltered(Specification<ProductPositionNotHierarchical> spec, Pageable pageable);

    void shipProductPositions(Long orderId, ProductPositionsShipmentDTO productPositionsShipmentDTO);
}
