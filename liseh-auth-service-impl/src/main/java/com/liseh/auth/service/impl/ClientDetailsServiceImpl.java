package com.liseh.auth.service.impl;

import com.liseh.auth.persistence.dto.LisehClientDetails;
import com.liseh.auth.persistence.entity.Client;
import com.liseh.auth.repository.ClientRepository;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;

@Service
public class ClientDetailsServiceImpl implements ClientDetailsService {
    private final ClientRepository clientRepository;

    public ClientDetailsServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        Client client = clientRepository.findByClientId(clientId);
        if (client == null) {
            throw new ClientRegistrationException("No client registered with clientId " + clientId);
        }
        return new LisehClientDetails(client);
    }
}
