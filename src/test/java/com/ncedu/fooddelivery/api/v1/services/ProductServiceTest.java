package com.ncedu.fooddelivery.api.v1.services;

import com.ncedu.fooddelivery.api.v1.dto.product.ProductDTO;
import com.ncedu.fooddelivery.api.v1.entities.Product;
import com.ncedu.fooddelivery.api.v1.entities.User;
import com.ncedu.fooddelivery.api.v1.errors.notfound.NotFoundEx;
import com.ncedu.fooddelivery.api.v1.repos.ProductRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ProductServiceTest {

    @MockBean
    ProductRepo productRepoMock;

    @Autowired
    ProductService productService;

    @Test
    public void getProductByIdSuccess() {
        Long productId = 1L;
        Product product = ProductUtils.createMilkInShowcase(productId);
        when(productRepoMock.findById(productId))
                .thenReturn(Optional.of(product));

        Product result = productService.getProductById(productId);

        verify(productRepoMock, times(1)).findById(productId);
        assertEquals(product, result);
    }

    @Test
    public void getProductByIdNotFoundEx() {
        Long productId = 0L;
        when(productRepoMock.findById(productId))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(NotFoundEx.class, () -> {
            productService.getProductById(productId);
        });

        String resultMessage = exception.getMessage();
        String perfectMessage = new NotFoundEx(productId.toString()).getMessage();

        verify(productRepoMock, times(1)).findById(productId);
        assertEquals(perfectMessage, resultMessage);
    }

    @Test
    public void getProductDTOByIDSuccess() {
        Long productId = 1L;
        Product product = ProductUtils.createMilkInShowcase(productId);
        when(productRepoMock.findById(productId)).thenReturn(Optional.of(product));

        ProductDTO resultDTO = productService.getProductDTOById(productId);
        ProductDTO perfectDTO = createProductDTO(product);

        verify(productRepoMock, times(1)).findById(productId);
        assertEquals(perfectDTO, resultDTO);
    }

    @Test
    public void getProductDTOByIdNotFoundEx() {
        Long productId = 0L;
        when(productRepoMock.findById(productId))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(NotFoundEx.class, () -> {
            productService.getProductDTOById(productId);
        });

        String resultMessage = exception.getMessage();
        String perfectMessage = new NotFoundEx(productId.toString()).getMessage();

        verify(productRepoMock, times(1)).findById(productId);
        assertEquals(perfectMessage, resultMessage);
    }

    @Test
    public void getProductDTOByIdInShowCaseSuccess() {
        Long productId = 1L;
        Product product = ProductUtils.createMilkInShowcase(productId);
        when(productRepoMock.findById(productId))
                .thenReturn(Optional.of(product));

        ProductDTO resultDTO = productService.getProductDTOByIdInShowcase(productId);
        ProductDTO perfectDTO = ProductUtils.createProductDTO(product);

        verify(productRepoMock, times(1)).findById(productId);
        assertEquals(perfectDTO, resultDTO);
    }

    @Test
    public void getProductDTOByIdInShowCaseError() {
        Long productId = 1L;
        Product product = ProductUtils.createMilkNOTinShowcase(productId);
        when(productRepoMock.findById(productId))
                .thenReturn(Optional.of(product));

        Exception exception = assertThrows(NotFoundEx.class, () -> {
            productService.getProductDTOByIdInShowcase(productId);
        });
        String resultMessage = exception.getMessage();
        String perfectMessage = new NotFoundEx(productId.toString()).getMessage();

        verify(productRepoMock, times(1)).findById(productId);
        assertEquals(perfectMessage, resultMessage);
    }

    @Test
    public void getProductsSuccess() {
        Pageable pageable = PageRequest.of(0 , 2);
        Page<Product> productsPage = ProductUtils.createPageWithMilkProducts(pageable);

        when(productRepoMock.findAll(pageable)).thenReturn(productsPage);

        List<ProductDTO> resultList = productService.getProducts(pageable);
        List<ProductDTO> perfectList = ProductUtils.createProductDTOListFromPage(productsPage);

        verify(productRepoMock, times(1)).findAll(pageable);
        assertEquals(perfectList, resultList);
    }

    @Test
    public void getProductsNullResult() {
        Pageable pageable = PageRequest.of(0 , 2);
        Page<Product> productsPage = new PageImpl<>(new ArrayList<Product>(), pageable, 0);
        when(productRepoMock.findAll(pageable)).thenReturn(productsPage);

        List<ProductDTO> resultList = productService.getProducts(pageable);
        System.out.println(resultList);
        List<ProductDTO> perfectList = new ArrayList<>();

        verify(productRepoMock, times(1)).findAll(pageable);
        assertEquals(perfectList, resultList);
    }

    @Test
    public void getProductsInShowcaseSuccess() {
        Pageable pageable = PageRequest.of(0, 2);
        Page<Product> productPage = ProductUtils.createPageProductsInShowcase(pageable);
        when(productRepoMock.findAllByInShowcase(true, pageable)).thenReturn(productPage);

        List<ProductDTO> resultList = productService.getProductsInShowcase(pageable);
        List<ProductDTO> perfectList = ProductUtils.createProductDTOListFromPage(productPage);

        verify(productRepoMock, times(1)).findAllByInShowcase(true, pageable);
        assertEquals(perfectList, resultList);
    }

    @Test
    public void searchProductsSuccess() {
        Pageable pageable = PageRequest.of(0, 2);
        Page<Product> productPage = ProductUtils.createPageWithMilkProducts(pageable);
        String requestSearchPhrase = "Milk taste";
        String perfectPhrase = "Milk:* & taste:*";
        when(productRepoMock.searchProducts(perfectPhrase, pageable)).thenReturn(productPage);

        List<ProductDTO> resultList = productService.searchProducts(requestSearchPhrase, pageable);
        List<ProductDTO> perfectList = ProductUtils.createProductDTOListFromPage(productPage);

        verify(productRepoMock, times(1)).searchProducts(perfectPhrase, pageable);
        assertEquals(resultList, perfectList);
    }

    private ProductDTO createProductDTO(Product p) {
        return new ProductDTO(p.getId(), p.getName(), p.getDescription(),
                p.getPictureUUID(), p.getWeight(), p.getComposition(),
                p.getExpirationDays(), p.getInShowcase(), p.getPrice(), p.getDiscount());
    }
}
