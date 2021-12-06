package com.ncedu.fooddelivery.api.v1.services;

import com.ncedu.fooddelivery.api.v1.dto.user.ModeratorInfoDTO;
import com.ncedu.fooddelivery.api.v1.entities.Moderator;
import com.ncedu.fooddelivery.api.v1.entities.Role;
import com.ncedu.fooddelivery.api.v1.entities.User;
import com.ncedu.fooddelivery.api.v1.errors.notfound.NotFoundEx;
import com.ncedu.fooddelivery.api.v1.repos.ModeratorRepo;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ModeratorServiceTest {

    @MockBean
    ModeratorRepo moderatorRepoMock;

    @Autowired
    ModeratorService moderatorService;

    @Test
    public void getModeratorByIdSuccess() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setFullName("Howard Joel Wolowitz");
        user.setRole(Role.MODERATOR);
        Moderator moderator = new Moderator();
        moderator.setUser(user);
        moderator.setWarehouseId(1L);
        when(moderatorRepoMock.findById(userId)).thenReturn(Optional.of(moderator));

        Moderator result = moderatorService.getModeratorById(userId);

        verify(moderatorRepoMock, times(1)).findById(userId);
        assertEquals(moderator, result);
    }

    @Test
    public void getModeratorByIdNotFoundEx() {
        Long userId = 0L;
        when(moderatorRepoMock.findById(userId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NotFoundEx.class, () -> {
           moderatorService.getModeratorById(userId);
        });
        String resultMessage = exception.getMessage();
        String perfectMessage = new NotFoundEx(userId.toString()).getMessage();

        verify(moderatorRepoMock, times(1)).findById(userId);
        assertEquals(perfectMessage, resultMessage);
    }

    @Test
    public void getModeratorDTObyIDSuccess() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setFullName("Howard Joel Wolowitz");
        user.setRole(Role.MODERATOR);
        Moderator moderator = new Moderator();
        moderator.setUser(user);
        moderator.setWarehouseId(1L);

        when(moderatorRepoMock.findById(userId))
                .thenReturn(Optional.of(moderator));

        ModeratorInfoDTO resultModeratorDTO = moderatorService.getModeratorDTOById(userId);
        ModeratorInfoDTO perfectModeratorDTO = createModeratorDTO(moderator);

        verify(moderatorRepoMock, times(1)).findById(userId);
        assertEquals(perfectModeratorDTO, resultModeratorDTO);
    }

    @Test
    public  void getModeratorDTObyIDNotFoundEx() {
        Long userId = 0L;
        when(moderatorRepoMock.findById(userId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NotFoundEx.class, () -> {
            moderatorService.getModeratorById(userId);
        });
        String resultMessage = exception.getMessage();
        String perfectMessage = new NotFoundEx(userId.toString()).getMessage();

        verify(moderatorRepoMock, times(1)).findById(userId);
        assertEquals(perfectMessage, resultMessage);
    }

    private ModeratorInfoDTO createModeratorDTO(Moderator moderator) {
        User user = moderator.getUser();
        return new ModeratorInfoDTO(user.getId(), user.getRole().name(),
                user.getFullName(), user.getEmail(),
                user.getLastSigninDate(), user.getAvatarId(),
                moderator.getWarehouseId());
    }
}
