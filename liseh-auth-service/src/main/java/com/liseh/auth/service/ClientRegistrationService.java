package com.liseh.auth.service;

import com.liseh.auth.persistence.dto.ClientRegistrationDto;
import com.liseh.auth.persistence.entity.Client;

public interface ClientRegistrationService {
    Client registerClient(ClientRegistrationDto registrationDto);
}
