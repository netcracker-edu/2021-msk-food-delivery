package com.ncedu.fooddelivery.api.v1.mappers;

import com.ncedu.fooddelivery.api.v1.dto.product.ProductCreateDTO;
import com.ncedu.fooddelivery.api.v1.dto.product.ProductDTO;
import com.ncedu.fooddelivery.api.v1.dto.product.ProductUpdateDTO;
import com.ncedu.fooddelivery.api.v1.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductMapper {

    public ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    public Product mapToEntity(ProductCreateDTO productCreateDTO);
    public ProductDTO mapToDTO(Product product);
    public void updateToEntity(@MappingTarget Product product,  ProductUpdateDTO productUpdateDTO);
}
