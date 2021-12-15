package com.ncedu.fooddelivery.api.v1.entities;

import com.bedatadriven.jackson.datatype.jts.serialization.GeometryDeserializer;
import com.bedatadriven.jackson.datatype.jts.serialization.GeometrySerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Data
@Entity
@Component
@Table(name = "warehouses")
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "warehouse_id")
    private Long id;

    @JsonSerialize(using = GeometrySerializer.class)
    @JsonDeserialize(contentUsing = GeometryDeserializer.class)
    @Column(name = "geo", columnDefinition = "geometry(POINT)")
    private Point geo;

    @JsonSerialize(using = GeometrySerializer.class)
    @JsonDeserialize(contentUsing = GeometryDeserializer.class)
    @Column(name = "delivery_zone", columnDefinition = "geometry")
    private Geometry deliveryZone;

    @Column(name = "address")
    private String address;

    @Column(name = "name")
    private String name;

    @Column(name = "is_deactivated")
    private Boolean isDeactivated;

    public Warehouse(){}

    public Warehouse(Long id, Point geo, Geometry deliveryZone, String address, String name, Boolean isDeactivated) {
        this.id = id;
        this.geo = geo;
        this.deliveryZone = deliveryZone;
        this.address = address;
        this.name = name;
        this.isDeactivated = isDeactivated;
    }
}
