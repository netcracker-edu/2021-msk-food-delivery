package com.ncedu.fooddelivery.api.v1.services;

import com.ncedu.fooddelivery.api.v1.entities.Warehouse;
import com.ncedu.fooddelivery.api.v1.repos.WarehouseRepo;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class WarehouseServiceTest {

    @MockBean
    WarehouseRepo warehouseRepo;

    @Autowired
    WarehouseService warehouseService;

    @Test
    public void getByIdNullTest(){
        Mockito.when(warehouseRepo.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());

        Assertions.assertNull(warehouseService.getWarehouseInfoDTOById(1L));

        Mockito.verify(warehouseRepo, Mockito.times(1)).findById(1L);
    }

    @Test
    public void getNearestWarehouseTest(){
        List<Warehouse> mockWarehouses = getMockActiveWarehouses();
        Mockito.when(warehouseRepo.getActiveWarehouses()).thenReturn(mockWarehouses);
        Point mockInputPoint = Mockito.mock(Point.class);

        Assertions.assertEquals(2L, warehouseService.getNearestWarehouse(mockInputPoint).getId());

        Mockito.verify(mockWarehouses.get(0).getDeliveryZone(), Mockito.times(1)).covers(mockInputPoint);
        Mockito.verify(mockWarehouses.get(1).getDeliveryZone(), Mockito.times(1)).covers(mockInputPoint);
        Mockito.verify(mockWarehouses.get(2).getDeliveryZone(), Mockito.times(1)).covers(mockInputPoint);
        Mockito.verify(mockWarehouses.get(3).getDeliveryZone(), Mockito.times(1)).covers(mockInputPoint);

        Mockito.verify(mockWarehouses.get(3).getGeo(), Mockito.never()).distance(mockInputPoint);


    }

    private List<Warehouse> getMockActiveWarehouses(){
        List<Warehouse> mockWarehouses = new ArrayList<>();
        Point p1 = Mockito.mock(Point.class),
        p2 = Mockito.mock(Point.class),
        p3 = Mockito.mock(Point.class),
        p4 = Mockito.mock(Point.class);

        // setting fake distance between each warehouse and client
        Mockito.when(p1.distance(Mockito.any(Point.class))).thenReturn(10.0);
        Mockito.when(p2.distance(Mockito.any(Point.class))).thenReturn(5.0);
        Mockito.when(p3.distance(Mockito.any(Point.class))).thenReturn(15.0);
        Mockito.when(p4.distance(Mockito.any(Point.class))).thenReturn(1.0);


        Geometry g1 = Mockito.mock(Geometry.class),
        g2 = Mockito.mock(Geometry.class),
        g3 = Mockito.mock(Geometry.class),
        g4 = Mockito.mock(Geometry.class);

        // setting
        Mockito.when(g1.covers(Mockito.any(Point.class))).thenReturn(true);
        Mockito.when(g2.covers(Mockito.any(Point.class))).thenReturn(true);
        Mockito.when(g3.covers(Mockito.any(Point.class))).thenReturn(true);
        Mockito.when(g4.covers(Mockito.any(Point.class))).thenReturn(false);

        Warehouse w1 = new Warehouse(1L, p1, g1, "foo", "bar", true),
                w2 = new Warehouse(2L, p2, g2, "foo", "bar", true),
                w3 = new Warehouse(3L, p3, g3, "foo", "bar", true),
                w4 = new Warehouse(4L, p4, g4, "foo", "bar", true);

        mockWarehouses.addAll(Arrays.asList(w1, w2, w3, w4));

        return mockWarehouses;
    }
}
