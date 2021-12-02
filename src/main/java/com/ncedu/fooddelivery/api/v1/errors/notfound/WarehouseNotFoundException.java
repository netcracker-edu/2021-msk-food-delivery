package com.ncedu.fooddelivery.api.v1.errors.notfound;

public class WarehouseNotFoundException extends NotFoundEx {
    private static final String UUID = "311c17fc-6e1f-40e3-8b1c-8316875ec357";
    private static final String message = "Warehouse with such id not found";

    public WarehouseNotFoundException(Long id) {
        super(String.format("%s: {%d}", message, id), UUID);
    }
}
