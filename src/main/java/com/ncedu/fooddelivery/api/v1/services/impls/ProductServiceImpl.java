package com.ncedu.fooddelivery.api.v1.services.impls;

import com.ncedu.fooddelivery.api.v1.dto.CoordsDTO;
import com.ncedu.fooddelivery.api.v1.dto.CountDTO;
import com.ncedu.fooddelivery.api.v1.dto.isCreatedDTO;
import com.ncedu.fooddelivery.api.v1.dto.product.ProductCreateDTO;
import com.ncedu.fooddelivery.api.v1.dto.product.ProductDTO;
import com.ncedu.fooddelivery.api.v1.dto.product.ProductUpdateDTO;
import com.ncedu.fooddelivery.api.v1.dto.product.SearchProductDTO;
import com.ncedu.fooddelivery.api.v1.dto.warehouseDTOs.WarehouseInfoDTO;
import com.ncedu.fooddelivery.api.v1.entities.Product;
import com.ncedu.fooddelivery.api.v1.entities.Warehouse;
import com.ncedu.fooddelivery.api.v1.errors.notfound.NotFoundEx;
import com.ncedu.fooddelivery.api.v1.mappers.ProductMapper;
import com.ncedu.fooddelivery.api.v1.repos.ProductRepo;
import com.ncedu.fooddelivery.api.v1.services.ProductService;
import com.ncedu.fooddelivery.api.v1.services.WarehouseService;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepo productRepo;

    @Autowired
    WarehouseService warehouseService;

    ProductMapper productMapper = ProductMapper.INSTANCE;

    GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

    @Override
    public Product getProductById(Long productId) {
        Optional<Product> optional = productRepo.findById(productId);
        if (!optional.isPresent()) {
            throw new NotFoundEx(productId.toString());
        }
        return optional.get();
    }

    @Override
    public ProductDTO getProductDTOById(Long productId) {
        Product product = getProductById(productId);
        return productMapper.mapToDTO(product);
    }

    @Override
    public ProductDTO getProductDTOByIdInShowcase(Long productId) {
        Product product = getProductById(productId);
        if (product.getInShowcase().equals(false)) {
            throw new NotFoundEx(productId.toString());
        }
        return productMapper.mapToDTO(product);
    }

    @Override
    public isCreatedDTO createProduct(ProductCreateDTO newProduct) {
        Product product = productMapper.mapToEntity(newProduct);
        product = productRepo.save(product);
        isCreatedDTO createdDTO = new isCreatedDTO();
        createdDTO.setId(product.getId());
        return createdDTO;
    }

    @Override
    public void updateProduct(Long id, ProductUpdateDTO updatedProduct) {
        Product product = getProductById(id);
        productMapper.updateToEntity(product, updatedProduct);
        productRepo.save(product);
    }

    @Override
    public boolean switchInShowcaseStatus(Long id) {
        Product product = getProductById(id);
        boolean newInShowcase = !product.getInShowcase();
        product.setInShowcase(newInShowcase);
        productRepo.save(product);
        return newInShowcase;
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        productRepo.delete(product);
    }

    //TODO: rewrite with extracting List forward from page.
    @Override
    public List<ProductDTO> getProducts(CoordsDTO coordinates, Pageable pageable) {
        Long warehouseId = getWarehouseIdByCoordinates(coordinates);
        Iterable<Product> products = productRepo.findAll(warehouseId, pageable);
        return createProductsDtoFromIterable(products);
    }

    @Override
    public List<ProductDTO> getProductsInShowcase(CoordsDTO coordinates, Pageable pageable) {
        Long warehouseId = getWarehouseIdByCoordinates(coordinates);
        Iterable<Product> products = productRepo.findAllByInShowcase(warehouseId, pageable);
        return createProductsDtoFromIterable(products);
    }

    @Override
    public List<ProductDTO> searchProducts(SearchProductDTO searchDTO, Pageable pageable) {
        Long warehouseId = getWarehouseIdByCoordinates(searchDTO.getGeo());
        String resultPhrase = preparePhraseToSearch(searchDTO.getPhrase());
        Iterable<Product> products = productRepo.searchProducts(resultPhrase, warehouseId, pageable);
        return createProductsDtoFromIterable(products);
    }

    @Override
    public List<ProductDTO> searchProductsInShowcase(SearchProductDTO searchDTO, Pageable pageable) {
        Long warehouseId = getWarehouseIdByCoordinates(searchDTO.getGeo());
        String resultPhrase = preparePhraseToSearch(searchDTO.getPhrase());
        Iterable<Product> products = productRepo.searchProductsInShowcase(resultPhrase, warehouseId,pageable);
        return createProductsDtoFromIterable(products);
    }

    @Override
    public CountDTO getProductsCount(CoordsDTO coordinates) {
        Long warehouseId = getWarehouseIdByCoordinates(coordinates);
        int count = productRepo.findAllCount(warehouseId);
        return new CountDTO(count);
    }

    @Override
    public CountDTO getProductsCountInShowcase(CoordsDTO coordinates) {
        Long warehouseId = getWarehouseIdByCoordinates(coordinates);
        int countInShowcase = productRepo.findAllByInShowcaseCount(warehouseId);
        return new CountDTO(countInShowcase);
    }

    @Override
    public CountDTO searchCountProducts(SearchProductDTO searchDTO) {
        Long warehouseId = getWarehouseIdByCoordinates(searchDTO.getGeo());
        String resultPhrase = preparePhraseToSearch(searchDTO.getPhrase());
        int count = productRepo.searchProductsCount(resultPhrase, warehouseId);
        return new CountDTO(count);
    }

    @Override
    public CountDTO searchProductsCountInShowcase(SearchProductDTO searchDTO) {
        Long warehouseId = getWarehouseIdByCoordinates(searchDTO.getGeo());
        String resultPhrase = preparePhraseToSearch(searchDTO.getPhrase());
        int countInShowcase = productRepo.searchProductsCountInShowcase(resultPhrase, warehouseId);
        return new CountDTO(countInShowcase);
    }

    @Override
    public List<ProductDTO> getProductsByIds(List<Long> productIds) {
        Iterable<Product> products = productRepo.findByIds(productIds);
        return createProductsDtoFromIterable(products);
    }

    private Long getWarehouseIdByCoordinates(CoordsDTO coordinates) {
        Point geo = makeGeoCoordinates(coordinates);
        WarehouseInfoDTO nearestWarehouse = warehouseService.getNearestWarehouse(geo);
        log.debug("WAREHOUSE "+ nearestWarehouse.getId() +" for coords: " + geo);
        return nearestWarehouse.getId();
    }

    private Point makeGeoCoordinates(CoordsDTO coordinates) {
        double lon = coordinates.getLon().doubleValue();
        double lat = coordinates.getLat().doubleValue();
        return geometryFactory.createPoint(new Coordinate(lon, lat));
    }

    private List<ProductDTO> createProductsDtoFromIterable(Iterable<Product> products) {
        Iterator<Product> iterator = products.iterator();
        List<ProductDTO> productsDTO = new ArrayList<>();
        while (iterator.hasNext()) {
            productsDTO.add(productMapper.mapToDTO(iterator.next()));
        }
        return productsDTO;
    }

    private String preparePhraseToSearch(String phrase) {
        String[] splitedPhrase = phrase.split(" ");
        if (splitedPhrase.length == 1) {
            return phrase + ":*";
        }
        for (int i = 0; i < splitedPhrase.length; i++) {
            splitedPhrase[i] += ":*";
        }
        return String.join(" & ", splitedPhrase);
    }
}
