package com.ncedu.fooddelivery.api.v1.services;

import com.ncedu.fooddelivery.api.v1.dto.ProductPositionDTOs.AcceptSupplyDTO;
import com.ncedu.fooddelivery.api.v1.dto.ProductPositionDTOs.ProductPositionInfoDTO;
import com.ncedu.fooddelivery.api.v1.entities.Order;
import com.ncedu.fooddelivery.api.v1.entities.ProductPosition;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

public interface ProductPositionService {
    ProductPositionInfoDTO getProductPositionInfoDTOById(Long id);
    Long acceptSupply(AcceptSupplyDTO acceptSupplyDTO);

    boolean deleteProductPosition(Long id);

    ProductPosition getProductPosition(Long id);

    boolean nullifyProductPosition(Long id);

    boolean updatePaymentStatus(List<Long> ids);

    List<ProductPositionInfoDTO> getExpiredPositions();

    List<ProductPositionInfoDTO> getExpiredPositions(Long warehouseId);

    List<AbstractMap.SimpleEntry<Integer, ProductPositionInfoDTO>> getPositionsFromOrder(Order order);

}
