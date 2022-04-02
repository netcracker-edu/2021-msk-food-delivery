package com.ncedu.fooddelivery.api.v1.services.impls;

import com.ncedu.fooddelivery.api.v1.dto.areCreatedDTO;
import com.ncedu.fooddelivery.api.v1.dto.CoordsDTO;
import com.ncedu.fooddelivery.api.v1.dto.order.*;
import com.ncedu.fooddelivery.api.v1.dto.product.ProductDTO;
import com.ncedu.fooddelivery.api.v1.dto.warehouseDTOs.WarehouseInfoDTO;
import com.ncedu.fooddelivery.api.v1.entities.*;
import com.ncedu.fooddelivery.api.v1.entities.order.Order;
import com.ncedu.fooddelivery.api.v1.entities.orderProductPosition.OrderProductPosition;
import com.ncedu.fooddelivery.api.v1.entities.productPosition.ProductPosition;
import com.ncedu.fooddelivery.api.v1.errors.badrequest.*;
import com.ncedu.fooddelivery.api.v1.errors.notfound.NotFoundEx;
import com.ncedu.fooddelivery.api.v1.errors.orderRegistration.CourierAvailabilityEx;
import com.ncedu.fooddelivery.api.v1.errors.orderRegistration.OrderCostChangedEx;
import com.ncedu.fooddelivery.api.v1.errors.orderRegistration.ProductAvailabilityEx;
import com.ncedu.fooddelivery.api.v1.errors.orderRegistration.WarehouseCoordsBindingEx;
import com.ncedu.fooddelivery.api.v1.errors.security.CustomAccessDeniedException;
import com.ncedu.fooddelivery.api.v1.mappers.ProductMapper;
import com.ncedu.fooddelivery.api.v1.repos.CourierRepo;
import com.ncedu.fooddelivery.api.v1.repos.ProductRepo;
import com.ncedu.fooddelivery.api.v1.repos.order.OrderRepo;
import com.ncedu.fooddelivery.api.v1.repos.orderProductPosition.OrderProductPositionRepo;
import com.ncedu.fooddelivery.api.v1.repos.productPosition.ProductPositionRepo;
import com.ncedu.fooddelivery.api.v1.services.CourierService;
import com.ncedu.fooddelivery.api.v1.services.OrderService;
import com.ncedu.fooddelivery.api.v1.services.WarehouseService;
import com.ncedu.fooddelivery.api.v1.specifications.OrderSpecifications;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl1 implements OrderService {

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    OrderProductPositionRepo orderProductPositionRepo;

    @Autowired
    ProductPositionRepo productPositionRepo;

    @Autowired
    WarehouseService warehouseService;

    @Autowired
    ProductRepo productRepo;

    @Autowired
    CourierService courierService;

    @Autowired
    CourierRepo courierRepo;

    GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
    ProductMapper productMapper = ProductMapper.INSTANCE;

    @Override
    public Order getOrder(Long id) {
        Optional<Order> optionalOrder = orderRepo.findById(id);
        if(optionalOrder.isEmpty()) return null;
        else return optionalOrder.get();
    }

    @Override
    public List<OrderInfoDTO> findFiltered(User user, OrderFilterDTO dto, Pageable pageable) {
        Specification<Order> spec;

        if(user.getRole() == Role.MODERATOR){
            Long moderatorWarehouseId = user.getModerator().getWarehouseId();
            if(dto.getWarehouseId() != null){
                if(!dto.getWarehouseId().equals(moderatorWarehouseId)) throw new CustomAccessDeniedException();
            }

        } else dto.setWarehouseId(null);

        spec = OrderSpecifications.getFilterSpecification(dto);
        return orderRepo.findAll(spec, pageable).stream().map(order -> convertToOrderInfoDTO(order)).collect(Collectors.toList());
    }

    @Override
    public List<OrderInfoDTO> getOrdersHistory(User authedUser, User targetUser, Pageable pageable) {
        if(targetUser.getRole() == Role.ADMIN || targetUser.getRole() == Role.MODERATOR) throw new IncorrectUserRoleRequestException();

        List<OrderInfoDTO> orders;
        if(targetUser.getRole() == Role.CLIENT){
            orders = orderRepo.getClientOrdersHistory(targetUser.getId(), pageable).stream()
                              .map(order -> convertToOrderInfoDTO(order))
                              .collect(Collectors.toList());
        } else {
            orders = orderRepo.getCourierOrdersHistory(targetUser.getId(), pageable).stream()
                              .map(order -> convertToOrderInfoDTO(order))
                              .collect(Collectors.toList());
        }

        if(authedUser.getRole() == Role.MODERATOR){
            orders = orders.stream().filter(order -> order.getWarehouse().getId().equals(authedUser.getModerator().getWarehouseId()))
                                    .collect(Collectors.toList());
        }
        return orders;
    }

    @Override
    public List<OrderInfoDTO> getMyOrdersHistory(User user, Pageable pageable) {
        if(user.getRole() == Role.CLIENT) return orderRepo.getClientOrdersHistory(user.getId(), pageable).stream()
                                                          .map(order -> convertToOrderInfoDTO(order))
                                                          .collect(Collectors.toList());

        return orderRepo.getCourierOrdersHistory(user.getId(), pageable).stream()
                        .map(order -> convertToOrderInfoDTO(order)).collect(Collectors.toList());
    }

    @Override
    public List<OrderInfoDTO> getOrdersFromDeliverySession(User courier, DeliverySession deliverySession) {
        if(!courier.getId().equals(deliverySession.getCourier().getId())) throw new CustomAccessDeniedException();
        LocalDateTime endTime = deliverySession.getEndTime() == null ? LocalDateTime.now() : deliverySession.getEndTime();
        return orderRepo.getOrdersByCourierIdAndTime(courier.getId(),
                deliverySession.getStartTime(),
                endTime)
                .stream().map(order -> convertToOrderInfoDTO(order))
                .collect(Collectors.toList());
    }

    @Override
    public Double[] countOrderCost(CoordsDTO geo,
                                   HashMap<Long, Integer> pairs, Long clientWarehouseId) {
        WarehouseInfoDTO warehouse = warehouseService.getNearestWarehouse(geo.getLat(), geo.getLon());
        if(warehouse == null) throw new NotFoundEx(String.format("Lat: %s; lon: %s", geo.getLat().toString(),
                geo.getLon().toString()));
        Long warehouseId = warehouse.getId();
        if(!warehouseId.equals(clientWarehouseId)) throw new WarehouseCoordsBindingEx();

        Map<Long, Integer> productsAvailableAmounts = new HashMap<>();

        Double overallOrderCost = 0.0;
        Double overallOrderDiscount = 0.0;
        Double orderHighDemandCoeff;

        boolean enoughProductPositions = true;
        for(Map.Entry<Long, Integer> pair: pairs.entrySet()){
            Long productId = pair.getKey();
            Integer requestedAmount = pair.getValue();

            // 'findByProductIdAndWarehouseIdWithLock' sets lock on every product position in result set. So no one
            // other thread
            // will be able to interact with these positions before whole transaction will complete.
            List<ProductPosition> productPositions = productPositionRepo.findByProductIdAndWarehouseIdWithLock(productId, warehouseId);

            //filtering expired product positions
            productPositions = productPositions.stream().filter(new Predicate<ProductPosition>() {
                @Override
                public boolean test(ProductPosition productPosition) {
                    Short expirationDays = productPosition.getProduct().getExpirationDays();
                    Date manufactureDate = productPosition.getManufactureDate();
                    Calendar c = Calendar.getInstance();
                    c.setTime(manufactureDate);
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
                enoughProductPositions = false;
            }

            if(enoughProductPositions){
                // product price count
                Product product = productPositions.get(0).getProduct();
                Double productDiscount = product.getDiscount();
                Double productPrice = product.getPrice();
                overallOrderCost += requestedAmount * (productPrice - productDiscount);
                overallOrderDiscount += productDiscount * requestedAmount;
            }
        }

        if(!enoughProductPositions){
            throw new ProductAvailabilityEx(productsAvailableAmounts);
        }

        orderHighDemandCoeff = countHighDemandCoeff(warehouseId);
        overallOrderCost = Math.round(overallOrderCost * orderHighDemandCoeff * 100.0) / 100.0;
        return new Double[]{overallOrderCost, overallOrderDiscount, orderHighDemandCoeff};
    }

    @Override
    public CountOrderCostResponseDTO countOrderCost(CountOrderCostRequestDTO dto) {
        WarehouseInfoDTO warehouse = warehouseService.getNearestWarehouse(dto.getGeo().getLat(), dto.getGeo().getLon());
        if(warehouse == null) throw new NotFoundEx(String.format("{Lat: %s; lon: %s}", dto.getGeo().getLat().toString(),
                                                   dto.getGeo().getLon().toString()));
        Long warehouseId = warehouse.getId();
        if(!warehouseId.equals(dto.getWarehouseId())) throw new WarehouseCoordsBindingEx();

        Map<Long, Integer> productsAvailableAmounts = new HashMap<>();

        Double overallOrderCost = 0.0;
        Double overallOrderDiscount = 0.0;
        Double orderHighDemandCoeff;

        boolean enoughProductPositions = true;
        for(Map.Entry<Long, Integer> pair: dto.getProductAmountPairs().entrySet()){
            Long productId = pair.getKey();
            Integer requestedAmount = pair.getValue();

            List<ProductPosition> productPositions = productPositionRepo.findByProductIdAndWarehouseId(productId, warehouseId);

            //filtering expired product positions
            productPositions = productPositions.stream().filter(new Predicate<ProductPosition>() {
                @Override
                public boolean test(ProductPosition productPosition) {
                    Short expirationDays = productPosition.getProduct().getExpirationDays();
                    Date manufactureDate = productPosition.getManufactureDate();
                    Calendar c = Calendar.getInstance();
                    c.setTime(manufactureDate);
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
                enoughProductPositions = false;
            }
            if(enoughProductPositions){
                // product price count
                Product product = productRepo.findById(productId).get();
                Double productDiscount = product.getDiscount();
                Double productPrice = product.getPrice();
                overallOrderCost += requestedAmount * (productPrice - productDiscount);
                overallOrderDiscount += productDiscount * requestedAmount;
            }
        }

        if(!enoughProductPositions){
            throw new ProductAvailabilityEx(productsAvailableAmounts);
        }

        orderHighDemandCoeff = countHighDemandCoeff(warehouseId);
        overallOrderCost = Math.round(overallOrderCost * orderHighDemandCoeff * 100.0) / 100.0;

        return new CountOrderCostResponseDTO(overallOrderCost, overallOrderDiscount, orderHighDemandCoeff);
    }

    public Double countHighDemandCoeff(Long warehouseId){
        short workingCouriersAmount = courierRepo.countWorkingCouriersByWarehouse(warehouseId);
        short deliveringCouriersAmount = courierRepo.countDeliveringCouriersByWarehouse(warehouseId);

        int waitingCouriersAmount = workingCouriersAmount - deliveringCouriersAmount;

        double waitingToWorkingProportion = Math.round( ((double) waitingCouriersAmount / (double) workingCouriersAmount) * 100.0) / 100.0;

        if(waitingToWorkingProportion > 0.3) return 1.0;

        Double baseCoeff = 1.0;
        return baseCoeff + 0.05 * (20 - Math.round(waitingToWorkingProportion / 0.015));
    }

    @Override
    @Transactional
    public areCreatedDTO createOrder(CreateOrderDTO dto, User user) {
        checkOrderDataActuality(dto);

        Long warehouseId = dto.getWarehouseId();
        Geometry coords = geometryFactory.createPoint(new Coordinate(dto.getGeo().getLon().doubleValue(),
                dto.getGeo().getLat().doubleValue()));

        HashMap<Long, Integer> pairs = dto.getProductAmountPairs();
        Object[] productPosMaps = getProductPositionsData(pairs, warehouseId);

        // we will use later Maps below to reserve product positions from warehouse and put them in order(s)
        HashMap<Long, Double> productPosPriceMap = (HashMap<Long, Double>) productPosMaps[0];
        HashMap<Long, Integer> productPosAmountMap = (HashMap<Long, Integer>) productPosMaps[1];

        Double orderWeight = countOrderWeight(pairs);
        if(orderWeight > 15000d){
            int neededCouriersAmount = (int) Math.ceil(orderWeight / 15000d);
            List<ProductPosition> productPositions = getSortedProductPositions(productPosAmountMap);
            // we've got sorted by weight sequence of product positions, so we can put them into different orders

            List<Order> orders = new ArrayList<>();
            for(int i = 0; i < neededCouriersAmount; i++){
                Order order = buildOrder(user, coords, dto, productPositions);

                // attaching courier and then order status = 'courier_appointed'
                Courier courier;
                try{
                    courier = courierService.findFreeCourier(warehouseId);
                } catch (CourierAvailabilityEx ex){
                    for(Order o: orders){
                        o.setStatus(OrderStatus.CANCELLED);   // DB trigger will return all reserved product
                                                                  // positions back to warehouse
                        orderRepo.save(o);
                    }
                    throw ex;
                }
                order.setCourier(courier);
                order.setStatus(OrderStatus.COURIER_APPOINTED);
                orders.add(orderRepo.save(order));
            }

            return new areCreatedDTO(orders.stream().map(order -> order.getId()).collect(Collectors.toList()));

        } else {
            Order order = buildOrder(user, coords, dto, productPosPriceMap, productPosAmountMap);

            // attaching courier and then order status = 'courier_appointed'
            Courier courier;
            try{
                courier = courierService.findFreeCourier(warehouseId);
            } catch (CourierAvailabilityEx ex){
                order.setStatus(OrderStatus.CANCELLED);   // DB trigger will return all reserved product positions back
                // to warehouse
                orderRepo.save(order);
                throw ex;
            }
            order.setCourier(courier);
            order.setStatus(OrderStatus.COURIER_APPOINTED);
            orderRepo.save(order);
            return new areCreatedDTO(List.of(order.getId()));
        }
    }

    private Order buildOrder(User user, Geometry coords, CreateOrderDTO dto, Map<Long, Double> productPosPriceMap,
                             Map<Long, Integer> productPosAmountMap){
        Order order = new Order();
        order.setDiscount(dto.getDiscount());
        order.setHighDemandCoeff(dto.getHighDemandCoeff());
        order.setOverallCost(dto.getOverallCost());
        order.setClient(user.getClient());
        order.setWarehouse(warehouseService.findById(dto.getWarehouseId()));
        order.setAddress(dto.getAddress());
        order.setCoordinates(coords);
        order.setStatus(OrderStatus.CREATED);
        order.setDateStart(LocalDateTime.now());
        order = orderRepo.save(order);
        // adding records in DB table 'orders_product_positions'
        for(Long productPositionId: productPosAmountMap.keySet()){
            orderProductPositionRepo.save(new OrderProductPosition(order,
                    productPositionRepo.findById(productPositionId).get(),
                    productPosAmountMap.get(productPositionId),
                    productPosPriceMap.get(productPositionId)));
        }
        return orderRepo.save(order);

    }

    private Order buildOrder(User user, Geometry coords, CreateOrderDTO dto, List<ProductPosition> productPositions){
        Order order = new Order();
        final int weightLimit = 15000;
        int currentWeight = 0;

        Map<Long, Integer> currOrderPositions = new HashMap<>();
        // adding records in DB table 'orders_product_positions'
        for(int j = 0; j < productPositions.size() && weightLimit != currentWeight; j++){
            ProductPosition currPos = productPositions.get(j);
            int currPosWeight = currPos.getProduct().getWeight();
            if((weightLimit - currentWeight) >= currPosWeight){
                currentWeight += currPosWeight;
                currOrderPositions.merge(currPos.getId(), 1, Integer::sum);
                productPositions.remove(j);
                j--;
            }
        }

        Double orderCost = 0d;
        Double orderDiscount = 0d;
        for(Long psId: currOrderPositions.keySet()){
            int amount = currOrderPositions.get(psId);
            ProductPosition ps = productPositionRepo.findById(psId).get();
            Double price = (ps.getProduct().getPrice() - ps.getProduct().getDiscount()) * amount;
            Double discount = amount * ps.getProduct().getDiscount();
            orderCost += price;
            orderDiscount += discount;
        }
        order.setDiscount(orderDiscount);
        order.setHighDemandCoeff(dto.getHighDemandCoeff());
        order.setOverallCost(orderCost);
        order.setClient(user.getClient());
        order.setWarehouse(warehouseService.findById(dto.getWarehouseId()));
        order.setAddress(dto.getAddress());
        order.setCoordinates(coords);
        order.setStatus(OrderStatus.CREATED);
        order.setDateStart(LocalDateTime.now());
        order = orderRepo.save(order);
        for(Long psId: currOrderPositions.keySet()) {
            int amount = currOrderPositions.get(psId);
            ProductPosition ps = productPositionRepo.findById(psId).get();
            orderProductPositionRepo.save(new OrderProductPosition(order, ps, amount,
                    (ps.getProduct().getPrice() - ps.getProduct().getDiscount()) * amount));
        }
        return order;
    }

    @Override
    public OrderInfoDTO getCurrentOrder(User user) {
        Order o = this.findCouriersActiveOrder(user.getCourier());
        if(o == null) return null;
        return convertToOrderInfoDTO(o);
    }

    private List<ProductPosition> getSortedProductPositions(Map<Long, Integer> productPosAmountMap){
        List<ProductPosition> productPositions = new ArrayList<>();   // every product position in order
        for(Map.Entry<Long, Integer> prodPos: productPosAmountMap.entrySet()){
            productPositions.addAll(Collections.nCopies(prodPos.getValue(),
                    productPositionRepo.findById(prodPos.getKey()).get()));
        }
        productPositions = productPositions.stream().sorted(new Comparator<ProductPosition>() {
            @Override
            public int compare(ProductPosition ps1, ProductPosition ps2) {
                int weight1 = ps1.getProduct().getWeight(), weight2 = ps2.getProduct().getWeight();
                return weight1 > weight2 ? -1 : weight1 == weight2 ? 0 : 1;
            }
        }).collect(Collectors.toList());
        return productPositions;
    }

    private Object[] getProductPositionsData(HashMap<Long, Integer> pairs, Long warehouseId){
        Map<Long, Double> productPosPriceMap = new HashMap<>();     // productPositionId -> amount * (price -
        // discount)
        Map<Long, Integer> productPosAmountMap = new HashMap<>();   // productPositionId -> amount

        for(Map.Entry<Long, Integer> pair: pairs.entrySet()){
            Long productId = pair.getKey();
            Integer requestedAmount = pair.getValue();
            List<ProductPosition> productPositions = productPositionRepo.findByProductIdAndWarehouseIdWithLock(productId, warehouseId);

            //filtering expired product positions and sorting by manufacture date
            productPositions = productPositions.stream().filter(new Predicate<ProductPosition>() {
                @Override
                public boolean test(ProductPosition productPosition) {
                    Short expirationDays = productPosition.getProduct().getExpirationDays();
                    Date manufactureDate = productPosition.getManufactureDate();
                    Calendar c = Calendar.getInstance();
                    c.setTime(manufactureDate);
                    c.add(Calendar.DATE , expirationDays);
                    if(c.before(Calendar.getInstance())) return false;
                    return true;
                }
            }).sorted(new Comparator<ProductPosition>() {
                @Override
                public int compare(ProductPosition o1, ProductPosition o2) {
                    if(o1.getManufactureDate().before(o2.getManufactureDate())) return -1;
                    else if(o1.getManufactureDate().after(o2.getManufactureDate())) return 1;
                    return 0;
                }
            }).collect(Collectors.toList());

            reserveProductPositions(productPositions, requestedAmount, productPosPriceMap, productPosAmountMap);
        }
        return new Object[]{productPosPriceMap, productPosAmountMap};
    }

    private void reserveProductPositions(List<ProductPosition> productPositions, Integer requestedAmount,
                                         Map<Long, Double> productPosPriceMap, Map<Long, Integer> productPosAmountMap){
        Product product = productPositions.get(0).getProduct();
        Double productCost = product.getPrice() - product.getDiscount();
        Integer ship = 0;
        for(int i = 0; i < productPositions.size() && !ship.equals(requestedAmount); i++){
            ProductPosition currentPos = productPositions.get(i);
            if( (requestedAmount - ship) <= currentPos.getCurrentAmount()){
                currentPos.setCurrentAmount(currentPos.getCurrentAmount() - (requestedAmount - ship));
                productPositionRepo.save(currentPos);
                productPosAmountMap.put(currentPos.getId(), requestedAmount - ship);
                productPosPriceMap.put(currentPos.getId(), (requestedAmount - ship) * productCost);
                break;
            } else {
                ship += currentPos.getCurrentAmount();
                productPosAmountMap.put(currentPos.getId(), currentPos.getCurrentAmount());
                productPosPriceMap.put(currentPos.getId(), productCost * (currentPos.getCurrentAmount()));
                currentPos.setCurrentAmount(0);
                productPositionRepo.save(currentPos);
            }
        }
    }

    private void checkOrderDataActuality(CreateOrderDTO dto){
        Double clientCost = dto.getOverallCost();
        Double clientDiscount = dto.getDiscount();
        Double clientHighDemandCoeff = dto.getHighDemandCoeff();

        // checking that client data is still actual
        Double[] repeatedCalculation = countOrderCost(dto.getGeo(), dto.getProductAmountPairs(), dto.getWarehouseId());

        Double countedCost = repeatedCalculation[0];
        Double countedDiscount = repeatedCalculation[1];
        Double countedHighDemandCoeff = repeatedCalculation[2];

        if(!clientCost.equals(countedCost) || !clientHighDemandCoeff.equals(countedHighDemandCoeff) ||
                !clientDiscount.equals(countedDiscount)) throw new OrderCostChangedEx(countedCost,
                countedDiscount, countedHighDemandCoeff);
    }

    public Double countOrderWeight(HashMap<Long, Integer> products){
        return products.entrySet().stream().map(
                        p -> productRepo.findById(p.getKey()).get().getWeight() * p.getValue())
                .reduce( (accum, next) -> accum += next)
                .get().doubleValue();
    }

    @Override
    public OrderInfoDTO getOrderInfo(Order order, User user) {

        if(user.getRole() == Role.MODERATOR){
            if(!order.getWarehouse().getId().equals(user.getModerator().getWarehouseId())) throw new CustomAccessDeniedException();
        } else if(user.getRole() == Role.COURIER){
            if(!order.getCourier().getId().equals(user.getCourier().getId())) throw new CustomAccessDeniedException();
        } else if(user.getRole() == Role.CLIENT){
            if(!order.getClient().getId().equals(user.getClient().getId())) throw new CustomAccessDeniedException();
        }
        return convertToOrderInfoDTO(order);
    }

    @Override
    public void changeOrderStatus(Order order, User user, ChangeOrderStatusDTO dto) {

        if(user.getRole() == Role.MODERATOR){
            if(!order.getWarehouse().getId().equals(user.getModerator().getWarehouseId())) throw new CustomAccessDeniedException();
        } else if(user.getRole() == Role.COURIER){
            if(!order.getCourier().getId().equals(user.getCourier().getId())) throw new CustomAccessDeniedException();
        } else if(user.getRole() == Role.CLIENT){
            if(!order.getClient().getId().equals(user.getClient().getId())) throw new CustomAccessDeniedException();
        }

        OrderStatus oldStatus = order.getStatus();
        OrderStatus newStatus = dto.getNewStatus();
        if(oldStatus == OrderStatus.CANCELLED || oldStatus == OrderStatus.DELIVERED
                || (oldStatus.ordinal() > newStatus.ordinal())) throw new OrderStatusChangeException(order.getId());

        order.setStatus(newStatus);
        orderRepo.save(order);
    }

    @Override
    public void replaceCourier(Order order, User user) {

        if(user.getRole() == Role.MODERATOR){
            if(!order.getWarehouse().getId().equals(user.getModerator().getWarehouseId())) throw new CustomAccessDeniedException();
        }

        if(order.getStatus() == OrderStatus.CANCELLED || order.getStatus() == OrderStatus.DELIVERED) throw new CourierReplaceException();

        Courier currentCourier = order.getCourier();
        if(currentCourier == null) throw new CourierNotSetException();
        Courier newCourier = courierRepo.getWaitingCourierByWarehouse(order.getWarehouse().getId());
        order.setCourier(newCourier);
        orderRepo.save(order);
    }

    @Override
    public void changeDeliveryRating(Order order, ChangeRatingDTO dto, User user) {

        if(order.getCourier() == null) throw new CourierNotSetException();
        if(!user.getId().equals(order.getClient().getId())) throw new CustomAccessDeniedException();

        order.setDeliveryRating(dto.getRating());
        orderRepo.save(order);
    }

    @Override
    public void changeClientRating(Order order, ChangeRatingDTO dto, User user) {

        if(order.getCourier() == null || !user.getId().equals(order.getCourier().getId())) throw new CustomAccessDeniedException();

        order.setClientRating(dto.getRating());
        orderRepo.save(order);
    }

    @Override
    public Order findCouriersActiveOrder(Courier courier) {
        return orderRepo.findCouriersActiveOrder(courier.getId());
    }

    @Override
    public OrdersAmountDTO getOrdersAmount(User user) {
        return new OrdersAmountDTO(orderRepo.getOrdersAmount(user.getId()));
    }

    public OrderInfoDTO convertToOrderInfoDTO(Order order){
        List<OrderProductPosition> orderProductPositions = orderProductPositionRepo.findAllByOrder(order);
        List<OrderInfoDTO.ProductAmountPair> products = new ArrayList<>();
        for(OrderProductPosition orderProductPosition: orderProductPositions){
            ProductDTO p = productMapper.mapToDTO(orderProductPosition.getProductPosition().getProduct());
            Integer amount = orderProductPosition.getAmount();
            products.add(new OrderInfoDTO.ProductAmountPair(p, amount));
        }
        return new OrderInfoDTO(
                order.getId(), order.getClient(), order.getAddress(),
                order.getCoordinates(), order.getWarehouse(),
                order.getCourier(), order.getStatus(), order.getDateStart(),
                order.getDateEnd(), order.getOverallCost(), order.getHighDemandCoeff(),
                order.getDiscount(), order.getPromoCodeId(), order.getClientRating(),
                order.getDeliveryRating(), products);
    }
}
