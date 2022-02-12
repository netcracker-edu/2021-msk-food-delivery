package com.ncedu.fooddelivery.api.v1.services;

import com.ncedu.fooddelivery.api.v1.dto.CoordsDTO;
import com.ncedu.fooddelivery.api.v1.dto.order.ChangeOrderStatusDTO;
import com.ncedu.fooddelivery.api.v1.dto.order.ChangeRatingDTO;
import com.ncedu.fooddelivery.api.v1.dto.order.CreateOrderDTO;
import com.ncedu.fooddelivery.api.v1.dto.warehouseDTOs.WarehouseInfoDTO;
import com.ncedu.fooddelivery.api.v1.entities.*;
import com.ncedu.fooddelivery.api.v1.entities.order.Order;
import com.ncedu.fooddelivery.api.v1.entities.orderProductPosition.OrderProductPosition;
import com.ncedu.fooddelivery.api.v1.entities.productPosition.ProductPosition;
import com.ncedu.fooddelivery.api.v1.errors.badrequest.CourierNotSetException;
import com.ncedu.fooddelivery.api.v1.errors.badrequest.CourierReplaceException;
import com.ncedu.fooddelivery.api.v1.errors.orderRegistration.ProductAvailabilityEx;
import com.ncedu.fooddelivery.api.v1.errors.security.CustomAccessDeniedException;
import com.ncedu.fooddelivery.api.v1.repos.CourierRepo;
import com.ncedu.fooddelivery.api.v1.repos.ProductRepo;
import com.ncedu.fooddelivery.api.v1.repos.order.OrderRepo;
import com.ncedu.fooddelivery.api.v1.repos.orderProductPosition.OrderProductPositionRepo;
import com.ncedu.fooddelivery.api.v1.repos.productPosition.ProductPositionRepo;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@SpringBootTest
public class OrderServiceTest {

    @Autowired
    OrderService orderService;

    @MockBean
    OrderRepo orderRepo;

    @MockBean
    CourierRepo courierRepo;

    @MockBean
    WarehouseService warehouseService;

    @MockBean
    ProductPositionRepo productPositionRepo;

    @MockBean
    ProductRepo productRepo;

    @MockBean
    OrderProductPositionRepo orderProductPositionRepo;

    @Autowired
    UserService userService;


    private static final Calendar cal = Calendar.getInstance();
    private static final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

    @Test
    public void getOrderNullTest(){
        Mockito.when(orderRepo.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());
        Assertions.assertNull(orderService.getOrder(12L));
        Mockito.verify(orderRepo, Mockito.times(1)).findById(12L);
    }

    @Test
    public void countOrderCostSuccessfulTest(){
        WarehouseInfoDTO fakeWarehouseInfoDTO = getFakeWarehouseInfoDTO();
        List<Product> fakeProducts = getFakeProducts();
        List<ProductPosition> fakePositions = getFakeProductPositions();
        Mockito.when(courierRepo.countWorkingCouriersByWarehouse(Mockito.any(Long.class))).thenReturn((short) 100);
        Mockito.when(courierRepo.countDeliveringCouriersByWarehouse(Mockito.any(Long.class))).thenReturn((short) 100);
        Mockito.when(warehouseService.getNearestWarehouse(Mockito.any(BigDecimal.class),
                Mockito.any(BigDecimal.class))).thenReturn(fakeWarehouseInfoDTO);

        Mockito.when(productPositionRepo.findByProductIdAndWarehouseIdWithLock(1L, 1L))
                .thenReturn(Arrays.asList(fakePositions.get(0), fakePositions.get(1)));
        Mockito.when(productPositionRepo.findByProductIdAndWarehouseIdWithLock(2L, 1L))
                .thenReturn(Arrays.asList(fakePositions.get(2), fakePositions.get(3)));
        Mockito.when(productPositionRepo.findByProductIdAndWarehouseIdWithLock(3L, 1L))
                .thenReturn(Arrays.asList(fakePositions.get(4), fakePositions.get(5)));

        Mockito.when(productRepo.findById(1l)).thenReturn(Optional.of(fakeProducts.get(0)));
        Mockito.when(productRepo.findById(2l)).thenReturn(Optional.of(fakeProducts.get(1)));
        Mockito.when(productRepo.findById(3l)).thenReturn(Optional.of(fakeProducts.get(2)));

        HashMap<Long, Integer> fakeHashMap = new HashMap<>();
        fakeHashMap.put(1L, 2);
        fakeHashMap.put(2L, 3);
        fakeHashMap.put(3L, 5);

        Double[] res = orderService.countOrderCost(getFakeCoordsDTO(), fakeHashMap,1L);

        Assertions.assertEquals(res[0], 760.0);
        Assertions.assertEquals(res[1], 20.0);
        Assertions.assertEquals(res[2], 2.0);

        Mockito.verify(courierRepo, Mockito.times(1)).countWorkingCouriersByWarehouse(1L);
        Mockito.verify(courierRepo, Mockito.times(1)).countDeliveringCouriersByWarehouse(1L);
        Mockito.verify(productPositionRepo, Mockito.times(3))
                .findByProductIdAndWarehouseIdWithLock(Mockito.any(Long.class), Mockito.eq(1L));
        Mockito.verify(warehouseService, Mockito.times(1))
                .getNearestWarehouse(Mockito.any(BigDecimal.class), Mockito.any(BigDecimal.class));
    }

    @Test
    public void countOrderCostNotAvailableExTest(){
        WarehouseInfoDTO fakeWarehouseInfoDTO = getFakeWarehouseInfoDTO();
        List<Product> fakeProducts = getFakeProducts();
        List<ProductPosition> fakePositions = getFakeProductPositions();
        Mockito.when(courierRepo.countWorkingCouriersByWarehouse(Mockito.any(Long.class))).thenReturn((short) 100);
        Mockito.when(courierRepo.countDeliveringCouriersByWarehouse(Mockito.any(Long.class))).thenReturn((short) 100);
        Mockito.when(warehouseService.getNearestWarehouse(Mockito.any(BigDecimal.class),
                Mockito.any(BigDecimal.class))).thenReturn(fakeWarehouseInfoDTO);

        Mockito.when(productPositionRepo.findByProductIdAndWarehouseIdWithLock(1L, 1L))
                .thenReturn(Arrays.asList(fakePositions.get(0), fakePositions.get(1)));
        Mockito.when(productPositionRepo.findByProductIdAndWarehouseIdWithLock(2L, 1L))
                .thenReturn(Arrays.asList(fakePositions.get(2), fakePositions.get(3)));
        Mockito.when(productPositionRepo.findByProductIdAndWarehouseIdWithLock(3L, 1L))
                .thenReturn(Arrays.asList(fakePositions.get(4), fakePositions.get(5)));

        Mockito.when(productRepo.findById(1l)).thenReturn(Optional.of(fakeProducts.get(0)));
        Mockito.when(productRepo.findById(2l)).thenReturn(Optional.of(fakeProducts.get(1)));
        Mockito.when(productRepo.findById(3l)).thenReturn(Optional.of(fakeProducts.get(2)));

        HashMap<Long, Integer> fakeHashMap = new HashMap<>();
        fakeHashMap.put(1L, 4);
        fakeHashMap.put(2L, 3);
        fakeHashMap.put(3L, 5);

        Assertions.assertThrows(ProductAvailabilityEx.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                orderService.countOrderCost(getFakeCoordsDTO(), fakeHashMap,1L);
            }
        });

        Mockito.verify(productPositionRepo, Mockito.times(3))
                .findByProductIdAndWarehouseIdWithLock(Mockito.any(Long.class), Mockito.eq(1L));
        Mockito.verify(warehouseService, Mockito.times(1))
                .getNearestWarehouse(Mockito.any(BigDecimal.class), Mockito.any(BigDecimal.class));
    }

    @Test
    public void replaceCourierSuccessTest(){
        User fakeUser = getFakeUserModerator();
        Courier fakeCourier = getFakeCourier2();
        Order fakeOrder = getFakeOrder();

        Mockito.when(courierRepo.getWaitingCourierByWarehouse(1L)).thenReturn(fakeCourier);

        orderService.replaceCourier(fakeOrder, fakeUser);
        Assertions.assertEquals(fakeOrder.getCourier(), fakeCourier);

        Mockito.verify(courierRepo, Mockito.times(1)).getWaitingCourierByWarehouse(1L);
    }

    @Test
    public void replaceCourierAccessExTest(){
        User fakeUser = getFakeUserModerator();
        Courier mockCourier = Mockito.mock(Courier.class);
        Order fakeOrder = getFakeOrder();
        fakeOrder.setWarehouse(getFakeWarehouse2());

        Assertions.assertThrows(CustomAccessDeniedException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                orderService.replaceCourier(fakeOrder, fakeUser);
            }
        });
    }

    @Test
    public void replaceCourierReplaceExTest(){
        User fakeUser = getFakeUserModerator();
        Courier mockCourier = Mockito.mock(Courier.class);
        Order fakeOrder = getFakeOrder();
        fakeOrder.setStatus(OrderStatus.DELIVERED);

        Assertions.assertThrows(CourierReplaceException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                orderService.replaceCourier(fakeOrder, fakeUser);
            }
        });
    }

    @Test
    public void createOrderTest(){
        CreateOrderDTO fakeDTO = new CreateOrderDTO();
        User fakeUser = getFakeUserClient();
        HashMap<Long, Integer> fakeHashMap = new HashMap<>();
        fakeHashMap.put(1L, 2);
        fakeHashMap.put(2L, 3);
        fakeHashMap.put(3L, 5);
        fakeDTO.setProductAmountPairs(fakeHashMap);
        fakeDTO.setOverallCost(380.0);
        fakeDTO.setHighDemandCoeff(1.0);
        fakeDTO.setDiscount(20.0);
        fakeDTO.setAddress("address");
        fakeDTO.setGeo(getFakeCoordsDTO());
        fakeDTO.setWarehouseId(1L);

        WarehouseInfoDTO fakeWarehouseInfoDTO = getFakeWarehouseInfoDTO();
        List<Product> fakeProducts = getFakeProducts();
        List<ProductPosition> fakePositions = getFakeProductPositions();


        Mockito.when(courierRepo.countWorkingCouriersByWarehouse(Mockito.any(Long.class))).thenReturn((short) 100);
        Mockito.when(courierRepo.countDeliveringCouriersByWarehouse(Mockito.any(Long.class))).thenReturn((short) 50);

        Mockito.when(warehouseService.getNearestWarehouse(Mockito.any(BigDecimal.class),
                Mockito.any(BigDecimal.class))).thenReturn(fakeWarehouseInfoDTO);
        Mockito.when(warehouseService.findById(1L)).thenReturn(getFakeWarehouse1());

        Mockito.when(productPositionRepo.findByProductIdAndWarehouseIdWithLock(1L, 1L))
                .thenReturn(Arrays.asList(fakePositions.get(0), fakePositions.get(1)));
        Mockito.when(productPositionRepo.findByProductIdAndWarehouseIdWithLock(2L, 1L))
                .thenReturn(Arrays.asList(fakePositions.get(2), fakePositions.get(3)));
        Mockito.when(productPositionRepo.findByProductIdAndWarehouseIdWithLock(3L, 1L))
                .thenReturn(Arrays.asList(fakePositions.get(4), fakePositions.get(5)));
        Mockito.when(productPositionRepo.findById(1L)).thenReturn(Optional.of(fakePositions.get(0)));
        Mockito.when(productPositionRepo.findById(2L)).thenReturn(Optional.of(fakePositions.get(1)));
        Mockito.when(productPositionRepo.findById(3L)).thenReturn(Optional.of(fakePositions.get(2)));
        Mockito.when(productPositionRepo.findById(4L)).thenReturn(Optional.of(fakePositions.get(3)));
        Mockito.when(productPositionRepo.findById(5L)).thenReturn(Optional.of(fakePositions.get(4)));
        Mockito.when(productPositionRepo.findById(6L)).thenReturn(Optional.of(fakePositions.get(5)));


        Mockito.when(productRepo.findById(1l)).thenReturn(Optional.of(fakeProducts.get(0)));
        Mockito.when(productRepo.findById(2l)).thenReturn(Optional.of(fakeProducts.get(1)));
        Mockito.when(productRepo.findById(3l)).thenReturn(Optional.of(fakeProducts.get(2)));

        Mockito.when(orderRepo.save(Mockito.any(Order.class))).thenReturn(getFakeOrder());

        Mockito.when(courierRepo.getWaitingCourierByWarehouse(1L)).thenReturn(Mockito.mock(Courier.class));

        Mockito.when(orderProductPositionRepo.save(Mockito.any(OrderProductPosition.class))).thenReturn(Mockito.mock(OrderProductPosition.class));

        orderService.createOrder(fakeDTO, fakeUser);

        Mockito.verify(courierRepo, Mockito.times(1)).countWorkingCouriersByWarehouse(1L);
        Mockito.verify(courierRepo, Mockito.times(1)).countDeliveringCouriersByWarehouse(1L);
        Mockito.verify(productPositionRepo, Mockito.times(6))
                .findByProductIdAndWarehouseIdWithLock(Mockito.any(Long.class), Mockito.eq(1L));
        Mockito.verify(warehouseService, Mockito.times(1))
                .getNearestWarehouse(Mockito.any(BigDecimal.class), Mockito.any(BigDecimal.class));
        Mockito.verify(orderProductPositionRepo, Mockito.times(3)).save(Mockito.any(OrderProductPosition.class));
    }

    @Test
    public void changeOrderStatusSuccessTest(){
        ChangeOrderStatusDTO dto = new ChangeOrderStatusDTO();
        dto.setNewStatus(OrderStatus.DELIVERED);
        Order fakeOrder = getFakeOrder();

        Mockito.when(orderRepo.save(Mockito.any(Order.class))).thenReturn(fakeOrder);

        orderService.changeOrderStatus(fakeOrder, getFakeUserClient(), dto);
        Assertions.assertTrue(fakeOrder.getStatus() == OrderStatus.DELIVERED);

        Mockito.verify(orderRepo, Mockito.times(1)).save(fakeOrder);
    }

    @Test
    public void changeOrderStatusAccessDeniedExTest(){
        ChangeOrderStatusDTO dto = new ChangeOrderStatusDTO();
        dto.setNewStatus(OrderStatus.DELIVERED);
        Order fakeOrder = getFakeOrder();

        Mockito.when(orderRepo.save(Mockito.any(Order.class))).thenReturn(fakeOrder);

        Assertions.assertThrows(CustomAccessDeniedException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                orderService.changeOrderStatus(fakeOrder, getFakeCourier2().getUser(), dto);
            }
        });
    }

    @Test
    public void changeCourierRatingSuccessTest(){
        User fakeUser = getFakeUserClient();
        ChangeRatingDTO fakeDto = new ChangeRatingDTO();
        fakeDto.setRating(new BigDecimal(4.5));
        Order fakeOrder = getFakeOrder();

        Mockito.when(orderRepo.save(Mockito.any(Order.class))).thenReturn(fakeOrder);

        orderService.changeDeliveryRating(fakeOrder, fakeDto, fakeUser);
        Assertions.assertTrue(fakeOrder.getDeliveryRating().equals(new BigDecimal(4.5)));
        Mockito.verify(orderRepo, Mockito.times(1)).save(fakeOrder);
    }

    @Test
    public void changeCourierRatingCourierNotSetTest(){
        User fakeUser = getFakeUserClient();
        ChangeRatingDTO fakeDto = new ChangeRatingDTO();
        Order fakeOrder = getFakeOrder();
        fakeOrder.setCourier(null);

        Assertions.assertThrows(CourierNotSetException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                orderService.changeDeliveryRating(fakeOrder, fakeDto, fakeUser);
            }
        });
    }

    private List<ProductPosition> getFakeProductPositions(){
        List<Product> fakeProducts = getFakeProducts();
        Warehouse fakeWarehouse = getFakeWarehouse1();

        Date d1 = new Date();
        cal.setTime(d1);
        cal.add(Calendar.DATE, -10);
        d1.setTime(cal.getTime().getTime());

        Date d2 = new Date();
        cal.setTime(d2);
        cal.add(Calendar.DATE, -1);
        d2.setTime(cal.getTime().getTime());


        Date d3 = new Date();
        cal.setTime(d3);
        cal.add(Calendar.DATE, -40);
        d3.setTime(cal.getTime().getTime());

        ProductPosition ps1 = new ProductPosition(1L, fakeProducts.get(0), fakeWarehouse, "AAA", 5, 3, d1,
                new BigDecimal(10000.0), "ИП ИВАНОВ", true, d1);
        ps1.setId(1L);
        ProductPosition ps2 = new ProductPosition(1L, fakeProducts.get(0), fakeWarehouse, "AAA", 5, 3, d2,
                new BigDecimal(10000.0), "ИП ИВАНОВ", true, d2);
        ps2.setId(2L);
        ProductPosition ps3 = new ProductPosition(2L, fakeProducts.get(1), fakeWarehouse, "AAA", 5, 3, d1,
                new BigDecimal(10000.0), "ИП ПЕТРОВ", true, d1);
        ps3.setId(3L);
        ProductPosition ps4 = new ProductPosition(2L, fakeProducts.get(1), fakeWarehouse, "AAA", 10, 8, d2,
                new BigDecimal(10000.0), "ИП ПЕТРОВ", true, d2);
        ps4.setId(4L);
        ProductPosition ps5 = new ProductPosition(3L, fakeProducts.get(2), fakeWarehouse, "AAA", 20, 20, d3,
                new BigDecimal(10000.0), "ИП ФЕДОРОВ", true, d3);
        ps5.setId(5L);
        ProductPosition ps6 = new ProductPosition(3L, fakeProducts.get(2), fakeWarehouse, "AAA", 15, 15, d1,
                new BigDecimal(10000.0), "ИП ФЕДОРОВ", true, d1);
        ps6.setId(6L);
        return new ArrayList<>(Arrays.asList(ps1, ps2, ps3, ps4, ps5, ps6));
    }

    private List<Product> getFakeProducts(){
        Product p1 = new Product(), p2 = new Product(), p3 = new Product();
        p1.setPrice(100.0);
        p1.setDiscount(10.0);
        p1.setExpirationDays((short) 7);
        p1.setWeight(3000);
        p2.setPrice(50.0);
        p2.setDiscount(0.0);
        p2.setExpirationDays((short) 14);
        p2.setWeight(2000);
        p3.setPrice(10.0);
        p3.setDiscount(0.0);
        p3.setExpirationDays((short) 30);
        p3.setWeight(200);
        return new ArrayList<>(Arrays.asList(p1, p2, p3));
    }

    private Warehouse getFakeWarehouse1(){
        return new Warehouse(1L, Mockito.mock(Point.class),
                Mockito.mock(Point.class), "address", "name", false);
    }

    private Warehouse getFakeWarehouse2(){
        return new Warehouse(2L, Mockito.mock(Point.class),
                Mockito.mock(Point.class), "address", "name", false);
    }

    private WarehouseInfoDTO getFakeWarehouseInfoDTO(){
        Warehouse w = getFakeWarehouse1();
        return new WarehouseInfoDTO(w.getId(), w.getGeo(), w.getDeliveryZone(), w.getAddress(), w.getName(),
                w.getIsDeactivated());
    }

    private CoordsDTO getFakeCoordsDTO(){
        return new CoordsDTO(new BigDecimal(1.0), new BigDecimal(2.0));
    }

    private Order getFakeOrder(){
        return new Order(1l, getFakeUserClient().getClient(), "address", geometryFactory.createPoint(new Coordinate(1.0,
                1.0)), getFakeWarehouse1(), getFakeCourier1(), OrderStatus.DELIVERING,
                Mockito.mock(LocalDateTime.class), Mockito.mock(LocalDateTime.class), 1000.0, 1.0, 0.0,
                1l, new BigDecimal(5.0), new BigDecimal(5.0));
    }

    private User getFakeUserModerator(){
        User user = new User();
        user.setRole(Role.MODERATOR);
        Moderator moderator = new Moderator();
        moderator.setWarehouseId(1L);
        moderator.setUser(user);
        user.setLockDate(null);
        user.setModerator(moderator);
        return user;
    }

    private User getFakeUserClient(){
        User user = new User();
        user.setId(1L);
        user.setRole(Role.CLIENT);
        Client client = new Client();
        client.setId(1L);
        client.setUser(user);
        user.setLockDate(null);
        user.setClient(client);
        return user;
    }

    private Courier getFakeCourier1(){
        User user = new User();
        user.setLockDate(null);
        Courier courier = new Courier();
        user.setCourier(courier);
        user.setRole(Role.COURIER);
        courier.setUser(user);
        courier.setId(1L);
        return courier;
    }

    private Courier getFakeCourier2(){
        User user = new User();
        user.setLockDate(null);
        Courier courier = new Courier();
        user.setCourier(courier);
        user.setRole(Role.COURIER);
        courier.setUser(user);
        courier.setId(2L);
        return courier;
    }

}
