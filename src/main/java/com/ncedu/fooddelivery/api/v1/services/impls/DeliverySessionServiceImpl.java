package com.ncedu.fooddelivery.api.v1.services.impls;

import com.ncedu.fooddelivery.api.v1.dto.deliverySession.DeliverySessionInfoDTO;
import com.ncedu.fooddelivery.api.v1.dto.isCreatedDTO;
import com.ncedu.fooddelivery.api.v1.dto.user.CourierInfoDTO;
import com.ncedu.fooddelivery.api.v1.entities.*;
import com.ncedu.fooddelivery.api.v1.entities.order.Order;
import com.ncedu.fooddelivery.api.v1.errors.badrequest.*;
import com.ncedu.fooddelivery.api.v1.errors.security.CustomAccessDeniedException;
import com.ncedu.fooddelivery.api.v1.repos.DeliverySessionRepo;
import com.ncedu.fooddelivery.api.v1.services.DeliverySessionService;
import com.ncedu.fooddelivery.api.v1.services.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.time.DurationFormatUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DeliverySessionServiceImpl implements DeliverySessionService {

    @Autowired
    DeliverySessionRepo deliverySessionRepo;

    @Autowired
    OrderService orderService;

    @Override
    public DeliverySessionInfoDTO getInfo(User user, DeliverySession deliverySession) {
        if(user.getRole() == Role.COURIER){
            if(!user.getCourier().getId().equals(deliverySession.getCourier().getId())){
                throw new CustomAccessDeniedException();
            }
        } else if(user.getRole() == Role.MODERATOR) checkModeratorAccess(user.getModerator(),
                deliverySession.getCourier());

        return convertToInfoDto(deliverySession);
    }

    @Override
    public isCreatedDTO startSession(User user) {
        if(hasActiveSession(user)) throw new DeliverySessionAlreadyStartedException();
        DeliverySession newSession = createSession(user);
        log.debug("Courier (id=" + user.getId().toString() + ") started delivery session (id="
                + newSession.getId().toString() + ")");
        return new isCreatedDTO(newSession.getId());
    }

    private DeliverySession createSession(User user){
        DeliverySession session = new DeliverySession();
        session.setCourier(user.getCourier());
        session.setStartTime(LocalDateTime.now());
        return deliverySessionRepo.save(session);
    }

    private boolean hasActiveSession(User user){
        return deliverySessionRepo.getActiveSession(user.getId()) != null;
    }

    @Override
    public void finishSession(User user) {
        DeliverySession currentSession = deliverySessionRepo.getActiveSession(user.getId());
        if(currentSession == null) throw new NoActiveDeliverySessionException();
        Order activeOrder = orderService.findCouriersActiveOrder(user.getCourier());
        if(activeOrder != null) throw new DeliverySessionFinishException(activeOrder.getId());
        currentSession.setEndTime(LocalDateTime.now());
        deliverySessionRepo.save(currentSession);   // db trigger does other work
    }

    @Override
    public void finishSession(User user, User targetUser) {
        if(targetUser.getRole() != Role.COURIER) throw new IncorrectUserRoleRequestException();
        Courier courier = targetUser.getCourier();
        if(user.getRole() == Role.MODERATOR) checkModeratorAccess(user.getModerator(), courier);
        DeliverySession currentSession = deliverySessionRepo.getActiveSession(courier.getId());
        if(currentSession == null) throw new NoActiveDeliverySessionException();
        Order activeOrder = orderService.findCouriersActiveOrder(courier);
        if(activeOrder != null) throw new DeliverySessionFinishException(activeOrder.getId());
        currentSession.setEndTime(LocalDateTime.now());
        deliverySessionRepo.save(currentSession);   // db trigger does other work
    }

    @Override
    public void finishSession(User user, DeliverySession deliverySession) {
        if(user.getRole() == Role.MODERATOR) checkModeratorAccess(user.getModerator(), deliverySession.getCourier());
        if(deliverySession.getEndTime() != null) throw new DeliverySessionAlreadyFinishedException(deliverySession.getId());
        Order activeOrder = orderService.findCouriersActiveOrder(deliverySession.getCourier());
        if(activeOrder != null) throw new DeliverySessionFinishException(activeOrder.getId());
        deliverySession.setEndTime(LocalDateTime.now());
        deliverySessionRepo.save(deliverySession);   // db trigger does other work
    }

    @Override
    public List<DeliverySessionInfoDTO> getSessionsHistory(User user, Pageable pageable) {
        if(user.getRole() != Role.COURIER) throw new IncorrectUserRoleRequestException();
        return deliverySessionRepo.getSessionsByCourierId(user.getId(), pageable).stream()
                .map(session -> convertToInfoDto(session)).collect(Collectors.toList());
    }

    @Override
    public List<DeliverySessionInfoDTO> getSessionsHistory(User user, User targetUser, Pageable pageable) {
        if(targetUser.getRole() != Role.COURIER) throw new IncorrectUserRoleRequestException();
        if(user.getRole() == Role.MODERATOR) checkModeratorAccess(user.getModerator(), targetUser.getCourier());
        return deliverySessionRepo.getSessionsByCourierId(targetUser.getId(), pageable).stream()
                .map(session -> convertToInfoDto(session)).collect(Collectors.toList());
    }

    @Override
    public DeliverySessionInfoDTO getCurrentSessionInfo(User user) {
        DeliverySession current = deliverySessionRepo.getActiveSession(user.getId());
        if(current == null) throw new NoActiveDeliverySessionException();
        return convertToInfoDto(current);
    }

    @Override
    public DeliverySessionInfoDTO getCurrentSessionInfo(User user, User targetUser) {
        if(targetUser.getRole() != Role.COURIER) throw new IncorrectUserRoleRequestException();
        if(user.getRole() == Role.MODERATOR) checkModeratorAccess(user.getModerator(), targetUser.getCourier());
        DeliverySession current = deliverySessionRepo.getActiveSession(targetUser.getId());
        if(current == null) throw new NoActiveDeliverySessionException();
        return convertToInfoDto(current);
    }

    private void checkModeratorAccess(Moderator moderator, Courier courier){
        if(!courier.getWarehouse().getId().equals(moderator.getWarehouseId())){
            throw new CustomAccessDeniedException();
        }
    }

    public DeliverySessionInfoDTO convertToInfoDto(DeliverySession deliverySession){
        Duration d = deliverySession.getAverageTimePerOrder();
        Courier c = deliverySession.getCourier();
        String formattedDuration = null;
        if(d != null) formattedDuration = DurationFormatUtils.formatDuration(deliverySession.getAverageTimePerOrder().toMillis(),
                "HH:mm:ss", true);
        return new DeliverySessionInfoDTO(deliverySession.getId(),
                new CourierInfoDTO(c.getId(), Role.COURIER.name(), c.getUser().getFullName(), c.getUser().getEmail(),
                        c.getUser().getLastSigninDate(), c.getUser().getAvatarId(), c.getPhoneNumber(), c.getRating(),
                        c.getWarehouse().getId(), c.getAddress(), c.getCurrentBalance()),
                deliverySession.getStartTime(), deliverySession.getEndTime(), deliverySession.getOrdersCompleted(),
                formattedDuration, deliverySession.getMoneyEarned());
    }
}
