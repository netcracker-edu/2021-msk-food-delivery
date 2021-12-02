package com.ncedu.fooddelivery.api.v1.errors.notfound;

public class ProductPositionNotFoundException extends NotFoundEx{
    private final static String UUID = "894b7113-af86-4487-9e80-a46bf389364f";
    private final static String message = "Product position with such id not found";

    public ProductPositionNotFoundException(Long id) {
        super(String.format("%s: {%d}", message, id), UUID);
    }

}
