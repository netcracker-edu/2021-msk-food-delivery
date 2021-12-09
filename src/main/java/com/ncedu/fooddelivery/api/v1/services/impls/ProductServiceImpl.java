package com.ncedu.fooddelivery.api.v1.services.impls;

import com.ncedu.fooddelivery.api.v1.dto.product.ProductDTO;
import com.ncedu.fooddelivery.api.v1.entities.Product;
import com.ncedu.fooddelivery.api.v1.errors.notfound.NotFoundEx;
import com.ncedu.fooddelivery.api.v1.repos.ProductRepo;
import com.ncedu.fooddelivery.api.v1.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.domain.Sort.Order.desc;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepo productRepo;

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
        return createProductDTO(product);
    }

    @Override
    public ProductDTO getProductDTOByIdInShowcase(Long productId) {
        Product product = getProductById(productId);
        if (product.getInShowcase().equals(false)) {
            throw new NotFoundEx(productId.toString());
        }
        return createProductDTO(product);
    }

    @Override
    public List<ProductDTO> getProducts(Pageable pageable) {
        Iterable<Product> products = productRepo.findAll(pageable);
        Iterator<Product> iterator = products.iterator();
        List<ProductDTO> productsDTO = new ArrayList<>();
        while (iterator.hasNext()) {
            productsDTO.add(createProductDTO(iterator.next()));
        }
        return productsDTO;
    }

    @Override
    public List<ProductDTO> getProductsInShowcase(Pageable pageable) {
        Iterable<Product> products = productRepo.findAllByInShowcase(true, pageable);
        Iterator<Product> iterator = products.iterator();
        List<ProductDTO> productsDTO = new ArrayList<>();
        while (iterator.hasNext()) {
            productsDTO.add(createProductDTO(iterator.next()));
        }
        return productsDTO;
    }

    @Override
    public List<ProductDTO> searchProducts(String phrase, Pageable pageable) {

        String resultPhrase = preparePhraseToSearch(phrase);
        Iterable<Product> products = productRepo.searchProducts(
                                resultPhrase,
                                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
        Iterator<Product> iterator = products.iterator();
        List<ProductDTO> productsDTO = new ArrayList<>();
        while (iterator.hasNext()) {
            productsDTO.add(createProductDTO(iterator.next()));
        }
        return productsDTO;
    }

    @Override
    public List<ProductDTO> searchProductsInShowcase(String phrase, Pageable pageable) {
        String resultPhrase = preparePhraseToSearch(phrase);
        Iterable<Product> products = productRepo.searchProductsInShowcase(
                                resultPhrase,
                                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
        Iterator<Product> iterator = products.iterator();
        List<ProductDTO> productsDTO = new ArrayList<>();
        while (iterator.hasNext()) {
            productsDTO.add(createProductDTO(iterator.next()));
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

    private ProductDTO createProductDTO(Product p) {
        return new ProductDTO(p.getId(), p.getName(), p.getDescription(),
                p.getPictureUUID(), p.getWeight(), p.getComposition(),
                p.getExpirationDays(), p.getInShowcase(), p.getPrice(), p.getDiscount());
    }
}
