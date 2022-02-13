package com.ncedu.fooddelivery.api.v1.services;

import com.ncedu.fooddelivery.api.v1.dto.deliverySession.DeliverySessionInfoDTO;
import com.ncedu.fooddelivery.api.v1.entities.*;
import com.ncedu.fooddelivery.api.v1.entities.order.Order;
import com.ncedu.fooddelivery.api.v1.errors.badrequest.DeliverySessionFinishException;
import com.ncedu.fooddelivery.api.v1.errors.badrequest.DeliverySessionStartException;
import com.ncedu.fooddelivery.api.v1.errors.badrequest.IncorrectUserRoleRequestException;
import com.ncedu.fooddelivery.api.v1.errors.badrequest.NoActiveDeliverySessionException;
import com.ncedu.fooddelivery.api.v1.errors.security.CustomAccessDeniedException;
import com.ncedu.fooddelivery.api.v1.repos.DeliverySessionRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DeliverySessionServiceTest {

    @MockBean
    OrderService orderService;

    @MockBean
    DeliverySessionRepo deliverySessionRepo;

    @Autowired
    DeliverySessionService deliverySessionService;

    private final User targetUser = Mockito.mock(User.class);
    private final User user = Mockito.mock(User.class);
    private final Courier targetCourier = Mockito.mock(Courier.class);
    private final Moderator moderator = Mockito.mock(Moderator.class);
    private final Warehouse warehouse = Mockito.mock(Warehouse.class);
    private final Long userId = 20L;
    private final LocalDateTime currDateTime = LocalDateTime.now();
    private final DeliverySession deliverySession = new DeliverySession(userId, targetCourier, currDateTime,
            currDateTime.plusHours(1L), 1, Duration.ofHours(1L), 1000.0F);

    @BeforeAll
    public void setup(){
        Mockito.when(targetUser.getCourier()).thenReturn(targetCourier);
        Mockito.when(targetUser.getId()).thenReturn(userId);
        Mockito.when(targetUser.getRole()).thenReturn(Role.COURIER);
        Mockito.when(targetCourier.getId()).thenReturn(userId);
        Mockito.when(deliverySessionRepo.save(Mockito.any(DeliverySession.class))).thenReturn(deliverySession);
        Mockito.when(targetCourier.getWarehouse()).thenReturn(warehouse);
        deliverySession.setId(1L);
    }

    @Test
    public void courierStartSessionSuccessTest(){
        Mockito.when(deliverySessionRepo.getActiveSession(Mockito.any(Long.class))).thenReturn(null);
        Assertions.assertEquals(1L, deliverySessionService.startSession(targetUser).getId());
        Mockito.verify(deliverySessionRepo, Mockito.times(1)).save(Mockito.argThat(new ArgumentMatcher<DeliverySession>() {
            @Override
            public boolean matches(DeliverySession deliverySession) {
                return deliverySession.getCourier() == targetCourier;
            }
        }));
    }

    @Test
    public void courierStartSessionFailTest(){
        Mockito.when(deliverySessionRepo.getActiveSession(Mockito.any(Long.class))).thenReturn(deliverySession);

        Assertions.assertThrows(DeliverySessionStartException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                deliverySessionService.startSession(targetUser);
            }
        });

        Mockito.verify(deliverySessionRepo, Mockito.never()).save(Mockito.any(DeliverySession.class));
    }

    @Test
    public void getInfoAccessFailTest(){
        DeliverySession spyDeliverySession = Mockito.spy(deliverySession);

        Mockito.when(user.getRole()).thenReturn(Role.MODERATOR);
        Mockito.when(user.getModerator()).thenReturn(moderator);
        Mockito.when(moderator.getWarehouseId()).thenReturn(2L);
        Mockito.when(warehouse.getId()).thenReturn(1L);

        Assertions.assertThrows(CustomAccessDeniedException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                deliverySessionService.getInfo(user, spyDeliverySession);
            }
        });

        Mockito.verify(user, Mockito.times(1)).getModerator();
        Mockito.verify(moderator, Mockito.times(1)).getWarehouseId();
        Mockito.verify(spyDeliverySession, Mockito.times(1)).getCourier();
        Mockito.verify(spyDeliverySession.getCourier(), Mockito.times(1)).getWarehouse();
        Mockito.verify(spyDeliverySession.getCourier().getWarehouse(), Mockito.times(1)).getId();
    }

    @Test
    public void finishSessionSuccessTest(){
        DeliverySession spyDeliverySession = Mockito.spy(deliverySession);
        spyDeliverySession.setEndTime(null);

        Mockito.when(deliverySessionRepo.getActiveSession(targetUser.getId())).thenReturn(spyDeliverySession);

        deliverySessionService.finishSession(targetUser);
        Mockito.verify(deliverySessionRepo, Mockito.times(1)).save(Mockito.argThat(new ArgumentMatcher<DeliverySession>() {
            @Override
            public boolean matches(DeliverySession deliverySession) {
                return deliverySession == spyDeliverySession && deliverySession.getEndTime() != null;
            }
        }));

        Mockito.verify(deliverySessionRepo, Mockito.times(1)).getActiveSession(targetUser.getId());
        Mockito.verify(spyDeliverySession, Mockito.times(1)).setEndTime(Mockito.any(LocalDateTime.class));
        Mockito.verify(deliverySessionRepo, Mockito.times(1)).save(spyDeliverySession);
    }

    @Test
    public void finishSessionNotExistsExceptionTest(){
        Mockito.when(deliverySessionRepo.getActiveSession(targetUser.getId())).thenReturn(null);

        Assertions.assertThrows(NoActiveDeliverySessionException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                deliverySessionService.finishSession(targetUser);
            }
        });

        Mockito.verify(deliverySessionRepo, Mockito.times(1)).getActiveSession(targetUser.getId());
        Mockito.verify(deliverySessionRepo, Mockito.never()).save(Mockito.any(DeliverySession.class));
    }

    @Test
    public void finishSessionActiveOrderExceptionTest(){
        Order order = Mockito.mock(Order.class);

        Mockito.when(order.getId()).thenReturn(1L);
        Mockito.when(deliverySessionRepo.getActiveSession(targetUser.getId())).thenReturn(deliverySession);
        Mockito.when(orderService.findCouriersActiveOrder(targetCourier)).thenReturn(order);

        Assertions.assertThrows(DeliverySessionFinishException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                deliverySessionService.finishSession(targetUser);
            }
        });

        Mockito.verify(deliverySessionRepo, Mockito.times(1)).getActiveSession(targetUser.getId());
        Mockito.verify(orderService, Mockito.times(1)).findCouriersActiveOrder(targetCourier);
    }

    @Test
    public void finishSessionNotCourierExceptionTest(){
        Mockito.when(user.getRole()).thenReturn(Role.MODERATOR);
        Assertions.assertThrows(IncorrectUserRoleRequestException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                deliverySessionService.finishSession(user, user);
            }
        });
    }

    @Test
    public void getSessionsHistoryTest(){
        Mockito.when(deliverySessionRepo.getSessionsByCourierId(Mockito.any(Long.class), Mockito.any(Pageable.class)))
                .thenReturn(getFakePagedSessions());
        Assertions.assertArrayEquals(getFakeSessionInfoDTOs().toArray(),
                deliverySessionService.getSessionsHistory(targetUser, Mockito.mock(Pageable.class)).toArray());
    }

    private Page<DeliverySession> getFakePagedSessions(){
        DeliverySession ds1 = deliverySession;
        DeliverySession ds2 = new DeliverySession(userId, targetCourier, currDateTime.minusHours(3),
                currDateTime.minusHours(2), 2, Duration.ofMinutes(30), 2000.0F);
        ds2.setId(2L);
        DeliverySession ds3 = new DeliverySession(userId, targetCourier, currDateTime.minusDays(1),
                currDateTime.minusDays(1).plusHours(4), 7, Duration.ofSeconds( (int) (14400.0 / 7.0)), 5000.0F);
        ds3.setId(3L);
        return new PageImpl<>(Arrays.asList(ds1, ds2, ds3));
    }

    private List<DeliverySessionInfoDTO> getFakeSessionInfoDTOs(){
        DeliverySessionInfoDTO dto1 = new DeliverySessionInfoDTO(userId, targetCourier, currDateTime,
                currDateTime.plusHours(1L), 1, "01:00:00", 1000.0F);
        dto1.setId(1L);

        DeliverySessionInfoDTO dto2 = new DeliverySessionInfoDTO(userId, targetCourier, currDateTime.minusHours(3),
                currDateTime.minusHours(2), 2, "00:30:00", 2000.0F);
        dto2.setId(2L);

        DeliverySessionInfoDTO dto3 = new DeliverySessionInfoDTO(userId, targetCourier, currDateTime.minusDays(1),
                currDateTime.minusDays(1).plusHours(4), 7, "00:34:17", 5000.0F);
        dto3.setId(3L);
        return new ArrayList<>(Arrays.asList(dto1, dto2, dto3));
    }
}
