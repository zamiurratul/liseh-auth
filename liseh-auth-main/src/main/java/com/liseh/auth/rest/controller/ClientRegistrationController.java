package com.liseh.auth.rest.controller;

import com.liseh.auth.persistence.dto.ClientRegistrationDto;
import com.liseh.auth.persistence.entity.Client;
import com.liseh.auth.service.ClientRegistrationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "${api.version}/client", produces = MediaType.APPLICATION_JSON_VALUE)
public class ClientRegistrationController {
    private final ClientRegistrationService clientRegistrationService;

    public ClientRegistrationController(ClientRegistrationService clientRegistrationService) {
        this.clientRegistrationService = clientRegistrationService;
    }

    @RequestMapping(value = "/registration", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public ResponseEntity<Client> registerClient(@RequestBody ClientRegistrationDto registrationDto) {
        return new ResponseEntity<Client>(clientRegistrationService.registerClient(registrationDto), HttpStatus.OK);
    }
}
