package com.ncedu.fooddelivery.api.v1.services;

import com.ncedu.fooddelivery.api.v1.dto.user.ClientInfoDTO;
import com.ncedu.fooddelivery.api.v1.dto.user.UserChangeInfoDTO;
import com.ncedu.fooddelivery.api.v1.entities.Client;
import com.ncedu.fooddelivery.api.v1.entities.Role;
import com.ncedu.fooddelivery.api.v1.entities.User;
import com.ncedu.fooddelivery.api.v1.errors.notfound.NotFoundEx;
import com.ncedu.fooddelivery.api.v1.repos.ClientRepo;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ClientServiceTest {

    @MockBean
    ClientRepo clientRepoMock;

    @Autowired
    ClientService clientService;

    @Test
    public void getClientByIdSuccess() {
        Long userId = 1L;
        Client client = ClientUtils.createLeonardHofstadter(userId);
        when(clientRepoMock.findById(userId)).thenReturn(Optional.of(client));

        Client resultClient = clientService.getClientById(userId);

        verify(clientRepoMock, times(1)).findById(userId);
        assertEquals(client, resultClient);
    }

    @Test
    public void getClientByIdError() {
        Long userId = 0L;
        when(clientRepoMock.findById(userId))
                .thenReturn(Optional.ofNullable(null));

        Exception exception = assertThrows(NotFoundEx.class, () -> {
            clientService.getClientById(userId);
        });
        String perfectMessage = new NotFoundEx(userId.toString()).getMessage();
        String resultMessage = exception.getMessage();

        verify(clientRepoMock, times(1)).findById(userId);
        assertEquals(perfectMessage, resultMessage);
    }

    @Test
    public void getClientDTOByIdSuccess() {
        Long userId = 1L;
        Client client = ClientUtils.createLeonardHofstadter(userId);
        when(clientRepoMock.findById(userId)).thenReturn(Optional.of(client));

        ClientInfoDTO resultClientDTO = clientService.getClientDTOById(userId);
        ClientInfoDTO perfectClientDTO = ClientUtils.createClientInfoDTO(client);

        verify(clientRepoMock, times(1)).findById(userId);
        assertEquals(perfectClientDTO, resultClientDTO);
    }

    @Test
    public void getClientDTOByIdError() {
        Long userId = 0L;
        when(clientRepoMock.findById(userId))
                .thenReturn(Optional.ofNullable(null));

        Exception exception = assertThrows(NotFoundEx.class, () -> {
            clientService.getClientDTOById(userId);
        });

        String resultMessage = exception.getMessage();
        String perfectMessage = new NotFoundEx(userId.toString()).getMessage();

        verify(clientRepoMock, times(1)).findById(userId);
        assertEquals(perfectMessage, resultMessage);
    }

    @Test
    public void changeClientInfoSuccess() {
        Long userID = 1L;
        Client client = ClientUtils.createPennyTeller(userID);

        when(clientRepoMock.findById(userID)).thenReturn(Optional.of(client));
        when(clientRepoMock.save(client)).thenReturn(null);

        String newFullName = "Penny Hofstadter";
        String newPhoneNumber = "+7 (800) 555 35-35";
        UserChangeInfoDTO userChangeInfoDTO = new UserChangeInfoDTO(newFullName, newPhoneNumber);
        boolean result = clientService.changeClientInfo(userID, userChangeInfoDTO);

        verify(clientRepoMock, times(1)).findById(userID);
        verify(clientRepoMock, times(1)).save(client);
        assertEquals(newFullName, client.getUser().getFullName());
        assertEquals(newPhoneNumber, client.getPhoneNumber());
        assertTrue(result);
    }

    @Test
    public void changeClientInfoError() {
        Long userID = 0L;
        Client client = ClientUtils.createPennyTeller(userID);
        when(clientRepoMock.findById(userID))
                .thenReturn(Optional.ofNullable(null));
        when(clientRepoMock.save(client)).thenReturn(null);


        UserChangeInfoDTO userChangeInfoDTO = new UserChangeInfoDTO();
        Exception exception = assertThrows(NotFoundEx.class, () -> {
            clientService.changeClientInfo(userID, userChangeInfoDTO);
        });
        String resultMessage = exception.getMessage();
        String perfectMessage = new NotFoundEx(userID.toString()).getMessage();

        verify(clientRepoMock, times(1)).findById(userID);
        verify(clientRepoMock, never()).save(client);
        assertEquals(perfectMessage, resultMessage);
    }


}
