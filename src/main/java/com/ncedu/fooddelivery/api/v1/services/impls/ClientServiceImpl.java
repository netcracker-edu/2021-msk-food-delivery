package com.ncedu.fooddelivery.api.v1.services.impls;

import com.ncedu.fooddelivery.api.v1.dto.ClientInfoDTO;
import com.ncedu.fooddelivery.api.v1.dto.UserChangeInfoDTO;
import com.ncedu.fooddelivery.api.v1.entities.Client;
import com.ncedu.fooddelivery.api.v1.entities.User;
import com.ncedu.fooddelivery.api.v1.repos.ClientRepo;
import com.ncedu.fooddelivery.api.v1.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {

    //TODO: throw notFoundEx forward from service

    @Autowired
    ClientRepo clientRepo;

    @Override
    public Client getClientById(Long id) {
        Optional<Client> findedClient = clientRepo.findById(id);
        if (findedClient.isPresent()) {
            return findedClient.get();
        }
        return null;
    }

    @Override
    public ClientInfoDTO getClientDTOById(Long id) {
        Client client = getClientById(id);
        if (client == null) {
            return null;
        }
        return createClientInfoDTO(client);
    }

    private ClientInfoDTO createClientInfoDTO(Client client) {
        User user = client.getUser();
        return  new ClientInfoDTO(user.getId(), user.getRole().name(),
                user.getFullName(), user.getEmail(),
                user.getLastSigninDate(), user.getAvatarId(),
                client.getPhoneNumber(), client.getRating());
    }

    @Override
    public boolean changeClientInfo(Long id, UserChangeInfoDTO newUserInfo) {
        Client client = getClientById(id);
        if (client == null) {
            return false;
        }
        //TODO: rewrite this logic for using MapStruct
        String newFullName = newUserInfo.getFullName();
        if (newFullName != null) {
            User user = client.getUser();
            user.setFullName(newFullName);
            client.setUser(user);
        }

        String newPhoneNumber = newUserInfo.getPhoneNumber();
        if (newPhoneNumber != null) {
            client.setPhoneNumber(newPhoneNumber);
        }
        clientRepo.save(client);

        return true;
    }
}
