package com.ncedu.fooddelivery.api.v1.services.impls;

import com.ncedu.fooddelivery.api.v1.dto.ClientInfoDTO;
import com.ncedu.fooddelivery.api.v1.entities.Client;
import com.ncedu.fooddelivery.api.v1.entities.User;
import com.ncedu.fooddelivery.api.v1.repos.ClientRepo;
import com.ncedu.fooddelivery.api.v1.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    ClientRepo clientRepo;

    public Client getClientById(Long id) {
        Optional<Client> findedClient = clientRepo.findById(id);
        if (findedClient.isPresent()) {
            return findedClient.get();
        }
        return null;
    }

    public ClientInfoDTO getClientDTOById(Long id) {
        Optional<Client> optional = clientRepo.findById(id);
        if (!optional.isPresent()) {
            return null;
        }
        Client client = optional.get();
        return createClientInfoDTO(client);
    }

    private ClientInfoDTO createClientInfoDTO(Client client) {
        User user = client.getUser();
        return  new ClientInfoDTO(user.getId(), user.getRole().name(),
                user.getFullName(), user.getEmail(),
                user.getLastSigninDate(), user.getAvatarId(),
                client.getPhoneNumber(), client.getRating());
    }
}
