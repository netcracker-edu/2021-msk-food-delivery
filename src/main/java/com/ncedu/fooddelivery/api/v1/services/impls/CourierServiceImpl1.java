package com.ncedu.fooddelivery.api.v1.services.impls;

import com.ncedu.fooddelivery.api.v1.dto.user.CourierInfoDTO;
import com.ncedu.fooddelivery.api.v1.entities.Courier;
import com.ncedu.fooddelivery.api.v1.entities.User;
import com.ncedu.fooddelivery.api.v1.errors.notfound.NotFoundEx;
import com.ncedu.fooddelivery.api.v1.errors.orderRegistration.CourierAvailabilityEx;
import com.ncedu.fooddelivery.api.v1.repos.CourierRepo;
import com.ncedu.fooddelivery.api.v1.services.CourierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CourierServiceImpl1 implements CourierService {

    @Autowired
    CourierRepo courierRepo;

    public Courier getCourier(Long id){
        Optional<Courier> optionalCourier = courierRepo.findById(id);
        if(optionalCourier.isEmpty()) return null;
        return optionalCourier.get();
    }

    @Override
    public CourierInfoDTO getCourierDTOById(Long id) {
        Courier c = getCourier(id);
        if(c == null) throw new NotFoundEx(id.toString());
        return convertToDTO(c);
    }

    @Override
    public Courier findFreeCourier(Long warehouseId){
        Courier courier = new Courier();
        int i = 0;
        try{
            for( ; i < 15; i++){
                courier = courierRepo.getWaitingCourierByWarehouse(warehouseId);
                if(courier != null) break;
                Thread.sleep(2000);
            }
        } catch (InterruptedException ex){}
        if(i == 15) throw new CourierAvailabilityEx();
        return courier;
    }

    private CourierInfoDTO convertToDTO(Courier c){
        return new CourierInfoDTO(c.getId(), c.getUser().getRole().toString(), c.getUser().getFullName(),
                c.getUser().getEmail(), c.getUser().getLastSigninDate(), c.getUser().getAvatarId(),
                c.getPhoneNumber(), c.getRating(), c.getWarehouse().getId(), c.getAddress(), c.getCurrentBalance());
    }
}
