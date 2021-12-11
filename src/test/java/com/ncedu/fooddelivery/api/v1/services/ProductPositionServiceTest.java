package com.ncedu.fooddelivery.api.v1.services;

import com.ncedu.fooddelivery.api.v1.entities.productPosition.ProductPosition;
import com.ncedu.fooddelivery.api.v1.repos.productPosition.ProductPositionRepo;
import org.junit.jupiter.api.Test;
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
}
