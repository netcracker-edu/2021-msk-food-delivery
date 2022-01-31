package com.ncedu.fooddelivery.api.v1.services;

import com.ncedu.fooddelivery.api.v1.dto.CoordsDTO;
import com.ncedu.fooddelivery.api.v1.dto.product.ProductDTO;
import com.ncedu.fooddelivery.api.v1.dto.product.SearchProductDTO;
import com.ncedu.fooddelivery.api.v1.dto.warehouseDTOs.WarehouseInfoDTO;
import com.ncedu.fooddelivery.api.v1.entities.Product;
import com.ncedu.fooddelivery.api.v1.entities.User;
import com.ncedu.fooddelivery.api.v1.errors.notfound.NotFoundEx;
import com.ncedu.fooddelivery.api.v1.repos.ProductRepo;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;

import java.math.BigDecimal;
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
    @MockBean
    WarehouseService warehouseServiceMock;

    @Autowired
    ProductService productService;

    GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
    private final double LONGITUDE = 37.632502;
    private final double LATITUDE = 55.809327;

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

    //TODO: rewrite tests!!!

    @Test
    public void getProductsSuccess() {
        Pageable pageable = PageRequest.of(0 , 2);
        Page<Product> productsPage = ProductUtils.createPageWithMilkProducts(pageable);
        Long warehouseId = 4L;
        Point geo = makeGeo();
        WarehouseInfoDTO warehouseInfoDTO = new WarehouseInfoDTO(warehouseId, null,null ,null, null, false);
        when(warehouseServiceMock.getNearestWarehouse(geo)).thenReturn(warehouseInfoDTO);
        when(productRepoMock.findAll(warehouseId,pageable)).thenReturn(productsPage);

        CoordsDTO coordsDTO = makeCoordsDTO();
        List<ProductDTO> resultList = productService.getProducts(coordsDTO, pageable);
        List<ProductDTO> perfectList = ProductUtils.createProductDTOListFromPage(productsPage);

        verify(warehouseServiceMock, times(1)).getNearestWarehouse(geo);
        verify(productRepoMock, times(1)).findAll(warehouseId,pageable);
        assertEquals(perfectList, resultList);
    }

    private Point makeGeo() {
        return geometryFactory.createPoint(new Coordinate(LONGITUDE, LATITUDE));
    }

    private CoordsDTO makeCoordsDTO() {
        return new CoordsDTO(BigDecimal.valueOf(LATITUDE), BigDecimal.valueOf(LONGITUDE));
    }

    @Test
    public void getProductsNullResult() {
        Pageable pageable = PageRequest.of(0 , 2);
        Page<Product> productsPage = new PageImpl<>(new ArrayList<Product>(), pageable, 0);
        Long warehouseId = 4L;
        Point geo = makeGeo();
        WarehouseInfoDTO warehouseInfoDTO = new WarehouseInfoDTO(warehouseId, null,null ,null, null, false);
        when(warehouseServiceMock.getNearestWarehouse(geo)).thenReturn(warehouseInfoDTO);
        when(productRepoMock.findAll(warehouseId,pageable)).thenReturn(productsPage);

        CoordsDTO coordsDTO = makeCoordsDTO();
        List<ProductDTO> resultList = productService.getProducts(coordsDTO, pageable);
        List<ProductDTO> perfectList = new ArrayList<>();

        verify(warehouseServiceMock, times(1)).getNearestWarehouse(geo);
        verify(productRepoMock, times(1)).findAll(warehouseId, pageable);
        assertEquals(perfectList, resultList);
    }

    @Test
    public void getProductsInShowcaseSuccess() {
        Pageable pageable = PageRequest.of(0, 2);
        Page<Product> productPage = ProductUtils.createPageProductsInShowcase(pageable);
        Long warehouseId = 4L;
        Point geo = makeGeo();
        WarehouseInfoDTO warehouseInfoDTO = new WarehouseInfoDTO(warehouseId, null,null ,null, null, false);
        when(warehouseServiceMock.getNearestWarehouse(geo)).thenReturn(warehouseInfoDTO);
        when(productRepoMock.findAllByInShowcase(warehouseId, pageable)).thenReturn(productPage);

        CoordsDTO coordsDTO = makeCoordsDTO();
        List<ProductDTO> resultList = productService.getProductsInShowcase(coordsDTO, pageable);
        List<ProductDTO> perfectList = ProductUtils.createProductDTOListFromPage(productPage);

        verify(warehouseServiceMock, times(1)).getNearestWarehouse(geo);
        verify(productRepoMock, times(1)).findAllByInShowcase(warehouseId, pageable);
        assertEquals(perfectList, resultList);
    }

    @Test
    public void searchProductsSuccess() {
        Pageable pageable = PageRequest.of(0, 2);
        Page<Product> productPage = ProductUtils.createPageWithMilkProducts(pageable);
        String requestSearchPhrase = "Milk taste";
        String perfectPhrase = "Milk:* & taste:*";
        Long warehouseId = 4L;
        Point geo = makeGeo();
        WarehouseInfoDTO warehouseInfoDTO = new WarehouseInfoDTO(warehouseId, null,null ,null, null, false);
        when(warehouseServiceMock.getNearestWarehouse(geo)).thenReturn(warehouseInfoDTO);
        when(productRepoMock.searchProducts(perfectPhrase, warehouseId, pageable)).thenReturn(productPage);

        SearchProductDTO searchDTO = createSearchProductDTO(requestSearchPhrase);
        List<ProductDTO> resultList = productService.searchProducts(searchDTO, pageable);
        List<ProductDTO> perfectList = ProductUtils.createProductDTOListFromPage(productPage);

        verify(warehouseServiceMock, times(1)).getNearestWarehouse(geo);
        verify(productRepoMock, times(1)).searchProducts(perfectPhrase, warehouseId, pageable);
        assertEquals(resultList, perfectList);
    }

    @Test
    public void searchProductInShowcaseSuccess() {
        Pageable pageable = PageRequest.of(0, 2);
        Page<Product> productPage = ProductUtils.createPageProductsInShowcase(pageable);
        String requestSearchPhrase = "Milk taste";
        String perfectPhrase = "Milk:* & taste:*";
        Long warehouseId = 4L;
        Point geo = makeGeo();
        WarehouseInfoDTO warehouseInfoDTO = new WarehouseInfoDTO(warehouseId, null,null ,null, null, false);
        when(warehouseServiceMock.getNearestWarehouse(geo)).thenReturn(warehouseInfoDTO);
        when(productRepoMock.searchProductsInShowcase(perfectPhrase, warehouseId, pageable)).thenReturn(productPage);

        SearchProductDTO searchDTO = createSearchProductDTO(requestSearchPhrase);
        List<ProductDTO> resultList = productService.searchProductsInShowcase(searchDTO, pageable);
        List<ProductDTO> perfectList = ProductUtils.createProductDTOListFromPage(productPage);

        verify(warehouseServiceMock, times(1)).getNearestWarehouse(geo);
        verify(productRepoMock, times(1)).searchProductsInShowcase(perfectPhrase, warehouseId, pageable);
        assertEquals(perfectList, resultList);
    }

    private SearchProductDTO createSearchProductDTO(String requestSearchPhrase) {
        CoordsDTO coordsDTO = makeCoordsDTO();
        return new SearchProductDTO(requestSearchPhrase, coordsDTO);
    }

    private ProductDTO createProductDTO(Product p) {
        return new ProductDTO(p.getId(), p.getName(), p.getDescription(),
                p.getPictureUUID(), p.getWeight(), p.getComposition(),
                p.getExpirationDays(), p.getInShowcase(), p.getPrice(), p.getDiscount());
    }
}
