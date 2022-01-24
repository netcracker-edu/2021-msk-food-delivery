package com.ncedu.fooddelivery.api.v1.schedulers;

import com.ncedu.fooddelivery.api.v1.repos.ClientRepo;
import com.ncedu.fooddelivery.api.v1.repos.CourierRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DatabaseScheduler {
    @Autowired
    ClientRepo clientRepo;
    @Autowired
    CourierRepo courierRepo;

    @Scheduled(cron = "0 0 0 * * *", zone = "Europe/Moscow")    // every midnight
    public void updateCourierAndClientRatings(){
        clientRepo.updateClientRatingNightProcedure();
        courierRepo.updateCourierRatingNightProcedure();
    }
}
