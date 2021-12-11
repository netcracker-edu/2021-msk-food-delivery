package com.ncedu.fooddelivery.api.v1.services.impls;

import com.ncedu.fooddelivery.api.v1.dto.user.ClientInfoDTO;
import com.ncedu.fooddelivery.api.v1.dto.user.UserChangeInfoDTO;
import com.ncedu.fooddelivery.api.v1.entities.Client;
import com.ncedu.fooddelivery.api.v1.entities.User;
import com.ncedu.fooddelivery.api.v1.errors.badrequest.AlreadyExistsException;
import com.ncedu.fooddelivery.api.v1.errors.notfound.NotFoundEx;
import com.ncedu.fooddelivery.api.v1.repos.ClientRepo;
import com.ncedu.fooddelivery.api.v1.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    ClientRepo clientRepo;

    @Override
    public Client getClientById(Long id) {
        Optional<Client> client = clientRepo.findById(id);
        if (!client.isPresent()) {
            throw new NotFoundEx(id.toString());
        }
        return client.get();
    }

    @Override
    public ClientInfoDTO getClientDTOById(Long id) {
        Client client = getClientById(id);
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
        String newFullName = newUserInfo.getFullName();
        if (newFullName != null) {
            User user = client.getUser();
            user.setFullName(newFullName);
            client.setUser(user);
        }

        String newPhoneNumber = newUserInfo.getPhoneNumber();
        if (newPhoneNumber != null) {
            Client clientWithNewNumber = clientRepo.findByPhoneNumber(newPhoneNumber);
            if (clientWithNewNumber != null) {
                throw new AlreadyExistsException(newPhoneNumber);
            }
            client.setPhoneNumber(newPhoneNumber);
        }
        clientRepo.save(client);

        return true;
    }
}
