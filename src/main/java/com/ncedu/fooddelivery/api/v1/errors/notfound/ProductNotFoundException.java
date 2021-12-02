package com.ncedu.fooddelivery.api.v1.errors.notfound;

public class ProductNotFoundException extends NotFoundEx{
    private final static String UUID = "04e80bdf-f942-4544-a4e6-aeb1cd20eb96";
    private final static String message = "Product with such id not found";


    public ProductNotFoundException(Long id) {
        super(String.format("%s: {%d}", message, id), UUID);
    }
}
