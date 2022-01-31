package com.ncedu.fooddelivery.api.v1.services.impls;

import com.ncedu.fooddelivery.api.v1.dto.IsCreatedDTO;
import com.ncedu.fooddelivery.api.v1.dto.order.*;
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
import com.ncedu.fooddelivery.api.v1.errors.security.CustomAccessDeniedException;
import com.ncedu.fooddelivery.api.v1.repos.CourierRepo;
import com.ncedu.fooddelivery.api.v1.repos.ProductRepo;
import com.ncedu.fooddelivery.api.v1.repos.order.OrderRepo;
import com.ncedu.fooddelivery.api.v1.repos.orderProductPosition.OrderNotHierarchicalProductPositionRepo;
import com.ncedu.fooddelivery.api.v1.repos.orderProductPosition.OrderProductPositionRepo;
import com.ncedu.fooddelivery.api.v1.repos.productPosition.ProductPositionRepo;
import com.ncedu.fooddelivery.api.v1.services.CourierService;
import com.ncedu.fooddelivery.api.v1.services.OrderService;
import com.ncedu.fooddelivery.api.v1.services.UserService;
import com.ncedu.fooddelivery.api.v1.services.WarehouseService;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl1 implements OrderService {

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    OrderNotHierarchicalProductPositionRepo orderNotHierarchicalProductPositionRepo;

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

    @Autowired
    UserService userService;

    GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

    private ReentrantLock mutex = new ReentrantLock();

    @Override
    public Order getOrder(Long id) {
        Optional<Order> optionalOrder = orderRepo.findById(id);
        if(optionalOrder.isEmpty()) return null;
        else return optionalOrder.get();
    }

    @Override
    public List<OrderInfoDTO> findFiltered(Specification<Order> spec, Pageable pageable) {

        return orderRepo.findAll(spec, pageable).stream().map(order -> convertToOrderInfoDTO(order)).collect(Collectors.toList());
    }

    @Override
    public List<OrderInfoDTO> getOrdersHistory(User user, Pageable pageable) {
        if(user.getRole() == Role.CLIENT) return orderRepo.getClientOrdersHistory(user.getId(), pageable).stream().map(order -> convertToOrderInfoDTO(order)).collect(Collectors.toList());
        return orderRepo.getCourierOrdersHistory(user.getId(), pageable).stream().map(order -> convertToOrderInfoDTO(order)).collect(Collectors.toList());
    }

    @Override
    public Double[] countOrderCost(CountOrderCostRequestDTO.Geo geo, List<CountOrderCostRequestDTO.ProductAmountPair> pairs) {
        WarehouseInfoDTO warehouse = warehouseService.getNearestWarehouse(geo.getLat(), geo.getLon());
        if(warehouse == null) throw new NotFoundEx(String.format("{Lat: %s; lon: $s}", geo.getLat().toString(), geo.getLon().toString()));
        Long warehouseId = warehouse.getId();

        List<Long> notFoundProductsIds = new ArrayList<>();
        Map<Long, Integer> productsAvailableAmounts = new HashMap<>();

        Double overallOrderCost = 0.0;
        Double overallOrderDiscount = 0.0;
        Double orderHighDemandCoeff;

        checkIdsUnique(pairs);

        for(CountOrderCostRequestDTO.ProductAmountPair pair: pairs){
            Long productId = pair.getId();
            Integer requestedAmount = pair.getAmount();

            // 'findByProductIdAndWarehouseId' sets lock on every product position in result set. So no one other thread
            // will be able to interact with these positions before whole transaction will complete.
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
        return new Double[]{overallOrderCost, overallOrderDiscount, orderHighDemandCoeff, warehouseId.doubleValue()};
    }

    @Override
    @Transactional
    public CountOrderCostResponseDTO countOrderCost(CountOrderCostRequestDTO dto) {
        WarehouseInfoDTO warehouse = warehouseService.getNearestWarehouse(dto.getGeo().getLat(), dto.getGeo().getLon());
        if(warehouse == null) throw new NotFoundEx(String.format("{Lat: %s; lon: $s}", dto.getGeo().getLat().toString(),
                                                   dto.getGeo().getLon().toString()));
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
        short workingCouriersAmount = 0;
        short deliveringCouriersAmount = 0;
        int i = 0;
        try{
            for( ; i < 15; i++){
                workingCouriersAmount = courierRepo.countWorkingCouriersByWarehouse(warehouseId);
                deliveringCouriersAmount = courierRepo.countDeliveringCouriersByWarehouse(warehouseId);
                if(workingCouriersAmount != deliveringCouriersAmount) break;
                Thread.sleep(2000);
            }
        } catch (InterruptedException ex){}
        if(i == 15) throw new CourierAvailabilityEx();

        int waitingCouriersAmount = workingCouriersAmount - deliveringCouriersAmount;

        double waitingToWorkingProportion = Math.round( ((double) waitingCouriersAmount / (double) workingCouriersAmount) * 100.0) / 100.0;

        if(waitingToWorkingProportion > 0.3) return 1.0;

        Double baseCoeff = 1.0;
        return baseCoeff + 0.05 * (20 - Math.round(waitingToWorkingProportion / 0.015));
    }

    @Override
    @Transactional
    public CreatedOrdersIdDTO createOrder(CreateOrderDTO dto, User user) {
        userService.checkIsUserLocked(user);

        Double clientCost = dto.getOverallCost();
        Double clientDiscount = dto.getDiscount();
        Double clientHighDemandCoeff = dto.getHighDemandCoeff();

        // checking that client data is still actual
        Double[] repeatedCalculation = countOrderCost(dto.getGeo(), dto.getProductAmountPairs());

        Double countedCost = repeatedCalculation[0];
        Double countedDiscount = repeatedCalculation[1];
        Double countedHighDemandCoeff = repeatedCalculation[2];
        Long warehouseId = repeatedCalculation[3].longValue();

        if(!clientCost.equals(countedCost) || !clientHighDemandCoeff.equals(countedHighDemandCoeff) ||
                !clientDiscount.equals(countedDiscount)) throw new OrderCostChangedEx(countedCost,
                                                         countedDiscount, countedHighDemandCoeff);

        Geometry coords = geometryFactory.createPoint(new Coordinate(dto.getGeo().getLon().doubleValue(),
                dto.getGeo().getLat().doubleValue()));

        List<CountOrderCostRequestDTO.ProductAmountPair> pairs = dto.getProductAmountPairs();

        // we will use later Maps below to reserve product positions from warehouse and put them in order
        Map<Long, Double> productPosPriceMap = new HashMap<>();     // productPositionId -> amount * (price - discount)
        Map<Long, Integer> productPosAmountMap = new HashMap<>();   // productPositionId -> amount

        for(CountOrderCostRequestDTO.ProductAmountPair pair: pairs){
            Long productId = pair.getId();
            Product product = productRepo.findById(productId).get();
            Double productCost = product.getPrice() - product.getDiscount();
            Integer requestedAmount = pair.getAmount();
            List<ProductPosition> productPositions = productPositionRepo.findByProductIdAndWarehouseId(productId, warehouseId);

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

            // reserving product positions
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

        Double orderWeight = countOrderWeight(pairs);
        if(orderWeight > 15000d){
            int neededCouriersAmount = (int) Math.ceil(orderWeight / 15000d);
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

            // we've got sorted sequence of product positions, so we can put them into different orders for different
            // couriers

            List<Order> orders = new ArrayList<>();
            for(int i = 0; i < neededCouriersAmount; i++){
                Order order = new Order();
                int weightLimit = 15000;
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
                order.setHighDemandCoeff(countedHighDemandCoeff);
                order.setOverallCost(orderCost);
                order.setClient(user.getClient());
                order.setWarehouse(warehouseService.findById(warehouseId));
                order.setAddress(dto.getAddress());
                order.setCoordinates(coords);
                order.setStatus(OrderStatus.CREATED);
                order.setDateStart(new Timestamp(System.currentTimeMillis()));
                orderRepo.save(order);
                orders.add(order);
                for(Long psId: currOrderPositions.keySet()) {
                    int amount = currOrderPositions.get(psId);
                    ProductPosition ps = productPositionRepo.findById(psId).get();
                    orderProductPositionRepo.save(new OrderProductPosition(order, ps, amount,
                                        (ps.getProduct().getPrice() - ps.getProduct().getDiscount()) * amount));
                }

                // attaching courier and then order status = 'courier_appointed'
                Courier courier = new Courier();
                try{
                    courier = findFreeCourier(warehouseId);
                } catch (CourierAvailabilityEx ex){
                    for(Order o: orders){
                        order.setStatus(OrderStatus.CANCELLED);   // DB trigger will return all reserved product
                                                                  // positions back to warehouse
                        orderRepo.save(order);
                    }
                    throw ex;
                }
                order.setCourier(courier);
                order.setStatus(OrderStatus.COURIER_APPOINTED);
                orderRepo.save(order);
            }

            return new CreatedOrdersIdDTO(orders.stream().map(order -> order.getId()).collect(Collectors.toList()));

        } else {
            Order order = new Order();
            order.setDiscount(countedDiscount);
            order.setHighDemandCoeff(countedHighDemandCoeff);
            order.setOverallCost(countedCost);
            order.setClient(user.getClient());
            order.setWarehouse(warehouseService.findById(warehouseId));
            order.setAddress(dto.getAddress());
            order.setCoordinates(coords);
            order.setStatus(OrderStatus.CREATED);
            order.setDateStart(new Timestamp(System.currentTimeMillis()));
            Long orderId = orderRepo.save(order).getId();

            // adding records in DB table 'orders_product_positions'
            for(Long productPositionId: productPosAmountMap.keySet()){
                orderProductPositionRepo.save(new OrderProductPosition(order,
                        productPositionRepo.findById(productPositionId).get(),
                        productPosAmountMap.get(productPositionId),
                        productPosPriceMap.get(productPositionId)));
            }

            // attaching courier and then order status = 'courier_appointed'
            Courier courier = new Courier();
            try{
                courier = findFreeCourier(warehouseId);
            } catch (CourierAvailabilityEx ex){
                order.setStatus(OrderStatus.CANCELLED);   // DB trigger will return all reserved product positions back
                // to warehouse
                orderRepo.save(order);
                throw ex;
            }
            order.setCourier(courier);
            order.setStatus(OrderStatus.COURIER_APPOINTED);
            return new CreatedOrdersIdDTO(Arrays.asList(orderId));
        }
    }

    public Double countOrderWeight(List<CountOrderCostRequestDTO.ProductAmountPair> products){
        return products.stream().map(
                        p -> productRepo.findById(p.getId()).get().getWeight() * p.getAmount())
                .reduce( (accum, next) -> accum += next)
                .get().doubleValue();
    }

    public Courier findFreeCourier(Long warehouseId){
        Courier courier = new Courier();
        int i = 0;
        try{
            for( ; i < 15; i++){
                courier = courierRepo.getWaitingCourierByWarehouse(warehouseId);
                if(courier != null) break;
                Thread.sleep(2000);
            }
        } catch (InterruptedException ex){}
        if(i == 15) throw new CourierAvailabilityEx();
        return courier;
    }

    @Override
    public OrderInfoDTO getOrderInfo(Long id, User user) {
        userService.checkIsUserLocked(user);
        Optional<Order> orderOptional = orderRepo.findById(id);
        if(orderOptional.isEmpty()) throw new NotFoundEx(id.toString());
        Order order = orderOptional.get();

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
    public void cancelOrder(Long id, User user) {
        userService.checkIsUserLocked(user);
        Optional<Order> orderOptional = orderRepo.findById(id);
        if(orderOptional.isEmpty()) throw new NotFoundEx(id.toString());
        Order order = orderOptional.get();

        if(user.getRole() == Role.MODERATOR){
            if(!order.getWarehouse().getId().equals(user.getModerator().getWarehouseId())) throw new CustomAccessDeniedException();
        } else if(user.getRole() == Role.COURIER){
            if(!order.getCourier().getId().equals(user.getCourier().getId())) throw new CustomAccessDeniedException();
        } else if(user.getRole() == Role.CLIENT){
            if(!order.getClient().getId().equals(user.getClient().getId())) throw new CustomAccessDeniedException();
        }

        if(order.getStatus() == OrderStatus.CANCELLED || order.getStatus() == OrderStatus.DELIVERED) throw new OrderCancellationException(id);

        order.setStatus(OrderStatus.CANCELLED);
        orderRepo.save(order);
    }

    @Override
    public void changeOrderStatus(Long id, User user) {
        userService.checkIsUserLocked(user);
        Optional<Order> orderOptional = orderRepo.findById(id);
        if(orderOptional.isEmpty()) throw new NotFoundEx(id.toString());
        Order order = orderOptional.get();

        if(user.getRole() == Role.MODERATOR){
            if(!order.getWarehouse().getId().equals(user.getModerator().getWarehouseId())) throw new CustomAccessDeniedException();
        } else if(user.getRole() == Role.COURIER){
            if(!order.getCourier().getId().equals(user.getCourier().getId())) throw new CustomAccessDeniedException();
        } else if(user.getRole() == Role.CLIENT){
            if(!order.getClient().getId().equals(user.getClient().getId())) throw new CustomAccessDeniedException();
        }

        if(order.getStatus() == OrderStatus.CANCELLED || order.getStatus() == OrderStatus.DELIVERED) throw new OrderStatusChangeException(id);

        order.setStatus(OrderStatus.values()[order.getStatus().ordinal() + 1]);
        order = orderRepo.save(order);
        Courier courier = order.getCourier();
        if(order.getStatus() == OrderStatus.DELIVERED && courier != null){
            Double overallCost = order.getOverallCost();
            Double income = Math.round(overallCost * 10.0) / 100.0;
            courier.setCurrentBalance(courier.getCurrentBalance() + income.floatValue());
            courierRepo.save(courier);
        }
    }

    @Override
    public void replaceCourier(Long orderId, User user) {
        userService.checkIsUserLocked(user);
        Order order = getOrder(orderId);
        if(order == null) throw new NotFoundEx(orderId.toString());

        if(user.getRole() == Role.MODERATOR){
            if(!order.getWarehouse().getId().equals(user.getModerator().getWarehouseId())) throw new CustomAccessDeniedException();
        }

        if(order.getStatus() == OrderStatus.CANCELLED || order.getStatus() == OrderStatus.DELIVERED) throw new CourierReplaceException();

        Courier currentCourier = order.getCourier();
        if(currentCourier == null) throw new CourierNotSetException();
        Courier newCourier = courierService.getAnotherAvailableCourier(currentCourier.getId(), order.getWarehouse().getId());
        order.setCourier(newCourier);
        orderRepo.save(order);
    }

    @Override
    public void changeDeliveryRating(Long orderId, BigDecimal newRating, User user) {
        userService.checkIsUserLocked(user);
        Order order = getOrder(orderId);

        if(order == null) throw new NotFoundEx(orderId.toString());
        if(order.getCourier() == null) throw new CourierNotSetException();
        if(!user.getId().equals(order.getClient().getId())) throw new CustomAccessDeniedException();

        order.setDeliveryRating(newRating);
        orderRepo.save(order);
    }

    @Override
    public void changeClientRating(Long orderId, BigDecimal newRating, User user) {
        userService.checkIsUserLocked(user);
        Order order = getOrder(orderId);

        if(order == null) throw new NotFoundEx(orderId.toString());
        if(order.getCourier() == null || !user.getId().equals(order.getCourier().getId())) throw new CustomAccessDeniedException();

        order.setClientRating(newRating);
        orderRepo.save(order);
    }

    public void checkIdsUnique(List<CountOrderCostRequestDTO.ProductAmountPair> pairs){
        List<Long> ids = pairs.stream().map(pair -> pair.getId()).collect(Collectors.toList());
        List<Long> uniqueIds = ids.stream().distinct().collect(Collectors.toList());
        if(ids.size() != uniqueIds.size()) throw new NotUniqueIdException();
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
