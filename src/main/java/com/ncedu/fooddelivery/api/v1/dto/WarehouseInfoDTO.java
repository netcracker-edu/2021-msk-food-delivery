package com.ncedu.fooddelivery.api.v1.dto;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import lombok.Data;

@Data
public class WarehouseInfoDTO {
    private Long id;
    private Point geo;
    private Geometry delivery_zone;
    private String address;
    private String name;
    private Boolean is_deactivated;

    public WarehouseInfoDTO(Long id, Point geo, Geometry delivery_zone, String address, String name, Boolean is_deactivated) {
        this.id = id;
        this.geo = geo;
        this.delivery_zone = delivery_zone;
        this.address = address;
        this.name = name;
        this.is_deactivated = is_deactivated;
    }
}
