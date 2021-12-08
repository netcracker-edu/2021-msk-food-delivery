package com.ncedu.fooddelivery.api.v1.services;

import com.ncedu.fooddelivery.api.v1.dto.product.ProductDTO;
import com.ncedu.fooddelivery.api.v1.entities.Product;
import com.ncedu.fooddelivery.api.v1.errors.notfound.NotFoundEx;
import com.ncedu.fooddelivery.api.v1.repos.ProductRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.parameters.P;

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
        //TODO product utils with several product entities and helper methods
        Long productId = 1L;
        Product product = new Product();
        product.setId(productId);
        product.setName("Milk");
        product.setWeight(400);
        product.setPrice(30.4);
        product.setInShowcase(true);
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
        Long productId = 0L;
        Product product = new Product();
        product.setId(productId);
        product.setName("Milk");
        product.setDescription("Awesome taste!!!");
        product.setExpirationDays(Short.valueOf("5"));
        product.setWeight(400);
        product.setPrice(30.4);
        product.setInShowcase(true);
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

    private ProductDTO createProductDTO(Product p) {
        return new ProductDTO(p.getId(), p.getName(), p.getDescription(),
                p.getPictureUUID(), p.getWeight(), p.getComposition(),
                p.getExpirationDays(), p.getPrice(), p.getDiscount());
    }
}
