package com.ncedu.fooddelivery.api.v1.services.impls;

import com.ncedu.fooddelivery.api.v1.dto.warehouseDTOs.WarehouseInfoDTO;
import com.ncedu.fooddelivery.api.v1.entities.Warehouse;
import com.ncedu.fooddelivery.api.v1.errors.notfound.NotFoundEx;
import com.ncedu.fooddelivery.api.v1.repos.WarehouseRepo;
import com.ncedu.fooddelivery.api.v1.services.WarehouseService;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;


@Service
public class WarehouseServiceImpl1 implements WarehouseService {

    @Autowired
    WarehouseRepo warehouseRepo;

    GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

    @Override
    public WarehouseInfoDTO getWarehouseInfoDTOById(Long id) {
        Optional<Warehouse> warehouseOptional = warehouseRepo.findById(id);
        if(warehouseOptional.isPresent()){
            Warehouse warehouse = warehouseOptional.get();
            return new WarehouseInfoDTO(warehouse.getId(), warehouse.getGeo(), warehouse.getDeliveryZone(), warehouse.getAddress(), warehouse.getName(), warehouse.getIsDeactivated());
        }
        return null;
    }

    @Override
    public List<WarehouseInfoDTO> getActiveWarehouses() {
        List<Warehouse> activeWarehouses = warehouseRepo.getActiveWarehouses();
        if(activeWarehouses.size() == 0) return null;
        return activeWarehouses.stream().map(warehouseEntity -> convertToInfoDTO(warehouseEntity)).collect(Collectors.toList());
    }

    @Override
    public WarehouseInfoDTO getNearestWarehouse(BigDecimal lat, BigDecimal lon) {
        Point geo = geometryFactory.createPoint(new Coordinate(lon.doubleValue(), lat.doubleValue()));
        return getNearestWarehouse(geo);
    }

    @Override
    public WarehouseInfoDTO getNearestWarehouse(Point geo) {
        List<WarehouseInfoDTO> activeWarehouses = getActiveWarehouses();
        if(activeWarehouses == null) return null;
        List<WarehouseInfoDTO> availableWarehouses = activeWarehouses.stream().filter(new Predicate<WarehouseInfoDTO>() {
            @Override
            public boolean test(WarehouseInfoDTO warehouseInfoDTO) {
                return warehouseInfoDTO.getDeliveryZone().covers(geo);
            }
        }).collect(Collectors.toList());
        if (availableWarehouses.size() == 0) {
            throw new NotFoundEx(geo.toString());
        }
        availableWarehouses.sort(new Comparator<WarehouseInfoDTO>() {
            @Override
            public int compare(WarehouseInfoDTO o1, WarehouseInfoDTO o2) {
                Double distance1, distance2;
                distance1 = o1.getGeo().distance(geo);
                distance2 = o2.getGeo().distance(geo);
                return (distance1 - distance2) > 0 ? 1 : (distance1 - distance2) < 0 ? -1 : 0;
            }
        });
        return availableWarehouses.get(0);
    }

    @Override
    public Warehouse findById(Long id) {
        Optional<Warehouse> warehouse = warehouseRepo.findById(id);
        if(warehouse.isEmpty()) throw new NotFoundEx(id.toString());
        return warehouse.get();
    }

    public WarehouseInfoDTO convertToInfoDTO(Warehouse warehouse){
        return new WarehouseInfoDTO(warehouse.getId(), warehouse.getGeo(), warehouse.getDeliveryZone(),
                                    warehouse.getAddress(), warehouse.getName(), warehouse.getIsDeactivated());
    }
}
