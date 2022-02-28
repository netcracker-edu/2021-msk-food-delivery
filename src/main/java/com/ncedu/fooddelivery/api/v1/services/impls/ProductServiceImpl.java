package com.ncedu.fooddelivery.api.v1.services.impls;

import com.ncedu.fooddelivery.api.v1.dto.CoordsDTO;
import com.ncedu.fooddelivery.api.v1.dto.isCreatedDTO;
import com.ncedu.fooddelivery.api.v1.dto.product.ProductCreateDTO;
import com.ncedu.fooddelivery.api.v1.dto.product.ProductDTO;
import com.ncedu.fooddelivery.api.v1.dto.product.ProductUpdateDTO;
import com.ncedu.fooddelivery.api.v1.dto.product.SearchProductDTO;
import com.ncedu.fooddelivery.api.v1.dto.warehouseDTOs.WarehouseInfoDTO;
import com.ncedu.fooddelivery.api.v1.entities.File;
import com.ncedu.fooddelivery.api.v1.entities.Product;
import com.ncedu.fooddelivery.api.v1.entities.User;
import com.ncedu.fooddelivery.api.v1.entities.Warehouse;
import com.ncedu.fooddelivery.api.v1.errors.notfound.NotFoundEx;
import com.ncedu.fooddelivery.api.v1.mappers.ProductMapper;
import com.ncedu.fooddelivery.api.v1.repos.ProductRepo;
import com.ncedu.fooddelivery.api.v1.services.FileService;
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

import java.util.*;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepo productRepo;

    @Autowired
    WarehouseService warehouseService;

    @Autowired
    FileService fileService;

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

    @Override
    public ProductDTO addPicture(Product product, String pictureCode) {
        UUID pictureUuid = UUID.fromString(pictureCode);
        File file = fileService.getFile(pictureUuid);
        product.setPictureUUID(file.getId());
        productRepo.save(product);
        return productMapper.mapToDTO(product);
    }

    @Override
    public void deletePicture(Product product, User authedUser) {
        UUID pictureUuid = product.getPictureUUID();
        if (pictureUuid == null) {
            return;
        }
        File productFile = fileService.getFile(pictureUuid);
        fileService.delete(productFile, authedUser);
        product.setPictureUUID(null);
        productRepo.save(product);
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
