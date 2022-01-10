package com.ncedu.fooddelivery.api.v1.dto.warehouseDTOs;

import com.bedatadriven.jackson.datatype.jts.serialization.GeometrySerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import lombok.Data;

@Data
public class WarehouseInfoDTO {
    private Long id;

    @JsonSerialize(using = GeometrySerializer.class)
    private Point geo;

    @JsonSerialize(using = GeometrySerializer.class)
    private Geometry deliveryZone;

    private String address;
    private String name;
    private Boolean isDeactivated;

    public WarehouseInfoDTO(Long id, Point geo, Geometry deliveryZone, String address, String name, Boolean isDeactivated) {
        this.id = id;
        this.geo = geo;
        this.deliveryZone = deliveryZone;
        this.address = address;
        this.name = name;
        this.isDeactivated = isDeactivated;
    }

    @Override
    public String toString() {
        return "WarehouseInfoDTO{" +
                "id=" + id +
                ", geo=" + geo +
                ", deliveryZone=" + deliveryZone +
                ", address='" + address + '\'' +
                ", name='" + name + '\'' +
                ", isDeactivated=" + isDeactivated +
                '}';
    }
}
