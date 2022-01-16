package com.ncedu.fooddelivery.api.v1.services.impls;

import com.ncedu.fooddelivery.api.v1.dto.ProductPositionDTOs.ProductPositionsShipmentDTO;
import com.ncedu.fooddelivery.api.v1.dto.order.CountOrderCostRequestDTO;
import com.ncedu.fooddelivery.api.v1.dto.order.CountOrderCostResponseDTO;
import com.ncedu.fooddelivery.api.v1.dto.order.OrderInfoDTO;
import com.ncedu.fooddelivery.api.v1.dto.warehouseDTOs.WarehouseInfoDTO;
import com.ncedu.fooddelivery.api.v1.entities.Product;
import com.ncedu.fooddelivery.api.v1.entities.Role;
import com.ncedu.fooddelivery.api.v1.entities.User;
import com.ncedu.fooddelivery.api.v1.entities.order.Order;
import com.ncedu.fooddelivery.api.v1.entities.order.OrderNotHierarchical;
import com.ncedu.fooddelivery.api.v1.entities.productPosition.ProductPosition;
import com.ncedu.fooddelivery.api.v1.errors.badrequest.NotUniqueIdException;
import com.ncedu.fooddelivery.api.v1.errors.notfound.NotFoundEx;
import com.ncedu.fooddelivery.api.v1.errors.orderRegistration.CourierAvailabilityEx;
import com.ncedu.fooddelivery.api.v1.errors.orderRegistration.ProductAvailabilityEx;
import com.ncedu.fooddelivery.api.v1.repos.CourierRepo;
import com.ncedu.fooddelivery.api.v1.repos.ProductRepo;
import com.ncedu.fooddelivery.api.v1.repos.order.OrderNotHierarchicalRepo;
import com.ncedu.fooddelivery.api.v1.repos.order.OrderRepo;
import com.ncedu.fooddelivery.api.v1.repos.orderProductPosition.OrderNotHierarchicalProductPositionRepo;
import com.ncedu.fooddelivery.api.v1.repos.productPosition.ProductPositionRepo;
import com.ncedu.fooddelivery.api.v1.services.OrderService;
import com.ncedu.fooddelivery.api.v1.services.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl1 implements OrderService {

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    OrderNotHierarchicalRepo orderNotHierarchicalRepo;

    @Autowired
    OrderNotHierarchicalProductPositionRepo orderNotHierarchicalProductPositionRepo;

    @Autowired
    ProductPositionRepo productPositionRepo;

    @Autowired
    WarehouseService warehouseService;

    @Autowired
    ProductRepo productRepo;

    @Autowired
    CourierRepo courierRepo;

    @Override
    public Order getOrder(Long id) {
        Optional<Order> optionalOrder = orderRepo.findById(id);
        if(optionalOrder.isEmpty()) return null;
        else return optionalOrder.get();
    }

    @Override
    public List<OrderInfoDTO> findFiltered(Specification<OrderNotHierarchical> spec, Pageable pageable) {

        return orderNotHierarchicalRepo.findAll(spec, pageable).stream().map(order -> convertToOrderInfoDTO(order)).collect(Collectors.toList());
    }

    @Override
    public List<OrderInfoDTO> getOrdersHistory(User user, Pageable pageable) {
        if(user.getRole() == Role.CLIENT) return orderRepo.getClientOrdersHistory(user.getId(), pageable).stream().map(order -> convertToOrderInfoDTO(order)).collect(Collectors.toList());
        return orderRepo.getCourierOrdersHistory(user.getId(), pageable).stream().map(order -> convertToOrderInfoDTO(order)).collect(Collectors.toList());
    }

    @Override
    public CountOrderCostResponseDTO countOrderPrice(CountOrderCostRequestDTO dto) {
        WarehouseInfoDTO warehouse = warehouseService.getNearestWarehouse(dto.getGeo().getLat(), dto.getGeo().getLon());
        if(warehouse == null) throw new NotFoundEx(String.format("{Lat: %s; lon: $s}", dto.getGeo().getLat().toString(), dto.getGeo().getLon().toString()));
        Long warehouseId = warehouse.getId();

        List<Long> notFoundProductsIds = new ArrayList<>();
        Map<Long, Integer> productsAvailableAmounts = new HashMap<>();

        Double overallOrderCost = 0.0;
        Double overallOrderDiscount = 0.0;
        Double orderHighDemandCoeff;

        checkIdsUnique(dto.getProductAmountPairs());

        for(CountOrderCostRequestDTO.ProductAmountPair pair: dto.getProductAmountPairs()){
            Long productId = pair.getId();
            Integer requestedAmount = pair.getAmount();

            List<ProductPosition> productPositions = productPositionRepo.findByProductIdAndWarehouseId(productId, warehouseId);
            if(productPositions.size() == 0){
                notFoundProductsIds.add(productId);
                continue;
            }

            //filtering expired product positions
            productPositions = productPositions.stream().filter(new Predicate<ProductPosition>() {
                @Override
                public boolean test(ProductPosition productPosition) {
                    Short expirationDays = productPosition.getProduct().getExpirationDays();
                    Date supplyDate = productPosition.getSupplyDate();
                    Calendar c = Calendar.getInstance();
                    c.setTime(supplyDate);
                    c.add(Calendar.DATE , expirationDays);
                    if(c.before(Calendar.getInstance())) return false;
                    return true;
                }
            }).collect(Collectors.toList());

            Integer overallAmount = 0;
            for(ProductPosition pos: productPositions){
                overallAmount += pos.getCurrentAmount();
            }
            if(overallAmount < requestedAmount){
                productsAvailableAmounts.put(productId, overallAmount);
            }

            // product price count
            Product product = productRepo.findById(productId).get();
            Double productDiscount = product.getDiscount();
            Double productPrice = product.getPrice();
            overallOrderCost += requestedAmount * (productPrice - productDiscount);
            overallOrderDiscount += productDiscount * requestedAmount;

        }

        if(!productsAvailableAmounts.isEmpty() || !notFoundProductsIds.isEmpty()){
            throw new ProductAvailabilityEx(notFoundProductsIds, productsAvailableAmounts);
        }

        orderHighDemandCoeff = countHighDemandCoeff(warehouseId);
        overallOrderCost = Math.round(overallOrderCost * orderHighDemandCoeff * 100.0) / 100.0;
        return new CountOrderCostResponseDTO(overallOrderCost, overallOrderDiscount, orderHighDemandCoeff);
    }

    public Double countHighDemandCoeff(Long warehouseId){
        short workingCouriersAmount = courierRepo.countWorkingCouriersByWarehouse(warehouseId);
        short deliveringCouriersAmount = courierRepo.countDeliveringCouriersByWarehouse(warehouseId);
        if(workingCouriersAmount == deliveringCouriersAmount) throw new CourierAvailabilityEx();
        int waitingCouriersAmount = workingCouriersAmount - deliveringCouriersAmount;

        double waitingToWorkingProportion = Math.round( ((double) waitingCouriersAmount / (double) workingCouriersAmount) * 100.0) / 100.0;

        if(waitingToWorkingProportion > 0.3) return 1.0;

        Double baseCoeff = 1.0;
        return baseCoeff + 0.05 * (20 - Math.round(waitingToWorkingProportion / 0.015));
    }

    private void checkIdsUnique(List<CountOrderCostRequestDTO.ProductAmountPair> pairs){
        List<Long> ids = pairs.stream().map(pair -> pair.getId()).collect(Collectors.toList());
        List<Long> uniqueIds = ids.stream().distinct().collect(Collectors.toList());
        if(ids.size() != uniqueIds.size()) throw new NotUniqueIdException();
    }

    public OrderInfoDTO convertToOrderInfoDTO(OrderNotHierarchical orderNotHierarchical){
        Order order = orderRepo.findById(orderNotHierarchical.getId()).get();
        return new OrderInfoDTO(
                order.getId(), order.getClient(), order.getAddress(),
                order.getCoordinates(), order.getWarehouse(),
                order.getCourier(), order.getStatus(), order.getDateStart(),
                order.getDateEnd(), order.getOverallCost(), order.getHighDemandCoeff(),
                order.getDiscount(), order.getPromoCodeId(), order.getClientRating(),
                order.getDeliveryRating(), orderNotHierarchicalProductPositionRepo.findAllByOrderId(order.getId())
        );
    }

    public OrderInfoDTO convertToOrderInfoDTO(Order order){
        return new OrderInfoDTO(
                order.getId(), order.getClient(), order.getAddress(),
                order.getCoordinates(), order.getWarehouse(),
                order.getCourier(), order.getStatus(), order.getDateStart(),
                order.getDateEnd(), order.getOverallCost(), order.getHighDemandCoeff(),
                order.getDiscount(), order.getPromoCodeId(), order.getClientRating(),
                order.getDeliveryRating(), orderNotHierarchicalProductPositionRepo.findAllByOrderId(order.getId())
        );
    }
}
