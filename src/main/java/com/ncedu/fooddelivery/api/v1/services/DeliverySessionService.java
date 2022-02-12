package com.ncedu.fooddelivery.api.v1.services;

import com.ncedu.fooddelivery.api.v1.dto.deliverySession.DeliverySessionInfoDTO;
import com.ncedu.fooddelivery.api.v1.dto.isCreatedDTO;
import com.ncedu.fooddelivery.api.v1.entities.Courier;
import com.ncedu.fooddelivery.api.v1.entities.DeliverySession;
import com.ncedu.fooddelivery.api.v1.entities.User;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DeliverySessionService {
    DeliverySessionInfoDTO getInfo(User user, DeliverySession deliverySession);
    isCreatedDTO startSession(User user);
    void finishSession(User user);
    void finishSession(User user, Courier courier);
    void finishSession(User user, DeliverySession deliverySession);
    List<DeliverySessionInfoDTO> getSessionsHistory(User user, Pageable pageable);
}
