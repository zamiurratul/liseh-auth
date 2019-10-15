package com.liseh.auth.service.impl;

import com.liseh.auth.persistence.dto.ClientRegistrationDto;
import com.liseh.auth.persistence.entity.Client;
import com.liseh.auth.repository.ClientRepository;
import com.liseh.auth.service.ClientRegistrationService;
import com.liseh.auth.util.CommonUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ClientServiceImpl implements ClientRegistrationService {
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    public ClientServiceImpl(ClientRepository clientRepository, PasswordEncoder passwordEncoder) {
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Client registerClient(ClientRegistrationDto registrationDto) {
        Client client = new Client();
        client.setUsername(registrationDto.getUsername());
        client.setClientId("ID_" + CommonUtil.randomUUID());
        client.setClientSecret(passwordEncoder.encode(registrationDto.getPassword()));
        client.setEmail(registrationDto.getEmail());
        client.setIsVerified(false);
        return clientRepository.save(client);
    }
}
