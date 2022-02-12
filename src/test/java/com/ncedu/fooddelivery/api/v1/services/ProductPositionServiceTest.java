package com.ncedu.fooddelivery.api.v1.services;

import com.ncedu.fooddelivery.api.v1.dto.ProductPositionDTOs.ProductPositionsShipmentDTO;
import com.ncedu.fooddelivery.api.v1.entities.Warehouse;
import com.ncedu.fooddelivery.api.v1.entities.order.Order;
import com.ncedu.fooddelivery.api.v1.entities.productPosition.ProductPosition;
import com.ncedu.fooddelivery.api.v1.errors.badrequest.NotUniqueIdException;
import com.ncedu.fooddelivery.api.v1.errors.badrequest.ProductPositionNotEnoughException;
import com.ncedu.fooddelivery.api.v1.repos.order.OrderRepo;
import com.ncedu.fooddelivery.api.v1.repos.productPosition.ProductPositionRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@SpringBootTest
public class ProductPositionServiceTest {

    @MockBean
    ProductPositionRepo productPositionRepo;

    @MockBean
    OrderRepo orderRepo;

    @Autowired
    ProductPositionService productPositionService;



    @Test
    public void getByIdReturnNullTest(){
        when(productPositionRepo.findById(any(Long.class))).thenReturn(Optional.empty());

        assertNull(productPositionService.getProductPosition(100L));
    }

    @Test
    public void deleteProductPosition(){
        ProductPosition mockProductPosition = new ProductPosition();
        when(productPositionRepo.findById(any(Long.class))).thenReturn(Optional.of(mockProductPosition));
        boolean res = productPositionService.deleteProductPosition(100L);

        Mockito.verify(productPositionRepo, times(1)).findById(any(Long.class));
        Mockito.verify(productPositionRepo, times(1)).delete(mockProductPosition);
        assertTrue(res);
    }

    @Test
    public void updatePaymentStatusNotNullTest(){
        List<Long> mockIds = new ArrayList<>(Arrays.asList(1L, 2L, 3L));
        ProductPosition mockProductPosition = Mockito.mock(ProductPosition.class);

        when(productPositionRepo.findById(any(Long.class))).thenReturn(Optional.of(mockProductPosition));

        boolean res = productPositionService.updatePaymentStatus(mockIds);

        verify(productPositionRepo, times(3)).findById(any(Long.class));
        verify(mockProductPosition, times(3)).setIsInvoicePaid(true);
        verify(productPositionRepo, times(3)).save(mockProductPosition);
        assertTrue(res);
    }

    @Test
    public void updatePaymentStatusNullTest(){
        List<Long> mockIds = new ArrayList<>(Arrays.asList(1L, null, 3L));
        ProductPosition mockProductPosition = Mockito.mock(ProductPosition.class);

        when(productPositionRepo.findById(any(Long.class))).thenReturn(Optional.of(mockProductPosition));

        boolean res = productPositionService.updatePaymentStatus(mockIds);

        verify(productPositionRepo, times(1)).findById(any(Long.class));
        verify(mockProductPosition, times(0)).setIsInvoicePaid(true);
        verify(productPositionRepo, times(0)).save(mockProductPosition);
        assertFalse(res);
    }

    @Test
    public void shipProductPositionsNotUniqueTest(){
        ProductPositionsShipmentDTO mockDTO = Mockito.mock(ProductPositionsShipmentDTO.class);
        List<ProductPositionsShipmentDTO.ProductPositionAmountPair> mockPairs;
        ProductPositionsShipmentDTO.ProductPositionAmountPair mockPair1 = new ProductPositionsShipmentDTO.ProductPositionAmountPair(),
                mockPair2 = new ProductPositionsShipmentDTO.ProductPositionAmountPair();
        mockPair1.setId(12L);
        mockPair1.setAmount(10);
        mockPair2.setId(13L);
        mockPair2.setAmount(20);
        mockPairs = new ArrayList<>(Arrays.asList(mockPair1, mockPair2, mockPair1));

        Long mockOrderId = 110L;
        Order mockOrder = new Order();
        Warehouse mockWarehouse = Mockito.mock(Warehouse.class);
        mockOrder.setWarehouse(mockWarehouse);

        Mockito.when(mockWarehouse.getId()).thenReturn(1L);

        Mockito.when(mockDTO.getPositionAmountPairs()).thenReturn(mockPairs);

        Mockito.when(orderRepo.findById(Mockito.any(Long.class))).thenReturn(Optional.of(mockOrder));

        assertThrows(NotUniqueIdException.class, new Executable() {
            @Override
            public void execute() {
                productPositionService.shipProductPositions(mockOrderId, mockDTO);
            }
        });

        Mockito.verify(orderRepo, times(1)).findById(110L);
        Mockito.verify(mockDTO, times(1)).getPositionAmountPairs();
    }

    @Test
    public void shipProductPositionsNotEnoughTest(){
        Long mockOrderId = 110L;
        Order mockOrder = Mockito.mock(Order.class);
        ProductPositionsShipmentDTO mockDTO = Mockito.mock(ProductPositionsShipmentDTO.class);
        ProductPositionsShipmentDTO.ProductPositionAmountPair mockPair = new ProductPositionsShipmentDTO.ProductPositionAmountPair();
        mockPair.setId(10L);
        mockPair.setAmount(1000);
        ProductPosition mockPosition = Mockito.mock(ProductPosition.class);
        mockPosition.setCurrentAmount(1);
        Warehouse mockWarehouse = Mockito.mock(Warehouse.class);

        Mockito.when(mockWarehouse.getId()).thenReturn(1L);
        Mockito.when(mockOrder.getWarehouse()).thenReturn(mockWarehouse);
        Mockito.when(mockDTO.getPositionAmountPairs()).thenReturn(new ArrayList<>(Arrays.asList(mockPair)));
        Mockito.when(orderRepo.findById(Mockito.any(Long.class))).thenReturn(Optional.of(mockOrder));
        Mockito.when(productPositionRepo.findById(Mockito.any(Long.class))).thenReturn(Optional.of(mockPosition));
        Mockito.when(mockPosition.getWarehouse()).thenReturn(mockWarehouse);

        assertThrows(ProductPositionNotEnoughException.class, new Executable() {
            @Override
            public void execute() {
                productPositionService.shipProductPositions(mockOrderId, mockDTO);
            }
        });

        verify(mockDTO, times(1)).getPositionAmountPairs();
        verify(orderRepo, times(1)).findById(110L);
        verify(productPositionRepo, times(2)).findById(10L);
    }


}
